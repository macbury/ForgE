package macbury.forge.graphics.batch.renderable;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.mesh.MeshAssembler;

/**
 * Created by macbury on 23.10.14.
 */
public class VoxelChunkRenderable extends BaseRenderable implements Disposable {
  public Vector3 direction = new Vector3();
  private Chunk parent;
  public BoundingBox boundingBox = new BoundingBox();
  public MeshAssembler.MeshFactory meshFactory;

  @Override
  public void dispose() {
    if (meshFactory != null) {
      meshFactory.dispose();
    }
    if (mesh != null) {
      mesh.dispose();
    }
    parent = null;
    mesh = null;
    meshFactory = null;
  }

  public void setParent(Chunk parent) {
    this.parent = parent;
  }

  public Chunk getParent() {
    return parent;
  }


  public void buildMeshFromFactory() {
    if (meshFactory != null) {
      this.mesh = meshFactory.get();

      this.worldTransform.idt().translate(parent.worldPosition);
      this.mesh.calculateBoundingBox(this.boundingBox);
      boundingBox.min.add(parent.worldPosition);
      boundingBox.max.add(parent.worldPosition);
      meshFactory.dispose();
      meshFactory = null;
    }
  }
}
