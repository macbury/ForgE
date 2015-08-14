package macbury.forge.editor.parell;

/**
 * Created by macbury on 06.11.14.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.utils.MethodInvoker;

import javax.swing.*;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public abstract class Job<T> implements Disposable {
  private Class<T>          type;
  private Reference<Object> whandler;
  private String            callback;

  public Job(Class<T> type) {
    this.type = type;
  }

  public abstract boolean isBlockingUI();
  public abstract boolean performCallbackOnOpenGlThread();
  public abstract T perform();

  public void setCallback(Object handler, String callback) {
    this.callback = callback;
    this.whandler = new WeakReference<Object>(handler);
  }

  public Exception start() {
    try {
      final T result = this.perform();

      if (whandler != null && callback != null && whandler.get() != null) {
        final Class<?>[] performSig = { type, getClass() };
        final Class<?>[] fallbackSig = { Object.class, getClass() };

        if (performCallbackOnOpenGlThread()) {
          Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
              Object handler = whandler.get();
              try {
                MethodInvoker.invokeHandler(handler, callback, true, true, performSig, fallbackSig, result, Job.this);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          });
        } else {
          Object handler = whandler.get();
          synchronized (handler) {
            MethodInvoker.invokeHandler(handler, callback, true, true, performSig, fallbackSig, result, this);
          }
        }

      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return e;
    }
  }

}
