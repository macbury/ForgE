package macbury.forge.script;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;

/**
 * Created by macbury on 11.05.15.
 */
public class ScriptManager implements Disposable {
  private final Context context;

  public ScriptManager() {
    context = Context.enter();
  }

  @Override
  public void dispose() {
    Context.exit();
  }


}
