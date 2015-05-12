package macbury.forge.editor.windows;

import com.badlogic.gdx.utils.Array;
import macbury.forge.scripts.ScriptManager;
import org.lwjgl.Sys;
import org.mozilla.javascript.*;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by macbury on 11.05.15.
 */
public class JSTest {

  public static void main( String[] args ){
    ScriptManager manager = new ScriptManager();
    manager.eval("game.module('$log', function() { return {}; })");
    manager.eval("game.run( function (test, test, $log) { $log.info('Hello world!') });");
    manager.dispose();
  }


}
