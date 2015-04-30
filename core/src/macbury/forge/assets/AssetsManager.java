package macbury.forge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.assets.assets.Asset;
import macbury.forge.assets.assets.ModelAsset;
import macbury.forge.assets.assets.TextureAsset;

import java.util.HashMap;

/**
 * Created by macbury on 16.10.14.
 */
public class AssetsManager implements Disposable {
  public static final String ASSETS_PREFIX = "assets/";
  private static final String TAG = "AssetsManager";
  private static final int MAX_TO_LOAD_PER_TICK = 10;
  private HashMap<String, Asset> loadedAssets;
  private Array<Asset> pendingAssets;

  public AssetsManager() {
    super();
    Gdx.app.log(TAG, "Initialized...");
    loadedAssets  = new HashMap<String, Asset>();
    pendingAssets = new Array<Asset>();
  }

  public Asset getAsset(Class<? extends Asset> assetClass, String path) {
    if (!loadedAssets.containsKey(path)) {
      Asset tempAsset = null;
      try {
        Gdx.app.log(TAG, "Adding pending asset: " + path);
        tempAsset = assetClass.newInstance();
        tempAsset.setManager(this);
        tempAsset.setPath(path);
        loadedAssets.put(path, tempAsset);
        pendingAssets.add(tempAsset);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return loadedAssets.get(path);
  }

  public TextureAsset getTexture(String path) {
    return (TextureAsset)getAsset(TextureAsset.class, path);
  }

  public ModelAsset getModel(String path) {
    return (ModelAsset)getAsset(ModelAsset.class, path);
  }

  public void loadPending() {
    while(pendingAssets.size > 0) {
      Asset asset = pendingAssets.pop();
      Gdx.app.log(TAG, "Loading: " + asset.getPath());
      asset.load();
    }
  }

  public boolean loadPendingInChunks() {
    int toLoadLeft = MAX_TO_LOAD_PER_TICK;
    while(pendingAssets.size > 0 || toLoadLeft == 0) {
      Asset asset = pendingAssets.pop();
      Gdx.app.log(TAG, "Loading: " + asset.getPath());
      asset.load();
      toLoadLeft--;
    }

    return pendingAssets.size == 0;
  }

  @Override
  public synchronized void dispose() {
    for(Asset asset : loadedAssets.values()) {
      Gdx.app.log(TAG, "Disposing: " + asset.getPath());
      asset.dispose();
    }
    loadedAssets.clear();
    pendingAssets.clear();
  }

  public FileHandle resolvePath(Asset asset, String path) {
    return Gdx.files.internal(path);
  }
}
