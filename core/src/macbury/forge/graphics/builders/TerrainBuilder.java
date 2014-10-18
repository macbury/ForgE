package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.GL30;
import macbury.forge.ForgE;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.graphics.batch.renderable.TerrainTileRenderable;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder extends VoxelsAssembler {

  public TerrainTileRenderable getRenderable() {
    TerrainTileRenderable renderable = new TerrainTileRenderable();
    renderable.primitiveType         = GL30.GL_TRIANGLES;
    if (ForgE.config.generateWireframe) {
      renderable.wireframe           = wireframe();
    }
    renderable.mesh                  = mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Color);
    renderable.shader                = ForgE.shaders.get("mesh_test");
    return renderable;
  }
}
