package macbury.forge.systems;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.Level;

/**
 * Created by macbury on 19.10.14.
 */
public class LevelEntityEngine extends PooledEngine implements Disposable {
  public final RenderingSystem rendering;
  public final TerrainSystem terrain;

  public LevelEntityEngine(Level level) {
    terrain   = new TerrainSystem(this);
    rendering = new RenderingSystem();

    addSystem(terrain);
    addSystem(rendering);
  }

  @Override
  public void dispose() {
    removeAllEntities();
    terrain.dispose();
  }
}
