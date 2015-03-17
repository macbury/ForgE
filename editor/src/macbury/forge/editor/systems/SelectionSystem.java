package macbury.forge.editor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import macbury.forge.components.CursorComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SelectType;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.ui.Overlay;
import macbury.forge.utils.VoxelCursor;
import macbury.forge.utils.VoxelPicker;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 19.10.14.
 * handles editor ui and editor input like selecting voxels, appending voxels, deleting voxels, clicking on enityt etc
 */
public class SelectionSystem extends EntitySystem {
  private static final String TAG = "EditorSysten";
  private final GameCamera camera;
  private final ChunkMap map;
  private final VoxelPicker voxelPicker;
  private final CursorComponent cursorComponent;
  private final ShapeRenderer shapeRenderer;
  private final RenderContext renderContext;
  private Overlay overlay;
  public final VoxelCursor voxelCursor = new VoxelCursor();
  private AbstractSelection selection;
  private Array<SelectionInterface> listeners;

  public SelectionSystem(Level level) {
    super();
    this.listeners               = new Array<SelectionInterface>();
    this.voxelPicker             = new VoxelPicker(level.terrainMap);
    this.camera                  = level.camera;
    this.map                     = level.terrainMap;
    this.shapeRenderer           = level.batch.shapeRenderer;
    this.renderContext           = level.renderContext;
    Entity cursorEntity          = level.entities.createEntity();

    this.cursorComponent         = level.entities.createComponent(CursorComponent.class);

    cursorEntity.add(level.entities.createComponent(PositionComponent.class));
    cursorEntity.add(cursorComponent);
    level.entities.addEntity(cursorEntity);
  }

  public void addListener(SelectionInterface selectionInterface) {
    listeners.add(selectionInterface);
  }

  public void removeListener(SelectionInterface selectionInterface) {
    listeners.removeValue(selectionInterface, true);
  }

  @Override
  public void update(float deltaTime) {
    cursorComponent.set(selection.getBoundingBox());
    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
      selection.setSelectType(SelectType.Replace);
    } else {
      selection.setSelectType(SelectType.Append);
    }
  }

  private boolean getCurrentVoxelCursor(float screenX, float screenY) {
    Ray pickRay              = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
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
        if (selection.shouldProcessMouseButton(button) && getCurrentVoxelCursor(x,y)) {
          selection.start(voxelCursor);
          for (SelectionInterface listener : listeners) {
            listener.onSelectionStart(selection);
          }
          return true;
        }

        return super.touchDown(event,x,y,pointer,button);
      }

      @Override
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (getCurrentVoxelCursor(x,y)) {
          selection.update(voxelCursor);
          for (SelectionInterface listener : listeners) {
            listener.onSelectionChange(selection);
          }
        }
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (selection.shouldProcessMouseButton(button) && getCurrentVoxelCursor(x,y)) {
          selection.setSelectedMouseButton(button);
          selection.end(voxelCursor);
          for (SelectionInterface listener : listeners) {
            listener.onSelectionEnd(selection);
          }
          selection.reset(voxelCursor);
        }
        super.touchUp(event, x, y, pointer, button);
      }
    });
  }


  public void setSelection(AbstractSelection selection) {
    this.selection = selection;
  }
}
