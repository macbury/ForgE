package macbury.forge.db;

/**
 * Created by macbury on 07.11.14.
 */
public class GameDatabase {
  public static final String FILE_NAME = "game.db";
  public String title;
  public int currentyUID = 0;

  /**
   * Create int uuid
   * @return unique id
   */
  public int uid() {
    return currentyUID += 1;
  }

  /**
   * Bootstrap db with default values
   */
  public void bootstrap() {
    title = "ForgE Project";
    currentyUID = 0;
  }
}
