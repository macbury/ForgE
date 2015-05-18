package macbury.forge.graphics;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.graphics.batch.renderable.SkyboxRenderable;
import macbury.forge.level.LevelEnv;

/**
 * Created by macbury on 18.05.15.
 * provides renderables for awesome skybox!
 */
public class Skybox implements Disposable, RenderableProvider {
  private SkyboxRenderable renderable;
  private final LevelEnv env;
  private final static float SIZE = 50f;
  public Skybox(LevelEnv env) {
    this.renderable           = new SkyboxRenderable();
    this.env                  = env;
    this.renderable.mesh      = buildMesh();

    renderable.primitiveType  = GL30.GL_TRIANGLE_STRIP;
    renderable.worldTransform.idt();
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
    if (env.skyboxAsset != null) {
      renderables.add(renderable);
    }
  }

  @Override
  public void dispose() {
    renderable.mesh.dispose();
    renderable = null;
  }


}
