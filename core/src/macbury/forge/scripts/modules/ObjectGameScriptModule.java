package macbury.forge.scripts.modules;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 13.05.15.
 */
public class ObjectGameScriptModule extends BaseGameScriptModule {

  private final Object toJava;

  public ObjectGameScriptModule(Object toJava) {
    this.toJava = toJava;
  }

  @Override
  public void compile(Context context, ScriptableObject mainScope) {
    if (moduleObject == null)
      moduleObject = Context.javaToJS(toJava, context.initStandardObjects());
  }
}
