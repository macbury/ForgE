package macbury.forge.editor.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 08.05.15.
 */
public class InterfaceTrigger<T> implements Disposable {
  private Array<T> listeners;
  public InterfaceTrigger() {
    listeners = new Array<T>();
  }

  public void addListener(T listener) {
    listeners.add(listener);
  }

  public void removeListener(T listener) {
    listeners.removeValue(listener, true);
  }

  public void trigger(Trigger<T> triggerCallback) {
    for (T listener : listeners) {
      triggerCallback.onListenerTrigger(listener);
    }
  }

  @Override
  public void dispose() {
    listeners.clear();
  }

  public interface Trigger<T> {
    public void onListenerTrigger(T listener);
  }
}
