package macbury.forge.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 06.05.15.
 */
public class ArgsParser {
  private final Array<String> arguments;
  public final boolean fullscreen;

  public ArgsParser(String[] arg) {
    this.arguments = new Array<String>(arg);

    this.fullscreen = hasFlag("--fullscreen");
  }

  public boolean hasFlag(String name) {
    return arguments.contains(name, false);
  }
}
