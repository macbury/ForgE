package macbury.forge.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.utils.OcculsionTimer;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.octree.query.FrustrumClassFilterOctreeQuery;
import macbury.forge.utils.ActionTimer;
import macbury.forge.utils.Vector3i;

import java.util.Comparator;

/**
 * Created by macbury on 23.10.14.
 */
public class TerrainEngine implements Disposable {
  private static final String TAG = "TerrainEngine";
  private static final int CHUNK_TO_REBUILD_PER_TICK = 10;
  private final ChunkMap          map;
  private final OctreeNode        octree;
  private final TerrainBuilder    builder;
  public  final Array<Chunk>      chunks;
  public  final Array<OctreeObject> tempObjects;
  public  final Vector3 tempA  = new Vector3();
  public  final Vector3 tempC  = new Vector3();
  public  final Vector3i tempB = new Vector3i();
  private  final BoundingBox tempBox;
  private final FrustrumClassFilterOctreeQuery frustrumOctreeQuery;

  private final Array<TerrainEngineListener> listeners = new Array<TerrainEngineListener>();

  public TerrainEngine(Level level) {
    this.frustrumOctreeQuery  = new FrustrumClassFilterOctreeQuery();
    this.tempObjects          = new Array<OctreeObject>();
    this.chunks               = new Array<Chunk>();
    this.map                  = level.terrainMap;
    this.octree               = level.octree;
    this.builder              = new TerrainBuilder(map);
    this.tempBox              = new BoundingBox();
    frustrumOctreeQuery.setKlass(Chunk.class);

    /*this.sorter               = new Comparator<Chunk>() {
      @Override
      public int compare(Chunk o1, Chunk o2) {
        tempA.set(o1.worldPosition);
        tempC.set(o2.worldPosition);
        final float dst = (int)(1000f * camera.normalOrDebugPosition().dst2(tempA)) - (int)(1000f * camera.normalOrDebugPosition().dst2(tempC));
        final int result = dst < 0 ? -1 : (dst > 0 ? 1 : 0);
        return result;
      }

      @Override
      public boolean equals(Object obj) {
        return false;
      }
    };*/
  }

  public void update() {
    rebuild();
  }


  /**
   * Check which chunks with its renderables are visible!
   */
  public void occulsion(GameCamera camera, Array<Chunk> visibleChunks, Array<VoxelChunkRenderable> visibleFaces) {
    visibleFaces.clear();
    visibleChunks.clear();
    tempObjects.clear();

    camera.extendFov();
    frustrumOctreeQuery.setFrustum(camera.normalOrDebugFrustrum());
    octree.retrieve(tempObjects, frustrumOctreeQuery);

   // tempC.set(camera.normalOrDebugPosition());

    while(tempObjects.size > 0) {
      Chunk visibleChunk = (Chunk) tempObjects.pop();
      if (visibleChunk.renderables.size > 0) {
        visibleChunks.add(visibleChunk);
        for (int i = 0; i < visibleChunk.renderables.size; i++) {
          VoxelChunkRenderable renderable = visibleChunk.renderables.get(i);

          //http://www.gamasutra.com/view/feature/131773/a_compact_method_for_backface_.php?print=1
          //tempA.set(renderable.boundingBox.getCenter());
          if (renderable.mesh != null && camera.boundsInFrustum(renderable.boundingBox) /*&& tempA.sub(tempC).scl(camera.direction).nor().dot(renderable.direction) >= 0f*/) {
            visibleFaces.add(renderable);
          }
        }
      }
    }
    camera.restoreFov();
  }

  /**
   * Check which chunks with its renderables are visible!
   */
  public void occulsion(VoxelBatch.CameraProvider cameraProvider, Array<Chunk> visibleChunks, Array<VoxelChunkRenderable> visibleFaces) {
    visibleFaces.clear();
    visibleChunks.clear();
    tempObjects.clear();

    frustrumOctreeQuery.setFrustum(cameraProvider.getBatchCamera().frustum);
    octree.retrieve(tempObjects, frustrumOctreeQuery);
    while(tempObjects.size > 0) {
      Chunk visibleChunk = (Chunk) tempObjects.pop();
      if (visibleChunk.renderables.size > 0) {
        visibleChunks.add(visibleChunk);
        for (int i = 0; i < visibleChunk.renderables.size; i++) {
          VoxelChunkRenderable renderable = visibleChunk.renderables.get(i);
          if (renderable.mesh != null && cameraProvider.getBatchCamera().frustum.boundsInFrustum(renderable.boundingBox)) {
            visibleFaces.add(renderable);
          }
        }
      }
    }
  }

  public boolean rebuild() {
    return rebuild(CHUNK_TO_REBUILD_PER_TICK, true);
  }

  /**
   * Rebuild pending chunks in queue, return true if everything has been rebuilded
   * @return
   */
  public boolean rebuild(int i, boolean buildNowMesh) {
    boolean notDone = map.chunkToRebuild.size > 0;
    if (map.chunkToRebuild.size > 0) {
      ForgE.blocks.loadAtlasAndUvsIfNull();
      Gdx.app.log(TAG, "Chunks to rebuild: " + map.chunkToRebuild.size);
      while(map.chunkToRebuild.size > 0) {
        Chunk chunk = map.chunkToRebuild.pop();
        buildChunkGeometry(chunk);
        i--;
        if (i <= 0) break;
      }

      OcculsionTimer.runAll();
    }

    if (buildNowMesh || notDone) {
      buildMeshForAvalibleChunks();
    }


    return !notDone;
  }

  public boolean rebuildInBackground(int i) {
    if (map.chunkToRebuild.size > 0) {
      ForgE.blocks.loadAtlasAndUvsIfNull();
      Gdx.app.log(TAG, "Chunks to rebuild: " + map.chunkToRebuild.size);
      while(map.chunkToRebuild.size > 0) {
        Chunk chunk = map.chunkToRebuild.pop();
        buildChunkGeometry(chunk);
        i--;
        if (i <= 0) break;
      }
    }


    return map.chunkToRebuild.size == 0;
  }

  public void buildMeshForAvalibleChunks() {
    for (Chunk chunk : chunks) {
      chunk.buildMesh();
    }
  }

  private void buildChunkGeometry(Chunk chunk) {
    remove(chunk);
    builder.begin(); {
      builder.cursor.set(chunk);

      while(builder.next()) {
        builder.buildFaceForChunk(chunk);
      }

      builder.assembleMesh(chunk);
    } builder.end();

    add(chunk);
  }

  private void add(Chunk chunk) {
    if (!chunk.isEmpty()) {
      chunk.updateBoundingBox();
      chunks.add(chunk);
      for (TerrainEngineListener listener : listeners) {
        listener.onChunkAdded(chunk, this);
      }
    }
  }

  private void remove(Chunk chunk) {
    chunks.removeValue(chunk, true);

    for (TerrainEngineListener listener : listeners) {
      listener.onChunkRemove(chunk, this);
    }
    chunk.dispose();
  }

  @Override
  public void dispose() {
    while(chunks.size > 0) {
      remove(chunks.pop());
    }
    builder.dispose();
    listeners.clear();
  }

  public void addListener(TerrainEngineListener listener) {
    listeners.add(listener);
  }

  public void removeListener(TerrainEngineListener listener) {
    listeners.removeValue(listener, true);
  }

  public interface TerrainEngineListener {
    public void onChunkRemove(Chunk chunk, TerrainEngine engine);
    public void onChunkAdded(Chunk chunk, TerrainEngine engine);
  }
}
