package macbury.forge.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.systems.LevelEntityEngine;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  public final LevelEntityEngine     entities;
  public final PerspectiveCamera     camera;
  public final VoxelBatch            batch;
  public final ChunkMap              terrainMap;
  public final LevelState            state;

  public Level(LevelState state) {
    this.state               = state;
    this.batch               = new VoxelBatch();
    this.camera              = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.far               = 500;
    this.entities            = new LevelEntityEngine(this);
    this.terrainMap          = state.terrainMap;

    entities.rendering.setBatch(batch);
    entities.terrain.setMap(terrainMap);
  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  public void render(float delta) {
    camera.update();
    batch.begin(camera); {
      this.entities.update(delta);
      batch.render();
    } batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
    terrainMap.dispose();
    entities.dispose();
  }

  public void setRenderType(VoxelBatch.RenderType renderType) {
    batch.setType(renderType);
  }
}
