package macbury.forge.graphics.renderable;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.Wireframe;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.mesh.MeshFactory;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.shaders.attributes.SolidTerrainAttribute;

/**
 * Created by macbury on 05.08.15.
 */
public class VoxelChunkRenderableFactory implements Disposable {
  public Material material;
  public int primitiveType;
  public Wireframe wireframe;
  public int triangleCount;
  public MeshVertexInfo.AttributeType[] attributes;
  public MeshFactory meshFactory;

  public VoxelChunkRenderableFactory() {

  }

  public VoxelChunkRenderable get() {
    VoxelChunkRenderable renderable   = new VoxelChunkRenderable();
    renderable.primitiveType          = primitiveType;
    renderable.wireframe              = wireframe;
    renderable.triangleCount          = triangleCount;
    renderable.meshFactory            = meshFactory;
    renderable.material               = material;
    renderable.worldTransform.idt();

    return renderable;
  }

  @Override
  public void dispose() {
    meshFactory.dispose();
    attributes  = null;
    material    = null;
    wireframe   = null;
    meshFactory = null;
  }
}
