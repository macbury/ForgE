package macbury.forge.promises;

/**
 * Created by macbury on 16.03.15.
 */
public interface Promise<T> {
  public void success(T result);
  public void error(Exception reason);
}
