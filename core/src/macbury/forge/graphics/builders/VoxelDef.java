package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.Color;
import macbury.forge.graphics.VoxelMaterial;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 05.11.14.
 */
public class VoxelDef {
  public Vector3i size          = new Vector3i();
  public Vector3i position      = new Vector3i();
  public VoxelMaterial material = new VoxelMaterial(1,1,1,1);
  public float ao               = 0.0f;

  public boolean shadeBottomLeftCorner = true;
  public boolean shadeTopLeftCorner = true;
  public boolean shadeTopRightCorner = true;
  public boolean shadeBottomRightCorner = true;


  public void reset() {
    ao = 0.0f;
    material.set(Color.WHITE);
    position.setZero();
    size.setZero();
    shadeBottomLeftCorner  = false;
    shadeTopRightCorner    = false;
    shadeTopLeftCorner     = false;
    shadeBottomRightCorner = false;
  }
}
