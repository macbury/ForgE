package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.graphics.renderable.VoxelChunkRenderableFactory;
import macbury.forge.shaders.attributes.SolidTerrainAttribute;
import macbury.forge.shaders.attributes.WaterAttribute;
import macbury.forge.shaders.attributes.WaterElevationAttribute;
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
  private final GreedyMesh greedySimpleMesh;
  private final GreedyCollider greedyCollider;
  private final VoxelsAssembler liquidVoxelAssembler;
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
    this.liquidVoxelAssembler      = new VoxelsAssembler();
    this.greedySimpleMesh          = new GreedyMesh(map);
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
    greedySimpleMesh.begin(side, chunk.start); {
      if (greedySimpleMesh.haveResults()) {
        greedySimpleMesh.getResults(quadParts);
        createTrianglesFor(side, quadParts, solidVoxelAssembler, transparentVoxelAssembler, liquidVoxelAssembler);
        quadParts.clear();
        greedyCollider.begin(side, chunk.start); {
          if (greedyCollider.haveResults()) {
            greedyCollider.getResults(quadParts);
            createCollidersFor(side, quadParts, chunk);
          }
        } greedyCollider.end();
      }
    } greedySimpleMesh.end();
  }

  public void assembleMesh(Chunk chunk) {
    buildFaceForLiquidWithAssembler(chunk, liquidVoxelAssembler);
    buildFaceForChunkWithAssembler(chunk, solidVoxelAssembler, false);
    buildFaceForChunkWithAssembler(chunk, transparentVoxelAssembler, true);
  }

  public void assembleFactories(Chunk chunk, Array<VoxelChunkRenderableFactory> out) {
    buildFactoryAndInsertFor(chunk, liquidVoxelAssembler, new Material(new WaterAttribute()), out);
    buildFactoryAndInsertFor(chunk, solidVoxelAssembler, new Material(new SolidTerrainAttribute()), out);
    buildFactoryAndInsertFor(chunk, transparentVoxelAssembler, new Material(new SolidTerrainAttribute(), new BlendingAttribute(true,1f)), out);
  }

  private VoxelChunkRenderableFactory buildFactoryFor(Chunk chunk, VoxelsAssembler assembler, Material material) {
    if (assembler.isEmpty()) {
      return null;
    } else {
      VoxelChunkRenderableFactory voxelChunkRenderableFactory = new VoxelChunkRenderableFactory();
      voxelChunkRenderableFactory.material                    = material;
      voxelChunkRenderableFactory.primitiveType               = GL30.GL_TRIANGLES;

      if (ForgE.config.generateWireframe)
        voxelChunkRenderableFactory.wireframe           = assembler.wireframe();
      voxelChunkRenderableFactory.triangleCount         = assembler.getTriangleCount();
      voxelChunkRenderableFactory.attributes            = MeshVertexInfo.voxelTypes();
      voxelChunkRenderableFactory.meshFactory           = assembler.meshFactory(voxelChunkRenderableFactory.attributes);
      return voxelChunkRenderableFactory;
    }
  }

  private void buildFactoryAndInsertFor(Chunk chunk, VoxelsAssembler assembler, Material material, Array<VoxelChunkRenderableFactory> out) {
    VoxelChunkRenderableFactory voxelChunkRenderableFactory = buildFactoryFor(chunk, assembler, material);
    if (voxelChunkRenderableFactory != null)
      out.add(voxelChunkRenderableFactory);
  }

  private void buildFaceForLiquidWithAssembler(Chunk chunk, VoxelsAssembler assembler) {
    VoxelChunkRenderable renderable = buildFaceForChunkWithAssembler(chunk, assembler, false);

    if (renderable != null) {
      renderable.material = new Material(new WaterAttribute());
    }
  }

  private VoxelChunkRenderable buildFaceForChunkWithAssembler(Chunk chunk, VoxelsAssembler assembler, boolean haveTransparency) {
    if (!assembler.isEmpty()) {
      VoxelChunkRenderable renderable   = new VoxelChunkRenderable();
      renderable.primitiveType          = GL30.GL_TRIANGLES;

      if (ForgE.config.generateWireframe)
        renderable.wireframe           = assembler.wireframe();
      renderable.triangleCount         = assembler.getTriangleCount();
      renderable.meshFactory           = assembler.meshFactory(MeshVertexInfo.voxelTypes());

      renderable.worldTransform.idt();
      renderable.material = new Material(new SolidTerrainAttribute());
      if (haveTransparency) {
        renderable.material.set(new BlendingAttribute(true,1f));
      }

      //renderable.haveTransparency = haveTransparency;
      /*renderable.worldTransform.translate(chunk.worldPosition);
      renderable.mesh.calculateBoundingBox(renderable.boundingBox);
      renderable.boundingBox.min.add(chunk.worldPosition);
      renderable.boundingBox.max.add(chunk.worldPosition);*/
      //Gdx.app.log(TAG, "Bounding box for renderable: " + cursor.size.toString());
      chunk.addFace(renderable);
      return renderable;
    } else {
      return null;
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

  private void createTrianglesFor(Block.Side side, Array<AbstractGreedyAlgorithm.GreedyQuad> terrainParts, VoxelsAssembler solidVoxelAssembler, VoxelsAssembler transparentVoxelAssembler, VoxelsAssembler liquidVoxelAssembler) {
    for (AbstractGreedyAlgorithm.GreedyQuad part : terrainParts) {
      voxelDef.block = part.block;
      tempB.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.voxelSize);
      tempA.set(part.voxelPosition.x, part.voxelPosition.y, part.voxelPosition.z).scl(map.voxelSize).sub(tempB);

      voxelDef.position.set(tempA);
      voxelDef.voxelPosition.set(part.voxelPosition.x, part.voxelPosition.y, part.voxelPosition.z);
      voxelDef.size.set(map.voxelSize);
      voxelDef.voxel         = part.voxel;
      voxelDef.center.set(map.voxelSize.x / 2f, map.voxelSize.y / 2f, map.voxelSize.z / 2f);
      if (part.block.isLiquid()) {
        liquidVoxelAssembler.face(voxelDef, side, part);
      } else if (part.block.transparent) {
        transparentVoxelAssembler.face(voxelDef, side, part);
      } else {
        solidVoxelAssembler.face(voxelDef, side, part);
      }
    }
  }

  public void dispose() {
    liquidVoxelAssembler.dispose();
    solidVoxelAssembler.dispose();
    transparentVoxelAssembler.dispose();
    greedySimpleMesh.dispose();
    greedyCollider.dispose();
  }
}
