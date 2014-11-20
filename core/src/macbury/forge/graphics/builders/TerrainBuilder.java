package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.procedular.PerlinNoise;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder {
  private static final double SHADE_AO_AMPLUTUDE = 10;
  private static final double SHADE_AO_FREQUENCY = 0.1;
  private static final float SHADE_AO_FACTOR = 0.85f;
  private final VoxelsAssembler solidVoxelAssembler;
  private final VoxelsAssembler transparentVoxelAssembler;
  private float[][][] aoArray;

  private static final String TAG = "TerrainBuilder";

  private final VoxelMap map;
  public final TerrainCursor cursor;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  private Array<Block.Side> facesToBuild = new Array<Block.Side>();
  private final PerlinNoise perlinNoise;
  private Vector3i nextTileToCheck = new Vector3i();
  private Color tempColor          = new Color();
  private final VoxelDef voxelDef;
  public TerrainBuilder(VoxelMap voxelMap) {
    super();
    this.map         = voxelMap;
    this.cursor      = new TerrainCursor();
    this.perlinNoise = new PerlinNoise(System.currentTimeMillis());
    this.voxelDef    = new VoxelDef(map);
    this.solidVoxelAssembler       = new VoxelsAssembler();
    this.transparentVoxelAssembler = new VoxelsAssembler();
    generatePrettyShadeArray();

  }

  private void generatePrettyShadeArray() {
    this.aoArray = new float[map.getWidth()][map.getHeight()][map.getDepth()];
    for (int y = 0; y < map.getHeight(); y++) {
      for (int x = 0; x < map.getWidth(); x++) {
        for (int z = 0; z < map.getDepth(); z++) {
          aoArray[x][y][z] = (float)perlinNoise.simpleNoise(x,y,z, SHADE_AO_AMPLUTUDE, SHADE_AO_FREQUENCY) * SHADE_AO_FACTOR;
        }
      }
    }
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

  private void buildFace(Vector3i checkTileInDirection, Block.Side face) {
    boolean updateCursorSize = false;
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int x = cursor.start.x; x < cursor.end.x; x++) {
        for (int z = cursor.start.z; z < cursor.end.z; z++) {
          if (map.isNotAir(x, y, z)) {
            voxelDef.reset();
            updateCursorSize = false;

            tempB.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.voxelSize);
            tempA.set(x,y,z).scl(map.voxelSize).sub(tempB);

            nextTileToCheck.set(x,y,z).add(checkTileInDirection);
            voxelDef.position.set(tempA);
            voxelDef.voxelPosition.set(x,y,z);
            voxelDef.size.set(map.voxelSize);

            voxelDef.center.set(map.voxelSize.x / 2f, map.voxelSize.y / 2f, map.voxelSize.z / 2f);

            Voxel currentVoxel     = map.getVoxelForPosition(x, y, z);
            Voxel nextVoxel        = map.getVoxelForPosition(nextTileToCheck);

            Block.Side side        = currentVoxel.getBlock().rotation.faceToSide(currentVoxel.alginTo);

            voxelDef.voxel         = currentVoxel;

            if (isVoxelTransparent(currentVoxel)) {
              if (!isVoxelTransparent(nextVoxel) || nextVoxel == null || nextVoxel.blockId != currentVoxel.blockId || !isVoxelBlockHaveOcculsion(currentVoxel)) {
                addTrianglesForFace(currentVoxel, face, transparentVoxelAssembler);
              }

              updateCursorSize = true;
            } else if (isVoxelTransparent(nextVoxel))  {
              addTrianglesForFace(currentVoxel, face, solidVoxelAssembler);
              updateCursorSize = true;
            } else if (map.isEmptyNotOutOfBounds(nextTileToCheck) || doVoxelsDontHaveTheSameShape(currentVoxel, nextVoxel)) {
              addTrianglesForFace(currentVoxel, face, solidVoxelAssembler);
              updateCursorSize = true;
            }

            if (updateCursorSize) {
              cursor.size.x = Math.max(x, cursor.size.x);
              cursor.size.y = Math.max(y, cursor.size.y);
              cursor.size.z = Math.max(z, cursor.size.z);
            }
          }
        }
      }
    }
    tempA.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.voxelSize);
    cursor.size.scl(map.voxelSize).sub(tempA);
  }

  private void addTrianglesForFace(Voxel voxel, Block.Side side, VoxelsAssembler assembler) {
    Block block    = voxel.getBlock();
    voxelDef.block = block;
    voxelDef.calculateAoFor(aoArray[voxelDef.voxelPosition.x][voxelDef.voxelPosition.y][voxelDef.voxelPosition.z]);
    assembler.face(voxelDef, side);
  }

  public void facesForChunk(Chunk chunk) {
    facesForPart(chunk.start, chunk.end, chunk.size);
  }

  private void facesForPart(Vector3i start, Vector3i end, Vector3 outSize) {
    outSize.setZero();
    Gdx.app.log(TAG, "Building faces for: " + start.toString() + " - " + end.toString());
    for (int x = start.x; x < end.x; x++) {
      for (int y = start.y; y < end.y; y++) {
        for (int z = start.z; z < end.z; z++) {
          if (map.isNotAir(x, y, z)) {
            outSize.x = Math.max(x, outSize.x);
            outSize.y = Math.max(y, outSize.y);
            outSize.z = Math.max(z, outSize.z);

            tempA.set(x,y,z);
            Gdx.app.log(TAG, tempA.toString());
            //VoxelMaterial material = map.getMaterialForPosition(x, y, z);
            /*
            if (map.isEmpty(x,y+1,z)) {
              //top(tempA, ChunkMap.TERRAIN_TILE_SIZE, material, 0);
            }

            if (map.isEmpty(x,y-1,z)) {
              bottom(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x-1,y,z)) {
              left(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x+1,y,z)) {
              right(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x,y,z+1)) {
              front(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x,y,z-1)) {
              back(tempA, ChunkMap.TERRAIN_TILE_SIZE, material, 0);
            }*/
          }
        }
      }
    }
    outSize.sub(start.x, start.y, start.z).add(1,1,1);
  }

  public void facesForMap() {
    facesForPart(new Vector3i(0,0,0), new Vector3i(map.getWidth(), map.getHeight(), map.getDepth()), new Vector3());
  }

  public void begin() {
    solidVoxelAssembler.begin();
    transparentVoxelAssembler.begin();
    facesToBuild.clear();
    facesToBuild.addAll(Block.Side.values());
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
    if (side != Block.Side.all || side != Block.Side.side) {
      buildFace(side.direction, side);

      buildFaceForChunkWithAssembler(chunk, solidVoxelAssembler, false, side);
      buildFaceForChunkWithAssembler(chunk, transparentVoxelAssembler, true, side);
    }

  }

  private void buildFaceForChunkWithAssembler(Chunk chunk, VoxelsAssembler assembler, boolean haveTransparency, Block.Side face) {
    if (!assembler.isEmpty()) {

      VoxelFaceRenderable renderable   = new VoxelFaceRenderable();
      renderable.primitiveType         = GL30.GL_TRIANGLES;

      if (ForgE.config.generateWireframe)
        renderable.wireframe           = assembler.wireframe();
      renderable.triangleCount         = assembler.getTriangleCount();
      renderable.mesh                  = assembler.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.TextureCord, MeshVertexInfo.AttributeType.Material);

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
  }
}
