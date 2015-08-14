package macbury.forge.graphics.skybox;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.env.LevelEnv;

/**
 * Created by macbury on 18.05.15.
 * provides renderables for awesome skybox!
 */
public abstract class Skybox implements Disposable, RenderableProvider {
  private final static float SIZE = 50f;
  public abstract void update(float delta, Camera camera);

  protected Mesh buildMesh() {
    Mesh mesh = new Mesh(true, 8, 14, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

    float[] cubeVerts = { -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
        SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, };

    short[] indices = { 0, 1, 2, 3, 7, 1, 5, 4, 7, 6, 2, 4, 0, 1 };

    mesh.setVertices(cubeVerts);
    mesh.setIndices(indices);
    return mesh;
  }

  public void render(VoxelBatch batch, LevelEnv env, Camera camera) {
    batch.add(this);
    batch.render(env);
  }
}
