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

  public boolean isLoaded() {
    return object != null;
  }

  public T get() {
    return object;
  }

  @Override
  public void dispose() {
    object.dispose();
  }

  public String getPath() {
    return path;
  }
}
