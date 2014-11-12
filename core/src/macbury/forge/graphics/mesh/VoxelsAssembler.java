package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.builders.VoxelDef;

/**
 * Created by macbury on 16.10.14.
 */
public class VoxelsAssembler extends MeshAssembler {
  public void top(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.top);
    MeshVertexInfo vert1                = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert1.normalUp().uv2(sideRegion).ao(voxelDef.ao);

    if (voxelDef.shadeTopLeftCorner) {
      vert1.applyAoShade();
    }

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y  + voxelDef.size.y , voxelDef.position.z + voxelDef.size.z); // bottom left
    vert2.normalUp().uv(sideRegion).ao(voxelDef.ao);
    if (voxelDef.shadeBottomLeftCorner) {
      vert2.applyAoShade();
    }

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y  + voxelDef.size.y, voxelDef.position.z); //top right
    vert3.normalUp().u2v2(sideRegion).ao(voxelDef.ao);

    if (voxelDef.shadeTopRightCorner) {
      vert3.applyAoShade();
    }

    triangle(vert1, vert2, vert3);

    vert1 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y  + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //bottom right
    vert1.normalUp().u2v(sideRegion).ao(voxelDef.ao);

    if (voxelDef.shadeBottomRightCorner) {
      vert1.applyAoShade();
    }

    triangle(vert3, vert2, vert1);
  }

  public void bottom(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.bottom);
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //top left
    vert1.normalBottom().uv2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y , voxelDef.position.z + voxelDef.size.z); // bottom left
    vert2.normalBottom().uv(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); //top right
    vert3.normalBottom().u2v2(sideRegion).ao(voxelDef.ao);

    triangle(vert3, vert2, vert1);

    vert1 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); //bottom right
    vert1.normalBottom().u2v(sideRegion).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);
  }

  public void front(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.front);
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); //bottom left
    vert1.normalFront().uv2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalFront().u2v2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalFront().u2v(sideRegion).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top left
    vert4.normalFront().uv(sideRegion).ao(voxelDef.ao);

    triangle(vert4, vert1, vert3);
  }

  public void back(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.back);
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalBack().uv2(sideRegion).ao(voxelDef.ao);

    if (voxelDef.shadeBottomLeftCorner) {
      vert1.applyAoShade();
    }

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); // bottom right
    vert2.normalBack().u2v2(sideRegion).ao(voxelDef.ao);

    if (voxelDef.shadeBottomRightCorner) {
      vert2.applyAoShade();
    }

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top right
    vert3.normalBack().u2v(sideRegion).ao(voxelDef.ao);

    if (voxelDef.shadeTopRightCorner) {
      vert3.applyAoShade();
    }

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalBack().uv(sideRegion).ao(voxelDef.ao);
    if (voxelDef.shadeTopLeftCorner) {
      vert3.applyAoShade();
    }

    triangle(vert3, vert1, vert4);
  }

  public void left(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.left);

    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalLeft().uv2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalLeft().u2v2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalLeft().u2v(sideRegion).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalLeft().uv(sideRegion).ao(voxelDef.ao);

    triangle(vert4, vert1, vert3);
  }

  public void right(VoxelDef voxelDef) {
    TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(Block.Side.right);

    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalRight().uv2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalRight().u2v2(sideRegion).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalRight().u2v(sideRegion).ao(voxelDef.ao);

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalRight().uv(sideRegion).ao(voxelDef.ao);

    triangle(vert3, vert1, vert4);
  }
}
