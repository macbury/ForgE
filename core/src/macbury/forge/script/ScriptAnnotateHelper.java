package macbury.forge.script;

import com.badlogic.gdx.utils.Array;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by macbury on 12.05.15.
 */
public class ScriptAnnotateHelper {
  public static Pattern FN_ARGS         = Pattern.compile("^function\\s*[^\\(]*\\(\\s*([^\\)]*)\\)");
  public static Pattern FN_ARG_SPLIT    = Pattern.compile(",");
  public static Pattern FN_ARG          = Pattern.compile("^\\s*(_?)(.+?)\\1\\s*$");
  public static Pattern STRIP_COMMENTS  = Pattern.compile("((\\/\\/.*$)|(\\/\\*[\\s\\S]*?\\*\\/)|(\\s))");

  public static Array<String> annotate(BaseFunction function) {
    Array<String> inject     = new Array<String>();
    String funcSrc           = Context.toString(function);

    String fnText           = STRIP_COMMENTS.matcher(funcSrc).replaceAll("");
    Matcher argDeclMatcher  = FN_ARGS.matcher(fnText);
    argDeclMatcher.find();
    String argDecl          = argDeclMatcher.group(1);

    for (String arg : FN_ARG_SPLIT.split(argDecl)) {
      Matcher argMatcher = FN_ARG.matcher(arg);
      if (argMatcher.find()) {
        inject.add(argMatcher.group(0));
      }
    }

    return inject;
  }
}
