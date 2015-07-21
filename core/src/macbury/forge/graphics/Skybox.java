package macbury.forge.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.assets.assets.CubemapAsset;
import macbury.forge.graphics.batch.renderable.SkyboxRenderable;

/**
 * Created by macbury on 18.05.15.
 * provides renderables for awesome skybox!
 */
public class Skybox implements Disposable, RenderableProvider {
  private SkyboxRenderable renderable;
  private final static float SIZE = 50f;
  private CubemapAsset skyboxAsset;
  private Cubemap skyboxCubemap;
  public float rotationSpeed = 0.4f;
  public final Vector3 rotationDirection = new Vector3(Vector3.Y);
  public float rotation;

  public Skybox(CubemapAsset asset) {
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

  private Mesh buildMesh() {
    Mesh mesh = new Mesh(true, 8, 14, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

    float[] cubeVerts = { -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
        SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, };

    short[] indices = { 0, 1, 2, 3, 7, 1, 5, 4, 7, 6, 2, 4, 0, 1 };

    mesh.setVertices(cubeVerts);
    mesh.setIndices(indices);
    return mesh;
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    if (skyboxAsset != null) {
      renderables.add(getRenderable());
    }
  }

  private Renderable getRenderable() {
    if (renderable == null) {
      this.renderable           = new SkyboxRenderable();
      this.renderable.mesh      = buildMesh();

      renderable.primitiveType  = GL30.GL_TRIANGLE_STRIP;
      renderable.worldTransform.idt();
    }

    return renderable;
  }

  @Override
  public void dispose() {
    renderable.mesh.dispose();
    renderable = null;
    setSkyboxAsset(null);
  }


}
