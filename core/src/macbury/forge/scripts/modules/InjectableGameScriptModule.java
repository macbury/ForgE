package macbury.forge.scripts.modules;

import com.badlogic.gdx.utils.Array;
import macbury.forge.scripts.GameScriptLib;
import macbury.forge.scripts.ScriptAnnotateHelper;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 12.05.15.
 */
public class InjectableGameScriptModule extends BaseGameScriptModule {
  private Array<String> inject;
  protected BaseFunction function;
  private GameScriptLib moduleProvider;
  private boolean compiled;

  public InjectableGameScriptModule(BaseFunction function, GameScriptLib moduleProvider) {
    this.inject         = ScriptAnnotateHelper.annotate(function);
    this.function       = function;
    this.moduleProvider = moduleProvider;
  }

  protected Object[] getAttributes(Context context, ScriptableObject mainScope) {
    Object[] args = new Object[inject.size];
    for (int i = 0; i < inject.size; i++) {
      args[i] = moduleProvider.get(inject.get(i), context, mainScope).get();
    }

    return args;
  }

  @Override
  public void compile(Context context, ScriptableObject mainScope) {
    if (!compiled) {
      ScriptableObject scope = context.initStandardObjects();
      scope.setParentScope(mainScope);
      scope.setParentScope(null);
      moduleObject = function.call(context, scope, scope, getAttributes(context, mainScope));
      compiled     = true;
    }
  }

}