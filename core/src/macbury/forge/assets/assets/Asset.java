package macbury.forge.assets.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.assets.AssetsManager;

/**
 * Created by macbury on 08.03.15.
 */
public abstract class Asset<T extends Disposable> implements Disposable {
  protected AssetsManager manager;
  private T object;
  private String path;
  private int retainCount = 0;

  public Asset() {
  }

  public void setManager(AssetsManager manager) {
    this.manager = manager;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean load() {
    if (!isLoaded()) {
      object = loadObject(manager.resolvePath(this, path));
      return object != null;
    } else {
      return true;
    }
  }

  protected abstract T loadObject(FileHandle file);

  private boolean isLoaded() {
    return object != null;
  }

  public T get() {
    load();
    return object;
  }

  public boolean isUnused() {
    return retainCount <= 0;
  }

  public void retain() {
    retainCount++;
  }

  public void release() {
    retainCount--;
  }

  @Override
  public void dispose() {
    if (isLoaded()) {
      object.dispose();
    }

    retainCount = 0;
  }

  public String getPath() {
    return path;
  }
}
