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
import macbury.forge.components.Visible;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.octree.OctreeNode;
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
  public final OctreeNode            octree;
  private final RenderContext        renderContext;

  public Level(LevelState state) {
    this.state               = state;
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.octree              = OctreeNode.root();
    this.batch               = new VoxelBatch(renderContext);
    this.camera              = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.far               = 500;
    this.entities            = new LevelEntityEngine(this);
    this.terrainMap          = state.terrainMap;

    octree.setBounds(terrainMap.getBounds(ChunkMap.TILE_SIZE));
    /*octree.split();
    octree.getNode(OctreePart.FrontBottomRight).split();
    octree.getNode(OctreePart.FrontBottomRight).getNode(OctreePart.BackTopLeft).split();*/

    entities.rendering.setBatch(batch);
    entities.terrain.setMap(terrainMap);

    Entity e          = entities.createEntity();
    Position position = entities.createComponent(Position.class);
    position.vector.set(5,5,5);

    Movement movement = entities.createComponent(Movement.class);
    movement.speed    = 20;
    movement.direction.set(Vector3.X);

    e.add(movement);
    e.add(position);
    e.add(entities.createComponent(Visible.class));
    entities.addEntity(e);
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
    renderDebugInfo();
  }

  private void renderDebugInfo() {
    if (ForgE.config.renderBoundingBox) {
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
  }

  public void setRenderType(VoxelBatch.RenderType renderType) {
    batch.setType(renderType);
  }
}
