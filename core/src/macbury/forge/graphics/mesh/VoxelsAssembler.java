package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlockShapePart;
import macbury.forge.blocks.BlockShapeTriangle;
import macbury.forge.graphics.builders.VoxelDef;

/**
 * Created by macbury on 16.10.14.
 */
public class VoxelsAssembler extends MeshAssembler {
  private Matrix4 transformMat = new Matrix4();
  private Vector3 tempVec      = new Vector3();
  private MeshVertexInfo vertex(VoxelDef voxelDef, BlockShapePart part, int index, TextureAtlas.AtlasRegion sideRegion) {
    MeshVertexInfo vert = this.vertex().ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    // Part first calculate position of vertex and rotate in the algiment path
    transformMat.idt();
    transformMat.translate(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z);
    transformMat.translate(voxelDef.center);
    switch(voxelDef.block.rotation) {
      case horizontal:
        transformMat.rotate(voxelDef.voxel.alginTo.rotationHorizontal);
      break;

      case alignToSurface:
        transformMat.rotate(voxelDef.voxel.alginTo.rotationAllSides);
      break;
    }

    transformMat.translate(part.verticies.get(index));
    transformMat.getTranslation(vert.position);

    // Recalculate aligment normals too :P

    transformMat.idt();
    switch(voxelDef.block.rotation) {
      case horizontal:
        transformMat.rotate(voxelDef.voxel.alginTo.rotationHorizontal);
      break;

      case alignToSurface:
        transformMat.rotate(voxelDef.voxel.alginTo.rotationAllSides);
      break;
    }
    transformMat.translate(part.normals.get(index));
    transformMat.getTranslation(vert.normal);


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
    BlockShapePart blockShapePart       = voxelDef.block.blockShape.get(side);

    if (blockShapePart != null) {
      TextureAtlas.AtlasRegion sideRegion = voxelDef.block.getRegionForSide(side);


      for(BlockShapeTriangle triangle : blockShapePart.triangles) {
        MeshVertexInfo vert1          = vertex(voxelDef, blockShapePart, triangle.index1, sideRegion);
        MeshVertexInfo vert2          = vertex(voxelDef, blockShapePart, triangle.index2, sideRegion);
        MeshVertexInfo vert3          = vertex(voxelDef, blockShapePart, triangle.index3, sideRegion);

        triangle(vert1, vert2, vert3);
      }
    }
  }

  public void top(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.top);
  }

  public void bottom(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.bottom);
  }

  public void front(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.front);
  }

  public void back(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.back);
  }

  public void left(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.left);
  }

  public void right(VoxelDef voxelDef) {
    face(voxelDef, Block.Side.right);
  }
}
