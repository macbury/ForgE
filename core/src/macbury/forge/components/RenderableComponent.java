package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.UBJsonReader;
import macbury.forge.ForgE;
import macbury.forge.assets.assets.ModelAsset;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

/**
 * Created by macbury on 19.10.14.
 */
public class RenderableComponent extends BaseComponent implements Pool.Poolable {
  private static Vector3 tempVec = new Vector3();
  private ModelInstance instance;
  private ModelAsset    asset;
  public String        path;

  public RenderableComponent() {
  }

  public ModelAsset getAsset() {
    if (asset == null) {
      asset = ForgE.assets.getModel(path);
      path  = null;
    }
    return asset;
  }

  public void addToBatch(VoxelBatch batch, Matrix4 worldTransform) {
    if (instance == null) {
      instance = getAsset().buildModelInstance();
    }

    instance.transform.idt();
    worldTransform.getTranslation(tempVec);
    instance.transform.setTranslation(tempVec);
    batch.add(instance);
  }


  @Override
  public void reset() {
    instance = null;
    asset    = null;
    path     = null;
  }

  @Override
  public void set(BaseComponent otherComponent) {
    RenderableComponent renderableComponent = (RenderableComponent)otherComponent;
    this.asset = renderableComponent.asset;
    this.path  = renderableComponent.path;
  }

}
