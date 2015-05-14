package macbury.forge.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.screens.AbstractScreen;
import macbury.forge.scripts.modules.BaseGameScriptModule;
import macbury.forge.scripts.modules.LoggingGameScriptModule;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaPackage;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by macbury on 12.05.15.
 */
public class ScriptManager implements Disposable {
  public static final String STORE_DIR = "db/scripts/";
  public static final String FILE_EXT  = ".fjs";
  private static final String TAG      = "ScriptManager";

  private static final Array<Class> packagesToImport = new Array<Class>(
      new Class[] {
          Matrix4.class,
          PerspectiveCamera.class,
          ShapeRenderer.class,
          Vector3.class,
          GL20.class,
          AbstractScreen.class
      }
  );
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
    gameScript.registerObjectAsModule("$screens", ForgE.screens);
    gameScript.registerObjectAsModule("$graphics", Gdx.graphics);
    gameScript.registerObjectAsModule("$gl", Gdx.gl);
    gameScript.registerObjectAsModule("$gdxApp", Gdx.app);
    gameScript.registerObjectAsModule("$levels", ForgE.levels);
    //TODO: Find better solution!
    for (int i = 0; i < packagesToImport.size; i++) {
      String classToImport = packagesToImport.get(i).getName();
      String simpleName    = packagesToImport.get(i).getSimpleName();
      Gdx.app.log(TAG, "Importing class: " + classToImport);
      String stub          = "var "+simpleName+" = Packages."+classToImport+";";
      context.evaluateString(mainScope, stub, "<import>", 0, null);
    }
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
