package macbury.forge.assets.assets;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 04.05.15.
 */
public abstract class AssetWithDependencies<T extends Disposable> extends Asset<T> {
  private Array<Asset> dependencies = new Array<Asset>();

  protected void addDependency(Asset asset) {
    dependencies.add(asset);
  }

  protected void removeDependency(Asset asset) {
    dependencies.removeValue(asset, true);
  }

  @Override
  public void retain() {
    super.retain();
    for (Asset asset : dependencies) {
      asset.retain();
    }
  }

  @Override
  public void release() {
    super.release();
    for (Asset asset : dependencies) {
      asset.release();
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    for (Asset asset : dependencies) {
      asset.release();
    }
  }
}
