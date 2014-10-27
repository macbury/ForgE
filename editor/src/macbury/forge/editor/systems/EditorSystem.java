package macbury.forge.editor.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.Level;
import macbury.forge.terrain.TerrainEngine;
import macbury.forge.ui.Overlay;
import macbury.forge.utils.Vector3Int;

/**
 * Created by macbury on 19.10.14.
 * handles editor ui and editor input like selecting voxels, appending voxels, deleting voxels, clicking on enityt etc
 */
public class EditorSystem extends IntervalSystem {
  private final GameCamera camera;
  private final TerrainEngine terrain;
  private Overlay overlay;
  private final MousePosition mousePosition = new MousePosition();
  private Vector3Int intersectionPoint = new Vector3Int();

  public EditorSystem(Level level) {
    super(0.1f);
    this.camera  = level.camera;
    this.terrain = level.terrainEngine;
  }

  @Override
  protected void updateInterval() {
    if (mousePosition.isDirty()) {
      mousePosition.setDirty(false);
      Ray pickRay              = camera.getPickRay(mousePosition.x, mousePosition.y);
      intersectionPoint.setZero();
      if (terrain.getVoxelPositionForPickRay(pickRay, intersectionPoint)) {
        //Gdx.app.log("Pick ray", pickRay.toString());
        Gdx.app.log("Voxel position", intersectionPoint.toString());
      } else {
        Gdx.app.log("TEST", "Not Found!");
      }

    }
  }

  public void setOverlay(Overlay overlay) {
    this.overlay = overlay;
    overlay.addCaptureListener(new InputListener() {

      @Override
      public boolean mouseMoved(InputEvent event, float x, float y) {
        mousePosition.set(x, y);
        return true;
      }
    });
  }
}
