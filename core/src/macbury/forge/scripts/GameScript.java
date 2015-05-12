package macbury.forge.scripts;

import org.mozilla.javascript.BaseFunction;

import java.util.HashMap;

/**
 * Created by macbury on 12.05.15.
 */
public class GameScript {
  private HashMap<String, GameScriptModule> modules;
  private GameScriptModule mainRunFunction;

  public GameScript() {
    modules = new HashMap<String, GameScriptModule>();
  }

  public void module(String name, BaseFunction function) {
    modules.put(name, new GameScriptModule(function));
  }

  public void run(BaseFunction callback) {
    mainRunFunction = new GameScriptModule(callback);
  }
}
