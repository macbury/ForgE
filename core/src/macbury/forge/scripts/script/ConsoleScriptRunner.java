package macbury.forge.scripts.script;

import macbury.forge.ForgE;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.jruby.embed.bsf.JRubyEngine;

/**
 * Created by macbury on 08.09.15.
 */
public class ConsoleScriptRunner extends BaseScriptRunner {
  private static final String TAG = "<console>";
  private final String src;

  public ConsoleScriptRunner(String src) {
    this.src = src;
  }

  @Override
  public void run(JRubyEngine engine, BSFManager manager) throws BSFException {
    try {
      ForgE.log(TAG, src);
      Object result = engine.eval(TAG, 0, 0, src);
      if (result != null) {
        ForgE.log(TAG, "=> "+result.toString());
      } else {
        ForgE.log(TAG, "=> nil");
      }
    } catch (BSFException e) {
      ForgE.log(TAG, e.getTargetException().toString());
    }
  }

  @Override
  public void dispose() {

  }
}
