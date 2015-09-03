package macbury.forge.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.components.PlayerComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.components.RigidBodyComoponent;
import macbury.forge.level.Level;
import macbury.forge.screens.AbstractScreen;
import macbury.forge.terrain.geometry.DynamicGeometryProvider;
import macbury.forge.terrain.geometry.FileGeometryProvider;
import macbury.forge.ui.GameplayView;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.bsf.JRubyEngine;
/**
 * Created by macbury on 01.09.15.
 */
public class ScriptThread extends Thread implements Disposable {
  private static final String TAG       = "ScriptThread";
  private static final String RUBY_LANG = "jruby20";
  private Listener listener;
  private BSFManager manager;
  private JRubyEngine ruby;
  private ScriptingContainer container;
  private FileHandle mainFileHandle;

  private static final Array<Class> packagesToImport = new Array<Class>(
      new Class[] {
          Matrix4.class,
          PerspectiveCamera.class,
          ShapeRenderer.class,
          Vector3.class,
          GL20.class,
          AbstractScreen.class,
          Level.class,
          FirstPersonCameraController.class,
          Input.class,
          PositionComponent.class,
          PlayerComponent.class,
          RigidBodyComoponent.class,
          FileGeometryProvider.class,
          DynamicGeometryProvider.class,
          ForgE.class,
          Gdx.class,
          Color.class,
          Runnable.class,
          GameplayView.class
      }
  );

  public ScriptThread(Listener listener) {
    this.listener = listener;
  }

  private void executeScript(String filename, String source) {
    try {
      ruby.exec(filename, 1, 1, source);
    } catch (BSFException e) {
      listener.onRubyError(e.getTargetException());
    }
  }

  @Override
  public void run() {
    initEngine();
    initalizeContext();
    runMain();

    while(ruby != null) {
     /* try {
        //manager.exec(RUBY_LANG, "(java)", 1, 1, "a += 1");
        //manager.exec(RUBY_LANG, "(java)", 1, 1, "$app.log('s',a.to_s)");
      } catch (BSFException e) {
        e.printStackTrace();
      }*/
    }
  }

  private void initEngine() {
    Gdx.app.log(TAG, "Initializing engine...");
    BSFManager.registerScriptingEngine(RUBY_LANG, "org.jruby.embed.bsf.JRubyEngine", new String[]{"rb"});
    this.manager = new BSFManager();
    this.ruby    = new JRubyEngine();
    try {
      ruby.initialize(manager, RUBY_LANG, null);
    } catch (BSFException e) {
      listener.onRubyError(e);
    }
  }

  private void initalizeContext() {
    Gdx.app.log(TAG, "Initializing context...");
    executeScript("<init>", "def import(path)\n" +
            "  require ForgE.files.internal(path+'.rb').path()\n" +
            "end"
    );

    for (int i = 0; i < packagesToImport.size; i++) {
      Gdx.app.log(TAG, "Setting: " + packagesToImport.get(i).getCanonicalName());
      executeScript("<init>", "java_import '" + packagesToImport.get(i).getCanonicalName()+"';");
    }
  }

  private void runMain() {
    Gdx.app.log(TAG, "Running main.rb");
    executeScript(mainFileHandle.path(), mainFileHandle.readString());
  }

  public void start(String path) {
    FileHandle handle = ForgE.files.internal(path);
    Gdx.app.log(TAG, "Running: " + handle.path());
    if (handle.exists()) {
      this.mainFileHandle = handle;
    } else {
      throw new GdxRuntimeException("Could not load file: " + path);
    }
    start();
  }

  @Override
  public void dispose() {
    ruby.terminate();
    ruby = null;
    manager = null;
    mainFileHandle = null;
    container = null;
    listener  = null;
  }

  public interface Listener {
    public void onRubyError(Throwable error);
  }
}
