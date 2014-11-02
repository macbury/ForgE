package macbury.forge.editor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import macbury.forge.components.Cursor;
import macbury.forge.components.Position;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.BoxSelection;
import macbury.forge.editor.utils.MousePosition;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.ui.Overlay;
import macbury.forge.utils.VoxelCursor;
import macbury.forge.utils.VoxelPicker;

/**
 * Created by macbury on 19.10.14.
 * handles editor ui and editor input like selecting voxels, appending voxels, deleting voxels, clicking on enityt etc
 */
public class EditorSystem extends EntitySystem {
  private static final String TAG = "EditorSysten";
  private final GameCamera camera;
  private final TerrainEngine terrain;
  private final ChunkMap map;
  private final VoxelPicker voxelPicker;
  private final Cursor cursorComponent;
  private Overlay overlay;
  private final MousePosition mousePosition;
  public final VoxelCursor voxelCursor = new VoxelCursor();
  private AbstractSelection selection;

  public EditorSystem(Level level) {
    super();
    this.voxelPicker             = new VoxelPicker(level.terrainMap);
    this.camera                  = level.camera;
    mousePosition                = new MousePosition(camera);
    this.terrain                 = level.terrainEngine;
    this.map                     = level.terrainMap;
    Entity cursorEntity          = level.entities.createEntity();

    this.cursorComponent         = level.entities.createComponent(Cursor.class);

    cursorEntity.add(level.entities.createComponent(Position.class));
    cursorEntity.add(cursorComponent);
    level.entities.addEntity(cursorEntity);

    this.selection = new BoxSelection(this.map);

  }


  @Override
  public void update(float deltaTime) {
    cursorComponent.cursorBox.set(selection.getBoundingBox());
  }

  private boolean getCurrentVoxelCursor(float screenX, float screenY) {
    mousePosition.set(screenX, screenY);
    Ray pickRay              = camera.getPickRay(mousePosition.x, mousePosition.y);
    return voxelPicker.getVoxelPositionForPickRay(pickRay, camera.far, voxelCursor);
  }

  public void setOverlay(Overlay overlay) {
    this.overlay = overlay;
    overlay.addCaptureListener(new InputListener() {

      @Override
      public boolean mouseMoved(InputEvent event, float x, float y) {
        if (getCurrentVoxelCursor(x,y)) {
          selection.reset(voxelCursor);
          return true;
        }
        return super.mouseMoved(event,x,y);
      }

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == Input.Buttons.LEFT && getCurrentVoxelCursor(x,y)) {
          selection.start(voxelCursor);
          return true;
        }

        return super.touchDown(event,x,y,pointer,button);
      }

      @Override
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (getCurrentVoxelCursor(x,y)) {
          selection.update(voxelCursor);
        }
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (button == Input.Buttons.LEFT && getCurrentVoxelCursor(x,y)) {
          selection.end(voxelCursor);
        }
        super.touchUp(event, x, y, pointer, button);
      }
    });
  }

}
