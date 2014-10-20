package macbury.forge.editor.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import macbury.forge.ui.Overlay;

/**
 * Created by macbury on 19.10.14.
 * handles editor ui and editor input like selecting voxels, appending voxels, deleting voxels, clicking on enityt etc
 */
public class EditorSystem extends EntitySystem {
  @Override
  public void addedToEngine(Engine engine) {
    super.addedToEngine(engine);
  }

  public void setOverlay(Overlay overlay) {

  }
}
