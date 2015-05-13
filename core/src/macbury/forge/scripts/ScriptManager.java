package macbury.forge.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.scripts.modules.BaseGameScriptModule;
import macbury.forge.scripts.modules.LoggingGameScriptModule;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by macbury on 12.05.15.
 */
public class ScriptManager implements Disposable {
  public static final String STORE_DIR = "db/scripts/";
  public static final String FILE_EXT  = ".fjs";
  private static final String TAG      = "ScriptManager";
  private final Context context;
  private final ScriptableObject mainScope;
  private GameScriptLib gameScript;

  public FileFilter scriptFileFilter = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      return pathname.getName().endsWith(FILE_EXT) || pathname.isDirectory();
    }
  };

  public ScriptManager() {
    this.context   = Context.enter();
    this.mainScope = context.initStandardObjects();

    addGameObj();
  }

  private void getHandles(FileHandle begin, Array<FileHandle> handles)  {
    FileHandle[] newHandles = begin.list(scriptFileFilter);
    for (FileHandle f : newHandles) {
      if (f.isDirectory()) {
        getHandles(f, handles);
      } else {
        handles.add(f);
      }
    }
  }

  public void loadAndRun() {
    registerDefaultModules();
    Array<FileHandle> scriptFiles = new Array<FileHandle>();
    getHandles(Gdx.files.internal(STORE_DIR), scriptFiles);
    for (FileHandle handle : scriptFiles) {
      Gdx.app.log(TAG, "Loading: " + handle.path());
      eval(handle);
    }

    gameScript.compileModules(context, mainScope);
  }

  private void registerDefaultModules() {
    gameScript.registerModule("$log", new LoggingGameScriptModule());
    gameScript.registerObjectAsModule("$startPosition", ForgE.db.startPosition);
  }


  public void eval(String src) {
    context.evaluateString(mainScope, src, "<eval>", 1, null);
  }

  private void eval(FileHandle handle) {
    context.evaluateString(mainScope, handle.readString(), handle.name(), 0, null);
  }

  private void addGameObj() {
    this.gameScript              = new GameScriptLib();
    Object wrappedGameScript     = Context.javaToJS(gameScript.getWrapper(), mainScope);
    ScriptableObject.putProperty(mainScope, "game", wrappedGameScript);
  }

  @Override
  public void dispose() {
    Context.exit();
  }
}
