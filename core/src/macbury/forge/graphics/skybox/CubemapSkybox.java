package macbury.forge.graphics.skybox;

import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.assets.assets.CubemapAsset;
import macbury.forge.graphics.batch.renderable.CubemapSkyboxRenderable;

/**
 * Created by macbury on 04.08.15.
 */
public class CubemapSkybox extends Skybox {
  private CubemapSkyboxRenderable renderable;
  private CubemapAsset skyboxAsset;
  private Cubemap skyboxCubemap;
  public float rotationSpeed = 0.4f;
  public final Vector3 rotationDirection = new Vector3(Vector3.Y);
  public float rotation;

  public CubemapSkybox(CubemapAsset asset) {
    setSkyboxAsset(asset);
  }

  public CubemapAsset getSkyboxAsset() {
    return skyboxAsset;
  }

  public Cubemap getSkyboxCubemap() {
    if (skyboxCubemap == null) {
      skyboxCubemap = skyboxAsset.get();
    }
    return skyboxCubemap;
  }
  @Override
  public void update(float delta) {
    rotation += delta * rotationSpeed;
  }

  public void setSkyboxAsset(CubemapAsset newSkyboxAsset) {
    if (skyboxAsset != null) {
      if (skyboxCubemap != null) {
        skyboxCubemap = null;
      }
      skyboxAsset.release();
      skyboxAsset = null;
    }
    this.skyboxAsset = newSkyboxAsset;
    if (skyboxAsset != null)
      skyboxAsset.retain();
  }


  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    if (skyboxAsset != null) {
      renderables.add(getRenderable());
    }
  }


  private Renderable getRenderable() {
    if (renderable == null) {
      this.renderable           = new CubemapSkyboxRenderable();
      this.renderable.mesh      = buildMesh();

      renderable.primitiveType  = GL30.GL_TRIANGLE_STRIP;
      renderable.worldTransform.idt();
    }

    return renderable;
  }

  @Override
  public void dispose() {
    if (renderable != null)
      renderable.mesh.dispose();
    renderable = null;
    setSkyboxAsset(null);
  }
}
