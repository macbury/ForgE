package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlockShapePart;
import macbury.forge.blocks.BlockShapeTriangle;
import macbury.forge.graphics.builders.VoxelDef;
import macbury.forge.terrain.greedy.AbstractGreedyAlgorithm;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 16.10.14.
 */
public class VoxelsAssembler extends MeshAssembler {
  private static Matrix4 transformMat = new Matrix4();
  private Vector3 tempVec      = new Vector3();
  private Vector2 tempVec2     = new Vector2();

  public static void vertexTranslationFromShape(Vector3i origin, Vector3i voxelScale, Vector3 voxelCenter, Block.Side alginTo, Block.Rotation rotation, Vector3 point, Vector3 out) {
    // Part first calculate position of vertex and rotate in the algiment path
    transformMat.idt();

    transformMat.translate(origin.x, origin.y, origin.z);
    transformMat.scl(voxelScale.x, voxelScale.y, voxelScale.z);
    transformMat.translate(voxelCenter);

    if (alginTo != null) {
      switch (rotation) {
        case horizontal:
          transformMat.rotate(alginTo.rotationHorizontal);
          break;

        case alignToSurface:
          transformMat.rotate(alginTo.rotationAllSides);
          break;
      }
    }

    transformMat.translate(point);
    transformMat.getTranslation(out);
  }

  private MeshVertexInfo vertex(VoxelDef voxelDef, BlockShapePart part, int index, TextureAtlas.AtlasRegion sideRegion, AbstractGreedyAlgorithm.GreedyQuad terrainPart) {
    MeshVertexInfo vert = this.vertex().ao(voxelDef.ao).transparent(voxelDef.block.transparent);

    vertexTranslationFromShape(
        voxelDef.position,
        terrainPart.voxelSize,
        voxelDef.center,
        voxelDef.voxel.alginTo,
        voxelDef.block.rotation,
        part.verticies.get(index),
        vert.position
    );
/*
    // Part first calculate position of vertex and rotate in the algiment path
    transformMat.idt();

    transformMat.translate(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z);
    transformMat.scl(terrainPart.voxelSize.x, terrainPart.voxelSize.y, terrainPart.voxelSize.z);
    transformMat.translate(voxelDef.center);

    if (voxelDef.voxel.alginTo != null) {
      switch (voxelDef.block.rotation) {
        case horizontal:
          transformMat.rotate(voxelDef.voxel.alginTo.rotationHorizontal);
          break;

        case alignToSurface:
          transformMat.rotate(voxelDef.voxel.alginTo.rotationAllSides);
          break;
      }
    }

    transformMat.translate(part.verticies.get(index));
    transformMat.getTranslation(vert.position);
*/
    // Recalculate aligment normals too :P

    transformMat.idt();
    if (voxelDef.voxel.alginTo != null) {
      switch(voxelDef.block.rotation) {
        case horizontal:
          transformMat.rotate(voxelDef.voxel.alginTo.rotationHorizontal);
        break;

        case alignToSurface:
          transformMat.rotate(voxelDef.voxel.alginTo.rotationAllSides);
        break;
      }
    }
    
    transformMat.translate(part.normals.get(index));
    transformMat.getTranslation(vert.normal);

    Vector2 uv = part.uvs.get(index);
    terrainPart.getUVScaling(tempVec2);
    tempVec2.scl(uv);

    vert.uv.set(tempVec2);
    vert.textureFullCords(sideRegion.getU(), sideRegion.getV(), sideRegion.getU2(), sideRegion.getV2());

    if (part.waviness != null) {
      vert.material.setWaviness(part.waviness[index]);
    }

    return vert;
  }

  public void face(VoxelDef voxelDef, Block.Side side, AbstractGreedyAlgorithm.GreedyQuad part) {
    BlockShapePart blockShapePart       = voxelDef.block.blockShape.get(side);

    if (blockShapePart != null) {
      TextureAtlas.AtlasRegion sideRegion = null;
      try {
        sideRegion = voxelDef.block.getRegionForSide(side);
      } catch (Block.NoUvForBlockSide error) {
        sideRegion = ForgE.blocks.getDevTextureRegion();
      }


      for(BlockShapeTriangle triangle : blockShapePart.triangles) {
        MeshVertexInfo vert1          = vertex(voxelDef, blockShapePart, triangle.index1, sideRegion, part);
        MeshVertexInfo vert2          = vertex(voxelDef, blockShapePart, triangle.index2, sideRegion, part);
        MeshVertexInfo vert3          = vertex(voxelDef, blockShapePart, triangle.index3, sideRegion, part);

        triangle(vert1, vert2, vert3);
      }
    }
  }
}
