package macbury.forge.scripts;

import com.badlogic.gdx.utils.Array;
import org.mozilla.javascript.BaseFunction;

/**
 * Created by macbury on 12.05.15.
 */
public class GameScriptModule {
  private final Array<String> inject;

  public GameScriptModule(BaseFunction function) {
    this.inject = ScriptAnnotateHelper.annotate(function);
  }
}