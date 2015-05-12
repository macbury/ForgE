package macbury.forge.editor.windows;

import com.badlogic.gdx.utils.Array;
import macbury.forge.script.ScriptAnnotateHelper;
import org.lwjgl.Sys;
import org.mozilla.javascript.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by macbury on 11.05.15.
 */
public class JSTest {

  public static void main( String[] args ){
    Context context              = Context.enter(); {
      Scriptable scope             = context.initStandardObjects();
      GameScript gameScript        = new GameScript();
      Object wrappedGameScript     = Context.javaToJS(gameScript, scope);
      ScriptableObject.putProperty(scope, "game", wrappedGameScript);
      context.evaluateString(scope, "game.run( function (test, test, $log) { $log.info('Hello world!') });", "<cmd>", 1, null);
      context.evaluateString(scope, "game.module('$log', function() { return {}; })", "<cmd>", 1, null);
      //gameScript.callback.call(context, scope, scope, null);
    } Context.exit();
  }

  public static class GameScript {

    public void module(String name, BaseFunction function) {

    }

    public void run(BaseFunction callback) {
      //this.callback = callback;
      Array<String> inject     = ScriptAnnotateHelper.annotate(callback);
      System.out.print(inject.toString());
    }
  }

}
