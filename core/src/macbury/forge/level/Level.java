package macbury.forge.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.components.Movement;
import macbury.forge.components.Position;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.frustrum.FrustrumDebugAndRenderer;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.octree.OctreeNode;
import macbury.forge.systems.engine.LevelEntityEngine;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  public final LevelEntityEngine     entities;
  public final PerspectiveCamera     camera;
  public final VoxelBatch            batch;
  public final ChunkMap              terrainMap;
  public final LevelState            state;
  public final OctreeNode            octree;
  public final RenderContext         renderContext;
  public final FrustrumDebugAndRenderer frustrumDebugger;

  public Level(LevelState state) {
    this.state               = state;
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.octree              = OctreeNode.root();
    this.batch               = new VoxelBatch(renderContext);
    this.camera              = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.frustrumDebugger    = new FrustrumDebugAndRenderer(camera);
    this.entities            = new LevelEntityEngine(this);
    this.terrainMap          = state.terrainMap;

    octree.setBounds(terrainMap.getBounds(ChunkMap.TILE_SIZE));
    entities.rendering.setBatch(batch);
    entities.terrain.setMap(terrainMap);

    for (int i = 0; i < 50; i++) {
      Entity e          = entities.createEntity();
      Position position = entities.createComponent(Position.class);
      position.vector.set(5,2,100+i);
      position.size.set(1,1,1);
      Movement movement = entities.createComponent(Movement.class);
      movement.speed    = 40;
      movement.direction.set(Vector3.X);

      e.add(movement);
      e.add(position);
      entities.addEntity(e);
    }

  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.far            = 512;
    camera.update(true);
  }

  public void render(float delta) {
    camera.update();
    batch.begin(camera); {
      this.entities.update(delta);
      batch.render();
    } batch.end();
    renderDebugInfo();
  }

  private void renderDebugInfo() {
    if (ForgE.config.debug) {
      renderContext.begin(); {
        renderContext.setDepthMask(true);
        renderContext.setCullFace(GL30.GL_BACK);
        renderContext.setDepthTest(GL20.GL_LEQUAL);
        entities.debug.update(Gdx.graphics.getDeltaTime());
      } renderContext.end();
    }
  }

  @Override
  public void dispose() {
    batch.dispose();
    terrainMap.dispose();
    entities.dispose();
    octree.dispose();
    frustrumDebugger.dispose();
  }

  public void setRenderType(VoxelBatch.RenderType renderType) {
    batch.setType(renderType);
  }
}
