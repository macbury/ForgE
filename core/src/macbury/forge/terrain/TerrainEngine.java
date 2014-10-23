package macbury.forge.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.BaseRenderableProvider;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.level.Level;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.shaders.utils.RenderableBaseShader;
import macbury.forge.utils.ActionTimer;

/**
 * Created by macbury on 23.10.14.
 */
public class TerrainEngine implements Disposable, ActionTimer.TimerListener, BaseRenderableProvider {
  private static final float UPDATE_EVERY    = 0.05f;
  private static final String TERRAIN_SHADER = "terrain";
  private final ActionTimer       timer;
  private final ChunkMap          map;
  private final OctreeNode        octree;
  private final GameCamera        camera;
  private final TerrainBuilder    builder;
  public  final Array<Chunk>      chunks;
  public  final Array<OctreeObject> visibleChunks;
  private int cursor;

  public TerrainEngine(Level level) {
    this.timer = new ActionTimer(UPDATE_EVERY, this);
    this.timer.start();

    this.visibleChunks        = new Array<OctreeObject>();
    this.chunks               = new Array<Chunk>();
    this.map                  = level.terrainMap;
    this.octree               = level.staticOctree;
    this.camera               = level.camera;
    this.builder              = new TerrainBuilder(map);
  }

  public void update() {
    timer.update(Gdx.graphics.getDeltaTime());
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    rebuild();
    occulsion();
  }

  private void occulsion() {
    visibleChunks.clear();

    octree.retrieve(visibleChunks, camera.normalOrDebugFrustrum(), false);
  }


  /**
   * Rebuild pending chunks in queue, return true if everything has been rebuilded
   * @return
   */
  private boolean rebuild() {
    if (map.chunkToRebuild.size > 0) {
      while (map.chunkToRebuild.size > 0) {
        Chunk chunk = map.chunkToRebuild.pop();
        buildChunkGeometry(chunk);
      }

      octree.clear();
      for (int i = 0; i < chunks.size; i++) {
        octree.insert(chunks.get(i));
      }
    }

    return map.chunkToRebuild.size == 0;
  }

  private void buildChunkGeometry(Chunk chunk) {
    builder.begin(); {
      builder.facesForChunk(chunk);

      if (builder.isEmpty()) {
        remove(chunk);
      } else {
        if (chunk.renderable == null) {
          chunk.renderable               = new TerrainChunkRenderable();
          chunk.renderable.primitiveType = GL30.GL_TRIANGLES;
          chunk.renderable.shader        = (RenderableBaseShader) ForgE.shaders.get(TERRAIN_SHADER);
        } else if (chunk.renderable.mesh != null) {
          chunk.renderable.mesh.dispose();
        }

        if (ForgE.config.generateWireframe)
          chunk.renderable.wireframe           = builder.wireframe();
        chunk.renderable.mesh                  = builder.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.Color);
        chunk.updateBoundingBox();
        if (!chunks.contains(chunk, true)) {
          chunks.add(chunk);
        }
      }
    }
  }

  private void remove(Chunk chunk) {
    chunks.removeValue(chunk, true);
    chunk.dispose();
  }

  @Override
  public void dispose() {
    while(chunks.size > 0) {
      remove(chunks.pop());
    }
    builder.dispose();
  }

  @Override
  public void getRenderables(Array<BaseRenderable> renderables) {
    for (int i = 0; i < visibleChunks.size; i++) {
      Chunk chunk = (Chunk)visibleChunks.get(i);
      renderables.add(chunk.renderable);
    }
  }
}
