package macbury.forge.scripts.modules;

import macbury.forge.scripts.GameScriptLib;
import org.mozilla.javascript.*;

/**
 * Created by macbury on 13.05.15.
 */
public class ScreenGameScriptModule extends InjectableGameScriptModule {
  private boolean compiled;
  private static int uuid = 1;

  public static int uuid() {
    return uuid+=1;
  }

  public ScreenGameScriptModule(BaseFunction function, GameScriptLib moduleProvider) {
    super(function, moduleProvider);
    this.compiled = false;
  }

  @Override
  public void compile(Context context, ScriptableObject mainScope) {
    if (!compiled) {
      /*ScriptableObject scope      = context.initStandardObjects();
      Object jsInterface          = function.call(context, scope, scope, getAttributes(context));

      InterfaceAdapter.

      String jsInterfaceStubName  = "forge_interface_"+uuid();
      String stub                 = "new JavaAdapter(Packages.macbury.forge.screens.AbstractScreen, "+ jsInterfaceStubName +");" ;
      scope.defineProperty(jsInterfaceStubName, jsInterface, 0);
      moduleObject            = context.evaluateString(scope, stub, "<stub>", 0,null);
      compiled     = true;*/
    }
  }
}
