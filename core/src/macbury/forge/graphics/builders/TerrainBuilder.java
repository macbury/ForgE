package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.terrain.greedy.AbstractGreedyAlgorithm;
import macbury.forge.terrain.greedy.GreedyMesh;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder {

  private final VoxelsAssembler solidVoxelAssembler;
  private final VoxelsAssembler transparentVoxelAssembler;

  private static final String TAG = "TerrainBuilder";

  private final ChunkMap map;
  public final TerrainCursor cursor;
  private final GreedyMesh greedyMesh;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  private Array<Block.Side> facesToBuild = new Array<Block.Side>();

  private Vector3i nextTileToCheck = new Vector3i();
  private final VoxelDef voxelDef;
  private Array<AbstractGreedyAlgorithm.GreedyQuad> terrainParts = new Array<AbstractGreedyAlgorithm.GreedyQuad>();


  public TerrainBuilder(ChunkMap voxelMap) {
    super();
    this.map                       = voxelMap;
    this.cursor                    = new TerrainCursor();

    this.voxelDef                  = new VoxelDef(map);
    this.solidVoxelAssembler       = new VoxelsAssembler();
    this.transparentVoxelAssembler = new VoxelsAssembler();

    this.greedyMesh                = new GreedyMesh(map);
  }


  public void begin() {
    solidVoxelAssembler.begin();
    transparentVoxelAssembler.begin();
    facesToBuild.clear();
    facesToBuild.add(Block.Side.top);
    facesToBuild.add(Block.Side.bottom);

    facesToBuild.add(Block.Side.left);
    facesToBuild.add(Block.Side.right);

    facesToBuild.add(Block.Side.back);
    facesToBuild.add(Block.Side.front);
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
    greedyMesh.greedy(side, chunk.start);

    if (greedyMesh.haveResults()) {
      greedyMesh.getResults(terrainParts);
      createTrianglesFor(side, terrainParts, solidVoxelAssembler, transparentVoxelAssembler);
    }
  }

  public void assembleMesh(Chunk chunk) {
    buildFaceForChunkWithAssembler(chunk, solidVoxelAssembler, false);
    buildFaceForChunkWithAssembler(chunk, transparentVoxelAssembler, true);
  }

  private void buildFaceForChunkWithAssembler(Chunk chunk, VoxelsAssembler assembler, boolean haveTransparency) {
    if (!assembler.isEmpty()) {
      VoxelChunkRenderable renderable   = new VoxelChunkRenderable();
      renderable.primitiveType         = GL30.GL_TRIANGLES;

      if (ForgE.config.generateWireframe)
        renderable.wireframe           = assembler.wireframe();
      renderable.triangleCount         = assembler.getTriangleCount();
      renderable.mesh                  = assembler.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.TextureCord, MeshVertexInfo.AttributeType.Material, MeshVertexInfo.AttributeType.TextureFullCords);

      renderable.worldTransform.idt();
      renderable.haveTransparency = haveTransparency;
      renderable.worldTransform.translate(chunk.worldPosition);
      renderable.mesh.calculateBoundingBox(renderable.boundingBox);
      renderable.boundingBox.min.add(chunk.worldPosition);
      renderable.boundingBox.max.add(chunk.worldPosition);
      //Gdx.app.log(TAG, "Bounding box for renderable: " + cursor.size.toString());
      chunk.addFace(renderable);
    }
  }


  private void createTrianglesFor(Block.Side side, Array<AbstractGreedyAlgorithm.GreedyQuad> terrainParts, VoxelsAssembler solidVoxelAssembler, VoxelsAssembler transparentVoxelAssembler) {
    for (AbstractGreedyAlgorithm.GreedyQuad part : terrainParts) {
      voxelDef.block = part.block;
      tempB.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.voxelSize);
      tempA.set(part.voxelPosition.x, part.voxelPosition.y, part.voxelPosition.z).scl(map.voxelSize).sub(tempB);

      voxelDef.position.set(tempA);
      voxelDef.voxelPosition.set(part.voxelPosition.x, part.voxelPosition.y, part.voxelPosition.z);
      voxelDef.size.set(map.voxelSize);
      voxelDef.voxel         = part.voxel;
      voxelDef.center.set(map.voxelSize.x / 2f, map.voxelSize.y / 2f, map.voxelSize.z / 2f);
      if (part.block.transparent) {
        transparentVoxelAssembler.face(voxelDef, side, part);
      } else {
        solidVoxelAssembler.face(voxelDef, side, part);
      }
    }
  }


  public void dispose() {
    solidVoxelAssembler.dispose();
    transparentVoxelAssembler.dispose();
    greedyMesh.dispose();
  }


}
