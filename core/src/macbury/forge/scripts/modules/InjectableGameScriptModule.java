package macbury.forge.scripts.modules;

import com.badlogic.gdx.utils.Array;
import macbury.forge.scripts.GameScriptLib;
import macbury.forge.scripts.ScriptAnnotateHelper;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 12.05.15.
 */
public class InjectableGameScriptModule extends BaseGameScriptModule {
  public final Array<String> inject;
  public final BaseFunction function;
  private final GameScriptLib moduleProvider;
  private boolean compiled;

  public InjectableGameScriptModule(BaseFunction function, GameScriptLib moduleProvider) {
    this.inject         = ScriptAnnotateHelper.annotate(function);
    this.function       = function;
    this.moduleProvider = moduleProvider;
  }


  @Override
  public void compile(Context context, ScriptableObject scope) {
    if (!compiled) {
      Object[] args = new Object[inject.size];
      for (int i = 0; i < inject.size; i++) {
        args[i] = moduleProvider.get(inject.get(i), context, scope).get();
      }
      moduleObject = function.call(context, scope, scope, args);
      compiled     = true;
    }
  }
}