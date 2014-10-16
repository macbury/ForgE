package macbury.forge.graphics.mesh;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 30.08.14.
 */
public class MeshTriangle implements Pool.Poolable {
  public MeshVertexInfo vert1;
  public MeshVertexInfo vert2;
  public MeshVertexInfo vert3;

  public MeshTriangle() {
    this.reset();
  }

  @Override
  public void reset() {
    this.set(null,null,null);
  }

  public void set(MeshVertexInfo vertex1, MeshVertexInfo vertex2, MeshVertexInfo vertex3) {
    this.vert1 = vertex1;
    this.vert2 = vertex2;
    this.vert3 = vertex3;
  }
}
