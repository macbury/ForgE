package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlockShapePart;
import macbury.forge.blocks.BlockShapeTriangle;
import macbury.forge.graphics.builders.VoxelDef;

/**
 * Created by macbury on 16.10.14.
 */
public class VoxelsAssembler extends MeshAssembler {
  private Matrix4 transformMat = new Matrix4();

  private MeshVertexInfo vertex(VoxelDef voxelDef, BlockShapePart part, int index, TextureAtlas.AtlasRegion sideRegion) {
    MeshVertexInfo vert = this.vertex().normal(part.normals.get(index)).ao(voxelDef.ao).transparent(voxelDef.block.transparent);
    transformMat.idt().translate(part.verticies.get(index)).translate(voxelDef.center).translate(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z).getTranslation(vert.position);

    Vector2 uv = part.uvs.get(index);

    if (uv.x == 0.0f) {
      vert.uv.x = sideRegion.getU();
    } else {
      vert.uv.x = sideRegion.getU2();
    }

    if (uv.y == 0.0f) {
      vert.uv.y = sideRegion.getV();
    } else {
      vert.uv.y = sideRegion.getV2();
    }

    return vert;
  }

  public void face(VoxelDef voxelDef, Block.Side side) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(side);
    BlockShapePart blockShapePart       = voxelDef.block.blockShape.get(side);

    for(BlockShapeTriangle triangle : blockShapePart.triangles) {
      MeshVertexInfo vert1          = vertex(voxelDef, blockShapePart, triangle.index1, sideRegion);
      MeshVertexInfo vert2          = vertex(voxelDef, blockShapePart, triangle.index2, sideRegion);
      MeshVertexInfo vert3          = vertex(voxelDef, blockShapePart, triangle.index3, sideRegion);

      triangle(vert1, vert2, vert3);
    }
  }

  public void top(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.top);
  }

  public void bottom(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.bottom);
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //top left
    vert1.normalBottom().uv2(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y , voxelDef.position.z + voxelDef.size.z); // bottom left
    vert2.normalBottom().uv(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); //top right
    vert3.normalBottom().u2v2(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    triangle(vert3, vert2, vert1);

    vert1 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); //bottom right
    vert1.normalBottom().u2v(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    triangle(vert1, vert2, vert3);
  }

  public void front(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.front);
  }

  public void back(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.back);
  }

  public void left(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.left);

    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalLeft().uv2(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalLeft().u2v2(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalLeft().u2v(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalLeft().uv(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    triangle(vert4, vert1, vert3);
  }

  public void right(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.right);

    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalRight().uv2(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalRight().u2v2(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalRight().u2v(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalRight().uv(sideRegion).ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    triangle(vert3, vert1, vert4);
  }
}
