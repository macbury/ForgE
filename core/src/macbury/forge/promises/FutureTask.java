package macbury.forge.promises;

import com.badlogic.gdx.Gdx;

/**
 * Created by macbury on 16.03.15.
 * K - input
 * V - result
 */
public abstract class FutureTask<K, V> {
  private Promise<V> promise;

  public FutureTask() {

  }

  public abstract void execute(K object);

  protected void done(final V result) {
    Gdx.app.log(getClass().getSimpleName(), "Done");
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        if (promise != null) {
          promise.success(result);
        }
      }
    });
  }

  protected void reject(final Exception e) {
    Gdx.app.log(getClass().getSimpleName(), "Error:" + e.toString());
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        if (promise != null) {
          promise.error(e);
        }
      }
    });
  }

  public FutureTask<V, ?> then(final FutureTask<V, ?> otherTask) {
    promise = new Promise<V>() {
      @Override
      public void success(V result) {
        otherTask.execute(result);
      }

      @Override
      public void error(Exception reason) {
        otherTask.reject(reason);
      }
    };
    return otherTask;
  }

  public FutureTask then(Promise promise) {
    this.promise = promise;
    return this;
  }
}
