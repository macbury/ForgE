package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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

  private static Pool<TerrainPart> terrainPartPool = new Pool<TerrainPart>() {
    @Override
    protected TerrainPart newObject() {
      TerrainPart part = new TerrainPart();
      part.reset();
      return part;
    }
  };

  private static Comparator<TerrainPart> terrainPartComparator = new Comparator<TerrainPart>() {
    private float dst;
    @Override
    public int compare(TerrainPart o1, TerrainPart o2) {
      dst = o1.distanceTo(o2);
      if (dst == 0) {
        return 0;
      } else if (dst > 0) {
        return 1;
      } else {
        return -1;
      }
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

  private void joinVeriticalyParts(Array<TerrainPart> out) {
    out.sort(terrainPartComparator);
  }

  private void optimizeFace(Block.Side face, Array<TerrainPart> out) {
    TerrainPart currentPart = null;
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int z = cursor.start.z; z < cursor.end.z; z++) {
        currentPart = null;
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          voxelDef.reset();
          nextTileToCheck.set(x,y,z);
          nextTileToCheck.add(face.direction);

          Voxel currentVoxel     = map.getVoxelForPosition(x, y, z);
          Voxel nextVoxel        = map.getVoxelForPosition(nextTileToCheck);

          if (map.isNotAir(x,y,z) && (map.isEmptyNotOutOfBounds(nextTileToCheck) || isVoxelTransparent(nextVoxel))) {
            currentPart               = terrainPartPool.obtain();
            currentPart.block         = currentVoxel.getBlock();
            currentPart.voxel         = currentVoxel;
            currentPart.voxelPosition.set(x, y, z);
            currentPart.voxelSize.setZero();
            if (out.size > 0) {
              TerrainPart lastPart = out.get(out.size-1);
              if (lastPart.similar(currentPart)) {
                lastPart.join(currentPart);
              } else {
                out.add(currentPart);
              }
            } else {
              out.add(currentPart);
            }
          } else {
            currentPart = null;
          }


          /*
          * check if last terrain part is similar to this part(the same block, only diffrence of one in position)
          * if true then remove this part and extend last part one time in direction of this part
           */

          //if (currentVoxel != null && currentVoxel.getBlock().isSolid()) {
          //  addTrianglesForFace(currentVoxel, face, solidVoxelAssembler);
          //}
          //if (currentVoxel != null)
          //addTrianglesForFace(currentVoxel, face, solidVoxelAssembler);
        }
      }
    }
  }

  private void buildFace(Block.Side face) {
    boolean updateCursorSize = false;
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int x = cursor.start.x; x < cursor.end.x; x++) {
        for (int z = cursor.start.z; z < cursor.end.z; z++) {
          //
          if (map.isNotAir(x, y, z)) {
            voxelDef.reset();

            Voxel currentVoxel     = map.getVoxelForPosition(x, y, z);

            nextTileToCheck.set(x,y,z);
            nextTileToCheck.add(face.direction);

            updateCursorSize = false;

            tempB.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.voxelSize);
            tempA.set(x,y,z).scl(map.voxelSize).sub(tempB);


            voxelDef.position.set(tempA);
            voxelDef.voxelPosition.set(x,y,z);
            voxelDef.size.set(map.voxelSize);

            //Gdx.app.log(TAG, "pos: "+ tempA.toString());

            voxelDef.center.set(map.voxelSize.x / 2f, map.voxelSize.y / 2f, map.voxelSize.z / 2f);


            Voxel nextVoxel        = map.getVoxelForPosition(nextTileToCheck);

            voxelDef.voxel         = currentVoxel;

            if (isVoxelTransparent(currentVoxel)) {
              if (!isVoxelTransparent(nextVoxel) || nextVoxel == null || nextVoxel.blockId != currentVoxel.blockId || !isVoxelBlockHaveOcculsion(currentVoxel)) {
                addTrianglesForFace(currentVoxel, face, transparentVoxelAssembler);
              }

              updateCursorSize = true;
            } else if (isVoxelTransparent(nextVoxel))  {
              addTrianglesForFace(currentVoxel, face, solidVoxelAssembler);
              updateCursorSize = true;
              addTrianglesForFace(currentVoxel, face, solidVoxelAssembler);
            } else if (map.isEmptyNotOutOfBounds(nextTileToCheck) || doVoxelsDontHaveTheSameShape(currentVoxel, nextVoxel) || !isVoxelBlockHaveOcculsion(currentVoxel)) {
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
    //assembler.face(voxelDef, side, part);
  }

  public void begin() {
    solidVoxelAssembler.begin();
    transparentVoxelAssembler.begin();
    facesToBuild.clear();
    //facesToBuild.addAll(Block.Side.values());
    facesToBuild.add(Block.Side.top);
    facesToBuild.add(Block.Side.bottom);
    facesToBuild.add(Block.Side.left);
    facesToBuild.add(Block.Side.right);
    facesToBuild.add(Block.Side.front);
    facesToBuild.add(Block.Side.back);
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
      optimizeFace(side, terrainParts);
      joinVeriticalyParts(terrainParts);
      createTrianglesFor(side, terrainParts, solidVoxelAssembler, transparentVoxelAssembler);
      buildFaceForChunkWithAssembler(chunk, solidVoxelAssembler, false, side);
      buildFaceForChunkWithAssembler(chunk, transparentVoxelAssembler, true, side);

      terrainPartPool.freeAll(terrainParts);
      terrainParts.clear();
    }

  }


  private void createTrianglesFor(Block.Side side, Array<TerrainPart> terrainParts, VoxelsAssembler solidVoxelAssembler, VoxelsAssembler transparentVoxelAssembler) {
    for (TerrainPart part : terrainParts) {
      part.voxelSize.add(1,1,1);
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
      renderable.mesh                  = assembler.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.TextureCord, MeshVertexInfo.AttributeType.Material, MeshVertexInfo.AttributeType.TextureTiling);

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
  }
}
