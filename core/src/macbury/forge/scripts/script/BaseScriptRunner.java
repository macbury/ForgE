package macbury.forge.scripts.script;

import com.badlogic.gdx.utils.Disposable;
import org.apache.bsf.BSFException;
import org.jruby.embed.bsf.JRubyEngine;

/**
 * Created by macbury on 08.09.15.
 */
public abstract class BaseScriptRunner implements Disposable {

  public abstract void run(JRubyEngine engine) throws BSFException;
}
