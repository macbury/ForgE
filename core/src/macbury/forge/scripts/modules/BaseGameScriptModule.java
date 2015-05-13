package macbury.forge.scripts.modules;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 13.05.15.
 */
public abstract class BaseGameScriptModule {
  protected Object moduleObject;

  public Object get() {
    return moduleObject;
  }
  public abstract void compile(Context context, ScriptableObject scope);
}
