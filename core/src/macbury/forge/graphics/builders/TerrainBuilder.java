package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;

import java.util.Comparator;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder {

  private final VoxelsAssembler solidVoxelAssembler;
  private final VoxelsAssembler transparentVoxelAssembler;

  private static final String TAG = "TerrainBuilder";

  private final ChunkMap map;
  public final TerrainCursor cursor;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  private Array<TerrainPart> terrainParts;
  private Array<Block.Side> facesToBuild = new Array<Block.Side>();

  private Vector3i nextTileToCheck = new Vector3i();
  private final VoxelDef voxelDef;
  private Array<TerrainPart> tempTerrainParts  = new Array<TerrainPart>();
  private static Voxel mask[] = new Voxel[ChunkMap.CHUNK_SIZE * ChunkMap.CHUNK_SIZE];

  private static Pool<TerrainPart> terrainPartPool = new Pool<TerrainPart>() {
    @Override
    protected TerrainPart newObject() {
      TerrainPart part = new TerrainPart();
      part.reset();
      return part;
    }
  };


  public TerrainBuilder(ChunkMap voxelMap) {
    super();
    this.terrainParts              = new Array<TerrainPart>();
    this.map                       = voxelMap;
    this.cursor                    = new TerrainCursor();

    this.voxelDef                  = new VoxelDef(map);
    this.solidVoxelAssembler       = new VoxelsAssembler();
    this.transparentVoxelAssembler = new VoxelsAssembler();
  }

  private boolean isVoxelTransparent(Voxel voxel) {
    return voxel != null && voxel.getBlock().transparent;
  }

  private boolean isVoxelBlockHaveOcculsion(Voxel voxel) {
    return voxel != null && voxel.getBlock().blockShape.occulsion;
  }

  private boolean doVoxelsDontHaveTheSameShape(Voxel voxelA, Voxel voxelB) {
    return voxelB != null && (voxelB.getBlock().blockShape != voxelA.getBlock().blockShape);
  }



  public void begin() {
    solidVoxelAssembler.begin();
    transparentVoxelAssembler.begin();
    facesToBuild.clear();
    facesToBuild.add(Block.Side.top);
    /*facesToBuild.add(Block.Side.bottom);
    facesToBuild.add(Block.Side.left);
    facesToBuild.add(Block.Side.right);
    facesToBuild.add(Block.Side.front);
    facesToBuild.add(Block.Side.back);*/
  }

  public void end() {
    solidVoxelAssembler.end();
    transparentVoxelAssembler.end();
  }

  public boolean next() {
    return facesToBuild.size > 0;
  }

  public void buildFaceForChunk(Chunk chunk) {
    Block.Side side = facesToBuild.pop();
    if (side == Block.Side.all || side == Block.Side.side) {
      throw new GdxRuntimeException("I cannot assemble chunk face for: " + side.toString());
    }
    resetMask();
    createMask(side);
    if (terrainParts.size > 0) {
      createTrianglesFor(side, terrainParts, solidVoxelAssembler, transparentVoxelAssembler);

      buildFaceForChunkWithAssembler(chunk, solidVoxelAssembler, false, side);
      buildFaceForChunkWithAssembler(chunk, transparentVoxelAssembler, true, side);

      terrainPartPool.freeAll(terrainParts);
      terrainParts.clear();
    }
  }

  private boolean isAir(Voxel voxel) {
    return voxel == null || voxel.isAir();
  }

  private boolean isVoxelsTheSame(Voxel a, Voxel b) {
    return a != null && b != null && a.equals(b) && a.isScalable();
  }

  private void createMask(Block.Side face) {
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      int n = 0;
      for (int z = cursor.start.z; z < cursor.end.z; z++) {
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          if (map.isNotAir(x, y, z)) {
            nextTileToCheck.set(x, y, z);
            nextTileToCheck.add(face.direction);

            Voxel currentVoxel = map.getVoxelForPosition(x, y, z);
            Voxel nextVoxel    = map.getVoxelForPosition(nextTileToCheck);
            //Gdx.app.log(TAG, "n="+n + " for X:" + x + " Y: " + y + " Z: " + z);

            if (isVoxelTransparent(currentVoxel)) {
              if (!isVoxelTransparent(nextVoxel) || nextVoxel == null || nextVoxel.blockId != currentVoxel.blockId || !isVoxelBlockHaveOcculsion(currentVoxel)) {
                mask[n++] = currentVoxel;
              } else {
                mask[n++] = null;
              }
            } else if (isVoxelTransparent(nextVoxel)) {
              mask[n++] = currentVoxel;
            } else if (map.isEmptyNotOutOfBounds(nextTileToCheck) || doVoxelsDontHaveTheSameShape(currentVoxel, nextVoxel) || !isVoxelBlockHaveOcculsion(currentVoxel)) {
              mask[n++] = currentVoxel;
            } else {
              mask[n++] = null;
            }
          } else {
            mask[n++] = null;
          }
        }
      }

      n = 0;

      for(int j = 0; j < ChunkMap.CHUNK_SIZE; j++) {
        for (int i = 0; i < ChunkMap.CHUNK_SIZE; ) {
          if (mask[n] == null || mask[n].isAir()) {
            i++;
            n++;
          } else {
            int w, h = 0;
            for(w = 1; i + w < ChunkMap.CHUNK_SIZE && !isAir(mask[n + w]) && isVoxelsTheSame(mask[n + w], mask[n]); w++) {}

            boolean done = false;

            for(h = 1; j + h < ChunkMap.CHUNK_SIZE; h++) {

              for(int k = 0; k < w; k++) {
                if(isAir(mask[n + k + h * ChunkMap.CHUNK_SIZE]) || !isVoxelsTheSame(mask[n + k + h * ChunkMap.CHUNK_SIZE], mask[n])) { done = true; break; }
              }

              if(done) { break; }
            }

            //Gdx.app.log(TAG, "New quad: " + mask[n].blockId + " size=" + w+"x"+h + " at " + "X: " + i + " Y: " + j);

            TerrainPart currentPart     = terrainPartPool.obtain();
            currentPart.face            = face;
            currentPart.block           = mask[n].getBlock();
            currentPart.voxel           = mask[n];
            currentPart.voxelPosition.set(i, y, j).add(cursor.start);
            currentPart.voxelSize.set(w, 1, h);
            currentPart.uvTiling.set(w,h);
            terrainParts.add(currentPart);

            Gdx.app.log(TAG, "Quad: " + currentPart.toString());


            for(int l = 0; l < h; ++l) {
              for(int k = 0; k < w; ++k) { mask[n + k + l * ChunkMap.CHUNK_SIZE] = null; }
            }

            i += w;
            n += w;
          }
        }
      }
    }
  }

  private void resetMask() {
    for (int i = 0; i < mask.length; i++) {
      mask[i] = null;
    }
  }

  private void createTrianglesFor(Block.Side side, Array<TerrainPart> terrainParts, VoxelsAssembler solidVoxelAssembler, VoxelsAssembler transparentVoxelAssembler) {
    for (TerrainPart part : terrainParts) {
      voxelDef.block = part.block;
      tempB.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.voxelSize);
      tempA.set(part.voxelPosition.x, part.voxelPosition.y, part.voxelPosition.z).scl(map.voxelSize).sub(tempB);

      voxelDef.position.set(tempA);
      voxelDef.voxelPosition.set(part.voxelPosition.x, part.voxelPosition.y, part.voxelPosition.z);
      voxelDef.size.set(map.voxelSize);
      voxelDef.voxel         = part.voxel;
      voxelDef.center.set(map.voxelSize.x / 2f, map.voxelSize.y / 2f, map.voxelSize.z / 2f);
      if (part.block.transparent) {
        transparentVoxelAssembler.face(voxelDef, side,part);
      } else {
        solidVoxelAssembler.face(voxelDef, side, part);
      }
    }
  }

  private void buildFaceForChunkWithAssembler(Chunk chunk, VoxelsAssembler assembler, boolean haveTransparency, Block.Side face) {
    if (!assembler.isEmpty()) {
      VoxelFaceRenderable renderable   = new VoxelFaceRenderable();
      renderable.primitiveType         = GL30.GL_TRIANGLES;

      if (ForgE.config.generateWireframe)
        renderable.wireframe           = assembler.wireframe();
      renderable.triangleCount         = assembler.getTriangleCount();
      renderable.mesh                  = assembler.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.TextureCord, MeshVertexInfo.AttributeType.Material, MeshVertexInfo.AttributeType.TextureFullCords);

      renderable.worldTransform.idt();
      renderable.haveTransparency = haveTransparency;
      renderable.worldTransform.translate(chunk.worldPosition);
      renderable.direction.set(face.direction.x, face.direction.y, face.direction.z);
      renderable.mesh.calculateBoundingBox(renderable.boundingBox);
      renderable.boundingBox.min.add(chunk.worldPosition);
      renderable.boundingBox.max.add(chunk.worldPosition);
      //Gdx.app.log(TAG, "Bounding box for renderable: " + cursor.size.toString());
      chunk.addFace(renderable);
    }
  }

  public void dispose() {
    solidVoxelAssembler.dispose();
    transparentVoxelAssembler.dispose();
    terrainParts.clear();
    terrainPartPool.freeAll(terrainParts);
    terrainPartPool.clear();
    resetMask();
  }
}
