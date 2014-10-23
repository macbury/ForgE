package macbury.forge.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.BaseRenderableProvider;
import macbury.forge.level.Level;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.utils.ActionTimer;

/**
 * Created by macbury on 23.10.14.
 */
public class TerrainEngine implements Disposable, ActionTimer.TimerListener, BaseRenderableProvider {
  private static final float UPDATE_EVERY = 0.1f;
  private final ActionTimer timer;
  private final ChunkMap map;
  private final OctreeNode octree;
  private final PerspectiveCamera camera;

  public TerrainEngine(Level level) {
    this.timer = new ActionTimer(UPDATE_EVERY, this);
    this.timer.start();

    this.map    = level.terrainMap;
    this.octree = level.staticOctree;
    this.camera = level.camera;
  }

  public void update() {
    timer.update(Gdx.graphics.getDeltaTime());
  }

  @Override
  public void onTimerTick(ActionTimer timer) {
    rebuild();
  }

  private void rebuild() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void getRenderables(Array<BaseRenderable> renderables) {

  }
}
