package macbury.forge.promises;

/**
 * Created by macbury on 16.03.15.
 */
public abstract class GdxFutureTask<K,V> extends FutureTask<K,V> {
  private final GdxPromiseFrameTicker ticker;

  public GdxFutureTask(GdxPromiseFrameTicker ticker) {
    super();
    this.ticker = ticker;
  }

  @Override
  public void execute(K object) {
    this.ticker.setTarget(this);
  }

  @Override
  protected void done(V result) {
    super.done(result);
    this.ticker.setTarget(null);
  }

  @Override
  protected void reject(Exception e) {
    super.reject(e);
    this.ticker.setTarget(null);
  }

  public abstract void tick(float delta);
}
