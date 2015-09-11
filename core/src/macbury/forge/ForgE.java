package macbury.forge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import macbury.forge.assets.AssetsManager;
import macbury.forge.assets.FileManager;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.db.GameDatabase;
import macbury.forge.graphics.GraphicsUtils;
import macbury.forge.graphics.fbo.FrameBufferManager;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.input.InputManager;
import macbury.forge.level.LevelManager;
import macbury.forge.screens.ScreenManager;
import macbury.forge.scripts.ScriptManager;
import macbury.forge.shaders.utils.ShadersManager;
import macbury.forge.storage.StorageManager;
import macbury.forge.entities.EntityManager;
import macbury.forge.time.TimeManager;
import macbury.forge.ui.UIManager;

public class ForgE extends Game {
  private static final String TAG = "Forge";
  public static GraphicsUtils       graphics;
  public static ScreenManager       screens;
  public static AssetsManager       assets;
  public static ShadersManager      shaders;
  public static Config              config;
  public static StorageManager      storage;
  public static GameDatabase        db;
  public static BlocksProvider      blocks;
  public static InputManager        input;
  public static EntityManager       entities;
  public static LevelManager        levels;
  public static ScriptManager       scripts;
  public static FrameBufferManager  fb;
  public static TimeManager         time;
  public static Thread              thread;
  public static UIManager           ui;
  public static String[]            args;
  private Array<ForgEBootListener>  bootListeners;
  public static FileManager         files;

  private static Array<LogListener>        listeners = new Array<LogListener>();

  public ForgE(Config config, String[] args) {
    super();
    this.args           = args;
    this.config         = config;
    this.bootListeners  = new Array<ForgEBootListener>();
  }

  @Override
  public void create () {
    Bullet.init(false, true);
    files         = new FileManager();
    storage       = new StorageManager();
    db            = storage.loadOrInitializeDB();
    graphics      = new GraphicsUtils();
    screens       = new ScreenManager(this);
    assets        = new AssetsManager();
    shaders       = new ShadersManager();
    input         = new InputManager();
    blocks        = new BlocksProvider();
    levels        = new LevelManager(storage);
    entities      = new EntityManager();
    scripts       = new ScriptManager();
    fb            = new FrameBufferManager();
    time          = new TimeManager();
    thread        = Thread.currentThread();
    ui            = new UIManager();

    Gdx.input.setInputProcessor(input);
    for (ForgEBootListener listener : bootListeners) {
      listener.afterEngineCreate(this);
    }

    ForgE.input.addProcessor(0, ui);

  }

  @Override
  public void render() {
    ForgE.graphics.clearAll(Color.BLACK);
    graphics.updateTime();
    super.render();
    ui.act();
    ui.draw();
  }

  @Override
  public void resize(int width, int height) {
    fb.resize(width, height, true);
    ui.getViewport().update(width, height, true);
    super.resize(width, height);
  }

  public void addBootListener(ForgEBootListener bootListener) {
    this.bootListeners.add(bootListener);
  }

  @Override
  public void dispose() {
    scripts.dispose();
    super.dispose();
  }

  public static boolean isOpenGlCurrentThread() {
    return Thread.currentThread() == thread;
  }

  public static void log(String tag, String string) {
    Gdx.app.log(tag, string);
    for (int i = 0; i < listeners.size; i++) {
      listeners.get(i).onLogResult(tag, string);
    }
  }

  public static void addLogListener(LogListener listener) {
    listeners.add(listener);
  }

  public static void removeLogListener(LogListener listener) {
    listeners.removeValue(listener, true);
  }

  public interface LogListener {
    public void onLogResult(String tag, String msg);
  }
}
