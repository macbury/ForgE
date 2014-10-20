package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.GL30;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.graphics.VoxelMap;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder extends VoxelsAssembler {
  private final VoxelMap map;


  public TerrainBuilder(VoxelMap voxelMap) {
    super();
    this.map       = voxelMap;
  }

  public TerrainChunkRenderable getRenderable() {
    TerrainChunkRenderable renderable = new TerrainChunkRenderable();
    renderable.primitiveType         = GL30.GL_TRIANGLES;
    renderable.mesh                  = mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Color);
    renderable.shader                = ForgE.shaders.get("mesh_test");
    if (ForgE.config.generateWireframe)
      renderable.wireframe           = wireframe();
    return renderable;
  }
}
