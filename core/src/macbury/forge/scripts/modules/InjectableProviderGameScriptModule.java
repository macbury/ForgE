package macbury.forge.scripts.modules;

import macbury.forge.scripts.GameScriptLib;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 14.05.15.
 */
public class InjectableProviderGameScriptModule extends InjectableGameScriptModule {
  private Context context;
  private ScriptableObject mainScope;

  public InjectableProviderGameScriptModule(BaseFunction function, GameScriptLib moduleProvider) {
    super(function, moduleProvider);
  }

  @Override
  public void compile(Context context, ScriptableObject mainScope) {
    this.mainScope = mainScope;
    this.context = context;
  }

  @Override
  public Object get() {
    ScriptableObject scope = context.initStandardObjects();
    moduleObject           = function.call(context, scope, scope, getAttributes(context, mainScope));
    return super.get();
  }
}
