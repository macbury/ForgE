package macbury.forge.editor.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import macbury.forge.components.Cursor;
import macbury.forge.components.Position;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.ui.Overlay;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 19.10.14.
 * handles editor ui and editor input like selecting voxels, appending voxels, deleting voxels, clicking on enityt etc
 */
public class EditorSystem extends EntitySystem implements EntityListener {
  private final GameCamera camera;
  private final TerrainEngine terrain;
  private final Position cursorPositionComponent;
  private final ChunkMap map;
  private Overlay overlay;
  private final MousePosition mousePosition;
  public final Vector3i intersectionPoint = new Vector3i();

  public EditorSystem(Level level) {
    super();
    this.camera   = level.camera;
    mousePosition = new MousePosition(camera);
    this.terrain  = level.terrainEngine;
    this.map      = level.terrainMap;
    Entity cursorEntity          = level.entities.createEntity();
    this.cursorPositionComponent = level.entities.createComponent(Position.class);
    cursorEntity.add(cursorPositionComponent);
    Cursor cursorComponent       = level.entities.createComponent(Cursor.class);
    cursorEntity.add(cursorComponent);
    level.entities.addEntity(cursorEntity);
  }

  @Override
  public void addedToEngine(Engine engine) {
    super.addedToEngine(engine);
    engine.addEntityListener(this);
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    if (mousePosition.isDirty()) {
      mousePosition.setDirty(false);


    }
  }

  private void updateCursor() {
    Ray pickRay              = camera.getPickRay(mousePosition.x, mousePosition.y);

    if (terrain.getVoxelPositionForPickRay(pickRay, camera.far, intersectionPoint)) {
      cursorPositionComponent.size.set(ChunkMap.TERRAIN_TILE_SIZE);
      cursorPositionComponent.vector.set(intersectionPoint.x, intersectionPoint.y, intersectionPoint.z);
    }
  }

  public void setOverlay(Overlay overlay) {
    this.overlay = overlay;
    overlay.addCaptureListener(new InputListener() {

      @Override
      public boolean mouseMoved(InputEvent event, float x, float y) {
        mousePosition.set(x, y);
        updateCursor();
        return true;
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        mousePosition.set(x, y);
        //ColorMaterial mat = map.materials.get(4);
        //map.setMaterialForPosition(mat, intersectionPoint);
        //updateCursor();
        return true;
      }
    });
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {

  }
}
