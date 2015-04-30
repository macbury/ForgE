package macbury.forge.assets.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.UBJsonReader;

/**
 * Created by macbury on 30.04.15.
 */
public class ModelAsset extends Asset<Model> {
  private static ModelLoader g3djLoader = new G3dModelLoader(new UBJsonReader());
  private btCollisionShape collisionShape;

  @Override
  protected Model loadObject(FileHandle file) {
    return g3djLoader.loadModel(file);
  }

  public ModelInstance buildModelInstance() {
    return new ModelInstance(get());
  }

  public btCollisionShape getCollisionShape() {
    if (collisionShape == null) {
      collisionShape = Bullet.obtainStaticNodeShape(get().nodes);
    }

    return collisionShape;
  }
}
