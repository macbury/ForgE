package macbury.forge.scripts;

import com.badlogic.gdx.utils.Disposable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 12.05.15.
 */
public class ScriptManager implements Disposable {
  private final Context context;
  private final ScriptableObject mainScope;
  private GameScript gameScript;

  public ScriptManager() {
    this.context   = Context.enter();
    this.mainScope = context.initStandardObjects();

    addGameObj();
  }

  public void eval(String src) {
    context.evaluateString(mainScope, src, "<eval>", 1, null);
  }

  private void addGameObj() {
    this.gameScript              = new GameScript();
    Object wrappedGameScript     = Context.javaToJS(gameScript, mainScope);
    ScriptableObject.putProperty(mainScope, "game", wrappedGameScript);
  }

  @Override
  public void dispose() {
    Context.exit();
  }
}
