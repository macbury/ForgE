package macbury.forge;

/**
 * Created by macbury on 15.10.14.
 */
public interface ForgEBootListener {
  /**
   * After entities created all managers and allocated all resources
   * @param engine
   */
  public void afterEngineCreate(ForgE engine);
}
