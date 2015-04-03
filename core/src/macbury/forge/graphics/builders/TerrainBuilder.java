package macbury.forge.graphics.builders;

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
  private Array<TerrainPart> tempTerrainParts = new Array<TerrainPart>();

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
      if (o1.comparedTo(o2)) {
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

  private void createTerrainPart(Block.Side face, int x, int y, int z, Voxel voxel, Array<TerrainPart> out) {
    TerrainPart currentPart    = terrainPartPool.obtain();
    currentPart.face           = face;
    currentPart.block         = voxel.getBlock();
    currentPart.voxel         = voxel;
    currentPart.voxelPosition.set(x, y, z);
    currentPart.voxelSize.setZero();
    if (out.size > 0) {
      TerrainPart lastPart = out.get(out.size-1);
      if (lastPart.isHorizontalSimilar(currentPart)) {
        lastPart.join(currentPart);
      } else {
        out.add(currentPart);
      }
    } else {
      out.add(currentPart);
    }
  }

  private void optimizeFace(Block.Side face, Array<TerrainPart> out) {
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int z = cursor.start.z; z < cursor.end.z; z++) {
        for (int x = cursor.start.x; x < cursor.end.x; x++) {
          if (map.isNotAir(x, y, z)) {
            voxelDef.reset();
            nextTileToCheck.set(x, y, z);
            nextTileToCheck.add(face.direction);

            Voxel currentVoxel = map.getVoxelForPosition(x, y, z);
            Voxel nextVoxel = map.getVoxelForPosition(nextTileToCheck);

            if (isVoxelTransparent(currentVoxel)) {
              if (!isVoxelTransparent(nextVoxel) || nextVoxel == null || nextVoxel.blockId != currentVoxel.blockId || !isVoxelBlockHaveOcculsion(currentVoxel)) {
                createTerrainPart(face, x, y, z, currentVoxel, out);
              }
            } else if (isVoxelTransparent(nextVoxel)) {
              createTerrainPart(face, x, y, z, currentVoxel, out);
            } else if (map.isEmptyNotOutOfBounds(nextTileToCheck) || doVoxelsDontHaveTheSameShape(currentVoxel, nextVoxel) || !isVoxelBlockHaveOcculsion(currentVoxel)) {
              createTerrainPart(face, x, y, z, currentVoxel, out);
            }
          }
        }
      }
    }
  }

  private void joinVeriticalyParts(Array<TerrainPart> out) {
    /*tempTerrainParts.addAll(out);
    tempTerrainParts.sort(terrainPartComparator);

    out.clear();
    while(tempTerrainParts.size > 1) {
      TerrainPart aPart = tempTerrainParts.pop();
      TerrainPart bPart = tempTerrainParts.pop();
      if (aPart.isVeriticalSimilar(bPart)) {
        aPart.join(bPart);
        terrainPartPool.free(bPart);
        out.add(aPart);
      } else {
        out.add(aPart);
        out.add(bPart);
      }
    }

    tempTerrainParts.clear();*/
  }

  public void begin() {
    solidVoxelAssembler.begin();
    transparentVoxelAssembler.begin();
    facesToBuild.clear();
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
    if (side == Block.Side.all || side == Block.Side.side) {
      throw new GdxRuntimeException("I cannot assemble chunk face for: " + side.toString());
    }
    optimizeFace(side, terrainParts);
    if (terrainParts.size > 0) {
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
  }
}
