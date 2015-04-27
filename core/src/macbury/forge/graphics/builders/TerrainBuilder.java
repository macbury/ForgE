package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
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
import macbury.forge.terrain.greedy.GreedyCollider;
import macbury.forge.terrain.greedy.GreedyMesh;
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
  private final GreedyCollider greedyCollider;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  private Array<Block.Side> facesToBuild = new Array<Block.Side>();

  private final VoxelDef voxelDef;
  private Array<AbstractGreedyAlgorithm.GreedyQuad> quadParts = new Array<AbstractGreedyAlgorithm.GreedyQuad>();


  public TerrainBuilder(ChunkMap voxelMap) {
    super();
    this.map                       = voxelMap;
    this.cursor                    = new TerrainCursor();

    this.voxelDef                  = new VoxelDef(map);
    this.solidVoxelAssembler       = new VoxelsAssembler();
    this.transparentVoxelAssembler = new VoxelsAssembler();

    this.greedyMesh                = new GreedyMesh(map);
    this.greedyCollider            = new GreedyCollider(map);
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
    greedyMesh.begin(side, chunk.start); {
      if (greedyMesh.haveResults()) {
        greedyMesh.getResults(quadParts);
        createTrianglesFor(side, quadParts, solidVoxelAssembler, transparentVoxelAssembler);
        quadParts.clear();
        if (ForgE.config.buildColliders) {
          greedyCollider.begin(side, chunk.start); {
            if (greedyCollider.haveResults()) {
              greedyCollider.getResults(quadParts);
              createCollidersFor(side, quadParts, chunk);
            }
          } greedyCollider.end();
        }

      }
    } greedyMesh.end();
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

  private void createCollidersFor(Block.Side side, Array<AbstractGreedyAlgorithm.GreedyQuad> quadParts, Chunk chunk) {
    for(AbstractGreedyAlgorithm.GreedyQuad quad : quadParts) {
      ChunkPartCollider chunkPartCollider = new ChunkPartCollider(quad);
      if (chunkPartCollider.canAssemble(side)) {
        chunkPartCollider.assemble(map.voxelSize, side);
        chunk.colliders.add(chunkPartCollider);
      } else {
        chunkPartCollider.dispose();
      }
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
    greedyCollider.dispose();
  }

}
