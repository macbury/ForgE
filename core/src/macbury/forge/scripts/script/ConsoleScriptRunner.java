package macbury.forge.scripts.script;

import com.badlogic.gdx.Gdx;
import org.apache.bsf.BSFException;
import org.jruby.embed.bsf.JRubyEngine;

/**
 * Created by macbury on 08.09.15.
 */
public class ConsoleScriptRunner extends BaseScriptRunner {
  private final String src;

  public ConsoleScriptRunner(String src) {
    this.src = src;
  }

  @Override
  public void run(JRubyEngine engine) throws BSFException {
    Object result = engine.eval("<console>", 0, 0, src);
    Gdx.app.log("<console>", result.toString());

  }

  @Override
  public void dispose() {

  }
}
