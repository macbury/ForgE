package macbury.forge.scripts;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.scripts.modules.*;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;

/**
 * Created by macbury on 12.05.15.
 */
public class GameScriptLib {
  private final HashMap<String, BaseGameScriptModule> modules;
  private final Wrapper wrapper;
  private Array<InjectableGameScriptModule> runFunctions;

  public GameScriptLib() {
    modules       = new HashMap<String, BaseGameScriptModule>();
    runFunctions  = new Array<InjectableGameScriptModule>();
    wrapper = new Wrapper();
  }

  public Wrapper getWrapper() {
    return wrapper;
  }

  public BaseGameScriptModule get(String name, Context context, ScriptableObject mainScope) {
    BaseGameScriptModule module = modules.get(name);
    if (module == null) {
      throw new GdxRuntimeException("Could not find module with name: " + name);
    } else {
      module.compile(context, mainScope);
      return module;
    }
  }

  public void compileModules(Context context, ScriptableObject mainScope) {
    for(String moduleName : modules.keySet()) {
      get(moduleName, context, mainScope);
    }
    if (runFunctions.size == 0) {
      throw new GdxRuntimeException("No main run function!!!");
    } else {
      for (int i = 0; i < runFunctions.size; i++) {
        runFunctions.get(i).compile(context, mainScope);
      }
    }
  }

  public void registerModule(String name, BaseGameScriptModule module) {
    modules.put(name, module);
  }

  public void registerObjectAsModule(String name, Object objectToModule) {
    modules.put(name, new ObjectGameScriptModule(objectToModule));
  }

  public class Wrapper {
    public void module(String name, BaseFunction function) {
      modules.put(name, new InjectableGameScriptModule(function, GameScriptLib.this));
    }

    public void provider(String name, BaseFunction function) {
      modules.put(name, new InjectableProviderGameScriptModule(function, GameScriptLib.this));
    }

    public void run(BaseFunction callback) {
      runFunctions.add(new InjectableGameScriptModule(callback, GameScriptLib.this));
    }
  }
}
