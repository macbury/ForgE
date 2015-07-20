package macbury.forge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Array;
import macbury.forge.assets.AssetsManager;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.db.GameDatabase;
import macbury.forge.graphics.GraphicsUtils;
import macbury.forge.graphics.fbo.FrameBufferManager;
import macbury.forge.input.InputManager;
import macbury.forge.level.LevelManager;
import macbury.forge.screens.ScreenManager;
import macbury.forge.scripts.ScriptManager;
import macbury.forge.shaders.utils.ShadersManager;
import macbury.forge.storage.StorageManager;
import macbury.forge.entities.EntityManager;

public class ForgE extends Game {
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
  public static FrameBufferManager fb;

  private Array<ForgEBootListener>  bootListeners;

  public ForgE(Config config) {
    super();
    this.config         = config;
    this.bootListeners  = new Array<ForgEBootListener>();
  }

  @Override
  public void create () {
    Bullet.init(false, true);
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
    Gdx.input.setInputProcessor(input);
    for (ForgEBootListener listener : bootListeners) {
      listener.afterEngineCreate(this);
    }
  }

  @Override
  public void render() {
    graphics.updateTime();
    super.render();

  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    fb.resize(width, height, true);
  }

  public void addBootListener(ForgEBootListener bootListener) {
    this.bootListeners.add(bootListener);
  }

  @Override
  public void dispose() {
    scripts.dispose();
    super.dispose();
  }


}
