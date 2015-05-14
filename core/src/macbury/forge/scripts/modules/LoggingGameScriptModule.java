package macbury.forge.scripts.modules;

import com.badlogic.gdx.Gdx;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 13.05.15.
 */
public class LoggingGameScriptModule extends BaseGameScriptModule {

  @Override
  public void compile(Context context, ScriptableObject mainScope) {
    if (moduleObject == null)
      moduleObject = Context.javaToJS(new LoggerWrapper(), context.initStandardObjects());
  }

  public class LoggerWrapper {
    public void info(String tag, String message) {
      Gdx.app.log(tag, message);
    }

    public void debug(String tag, String message) {
      Gdx.app.debug(tag, message);
    }
  }
}
