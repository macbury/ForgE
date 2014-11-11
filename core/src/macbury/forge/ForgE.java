package macbury.forge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import macbury.forge.assets.AssetsManager;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.db.GameDatabase;
import macbury.forge.storage.StorageManager;
import macbury.forge.graphics.GraphicsUtils;
import macbury.forge.input.InputManager;
import macbury.forge.screens.ScreenManager;
import macbury.forge.shaders.utils.ShadersManager;

public class ForgE extends Game {
  public static GraphicsUtils   graphics;
  public static ScreenManager   screens;
  public static AssetsManager   assets;
  public static ShadersManager  shaders;
  public static Config          config;
  public static StorageManager  storage;
  public static GameDatabase    db;
  public static BlocksProvider  blocks;
  public static InputManager    input;
  private ForgEBootListener bootListener;


  public ForgE(Config config) {
    super();
    this.config = config;
  }

  @Override
  public void create () {
    storage  = new StorageManager();

    db       = storage.loadOrInitializeDB();
    graphics = new GraphicsUtils();
    screens  = new ScreenManager(this);
    assets   = new AssetsManager();
    shaders  = new ShadersManager();
    input    = new InputManager();
    blocks   = new BlocksProvider();
    Gdx.input.setInputProcessor(input);
    if (bootListener != null) {
      bootListener.afterEngineCreate(this);
    }
  }

  public void setBootListener(ForgEBootListener bootListener) {
    this.bootListener = bootListener;
  }
}
