package macbury.forge.scripts;

import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.db.models.Teleport;
import macbury.forge.scripts.modules.BaseGameScriptModule;
import macbury.forge.scripts.modules.InjectableGameScriptModule;
import macbury.forge.scripts.modules.LoggingGameScriptModule;
import macbury.forge.scripts.modules.ObjectGameScriptModule;
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
  private InjectableGameScriptModule mainRunFunction;

  public GameScriptLib() {
    modules = new HashMap<String, BaseGameScriptModule>();
    wrapper = new Wrapper();
  }

  public Wrapper getWrapper() {
    return wrapper;
  }

  public BaseGameScriptModule get(String name, Context context, ScriptableObject scope) {
    BaseGameScriptModule module = modules.get(name);
    if (module == null) {
      throw new GdxRuntimeException("Could not find module with name: " + name);
    } else {
      module.compile(context, scope);
      return module;
    }
  }

  public void compileModules(Context context, ScriptableObject scope) {
    for(String moduleName : modules.keySet()) {
      get(moduleName, context, scope);
    }
    if (mainRunFunction == null) {
      throw new GdxRuntimeException("No main run function!!!");
    } else {
      mainRunFunction.compile(context,scope);
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

    public void run(BaseFunction callback) {
      mainRunFunction = new InjectableGameScriptModule(callback, GameScriptLib.this);
    }
  }
}
