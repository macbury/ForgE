package macbury.forge.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.BaseRenderableProvider;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.octree.OctreeObject;
import macbury.forge.octree.query.FrustrumClassFilterOctreeQuery;
import macbury.forge.utils.ActionTimer;
import macbury.forge.utils.Vector3i;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Comparator;

/**
 * Created by macbury on 23.10.14.
 */
public class TerrainEngine implements Disposable, ActionTimer.TimerListener, BaseRenderableProvider {
  private static final float UPDATE_EVERY    = 0.02f;
  private static final String TAG = "TerrainEngine";
  private static final int CHUNK_TO_REBUILD_PER_TICK = 10;
  private final ActionTimer       timer;
  private final ChunkMap          map;
  private final OctreeNode        octree;
  private final GameCamera        camera;
  private final TerrainBuilder    builder;
  public  final Array<Chunk>      chunks;
  public  final Array<VoxelFaceRenderable> visibleFaces;
  public  final Array<OctreeObject> tempObjects;
  public  final Matrix4 tempMat = new Matrix4();
  public  final Vector3 tempA  = new Vector3();
  public  final Vector3 tempC  = new Vector3();
  public  final Vector3 tempD  = new Vector3();
  public  final Vector3i tempB = new Vector3i();
  private  final BoundingBox tempBox;
  private final Array<Chunk> visibleChunks;
  private final Comparator<Chunk> sorter;
  private final FrustrumClassFilterOctreeQuery frustrumOctreeQuery;

  public TerrainEngine(Level level) {
    this.timer = new ActionTimer(UPDATE_EVERY, this);
    this.timer.start();
    this.frustrumOctreeQuery  = new FrustrumClassFilterOctreeQuery();
    this.tempObjects          = new Array<OctreeObject>();
    this.visibleChunks        = new Array<Chunk>();
    this.visibleFaces         = new Array<VoxelFaceRenderable>();
    this.chunks               = new Array<Chunk>();
    this.map                  = level.terrainMap;
    this.octree               = level.octree;
    this.camera               = level.camera;
    this.builder              = new TerrainBuilder(map);
    this.tempBox              = new BoundingBox();
    frustrumOctreeQuery.setKlass(Chunk.class);

    this.sorter               = new Comparator<Chunk>() {
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
    };
  }

  public void update() {
    timer.update(Gdx.graphics.getDeltaTime());
    rebuild();
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    occulsion();
  }

  /**
   * Check which chunks with its renderables are visible!
   */
  private void occulsion() {
    visibleFaces.clear();
    visibleChunks.clear();
    tempObjects.clear();

    camera.extendFov();
    frustrumOctreeQuery.setFrustum(camera.normalOrDebugFrustrum());
    octree.retrieve(tempObjects, frustrumOctreeQuery);

    tempC.set(camera.normalOrDebugPosition());

    while(tempObjects.size > 0) {
      Chunk visibleChunk = (Chunk) tempObjects.pop();
      if (visibleChunk.renderables.size > 0) {
        visibleChunks.add(visibleChunk);
        for (int i = 0; i < visibleChunk.renderables.size; i++) {
          VoxelFaceRenderable renderable = visibleChunk.renderables.get(i);
          //http://www.gamasutra.com/view/feature/131773/a_compact_method_for_backface_.php?print=1
          //tempA.set(renderable.boundingBox.getCenter());
          if (camera.boundsInFrustum(renderable.boundingBox) /*&& tempA.sub(tempC).scl(camera.direction).nor().dot(renderable.direction) >= 0f*/) {
            visibleFaces.add(renderable);
          }
        }
      }
    }
    visibleChunks.sort(sorter);
    camera.restoreFov();
  }

  public boolean rebuild() {
    return rebuild(CHUNK_TO_REBUILD_PER_TICK);
  }

  /**
   * Rebuild pending chunks in queue, return true if everything has been rebuilded
   * @return
   */
  public boolean rebuild(int i) {
    if (map.chunkToRebuild.size > 0) {
      ForgE.blocks.loadAtlasAndUvsIfNull();
      Gdx.app.log(TAG, "Chunks to rebuild: " + map.chunkToRebuild.size);
      while(map.chunkToRebuild.size > 0) {
        Chunk chunk = map.chunkToRebuild.pop();
        buildChunkGeometry(chunk);
        i--;
        if (i <= 0) break;
      }
      occulsion();
    }

    return map.chunkToRebuild.size == 0;
  }

  private void buildChunkGeometry(Chunk chunk) {
    chunk.clearFaces();
    builder.begin(); {
      builder.cursor.set(chunk);

      while(builder.next()) {
        builder.buildFaceForChunk(chunk);
      }
    } builder.end();
    if (ForgE.config.cacheGeometry && chunk.renderables.size > 0) {
      Kryo kryo                                = ForgE.storage.pool.borrow();

      try {

        File handle = new File("/tmp/"+chunk.position.toString()+".geo");
        Gdx.app.log(TAG, "Saving: " + handle.getAbsolutePath());
        Output output = new Output(new FileOutputStream(handle, false));
        for (VoxelFaceRenderable renderable : chunk.renderables) {
          kryo.writeObject(output, renderable);
        }

        output.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      ForgE.storage.pool.release(kryo);
      //throw new RuntimeException("Implement caching!!!");
      //TODO check if file chunk exists in cache, if true then load, else rebuild and save it
    }


    if (chunk.isEmpty()) {
      remove(chunk);
    } else {
      chunk.updateBoundingBox();
      if (!chunks.contains(chunk, true)) {
        chunks.add(chunk);
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
    renderables.addAll(visibleFaces);
  }
}
