package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.Color;
import macbury.forge.voxel.VoxelMap;
import macbury.forge.voxel.VoxelMaterial;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 05.11.14.
 */
public class VoxelDef {
  private final VoxelMap map;
  public Vector3i size          = new Vector3i();
  public Vector3i position      = new Vector3i();
  public Vector3i voxelPosition = new Vector3i();
  public VoxelMaterial material = new VoxelMaterial(1,1,1,1);
  public float ao               = 0.0f;

  public boolean shadeBottomLeftCorner = true;
  public boolean shadeTopLeftCorner = true;
  public boolean shadeTopRightCorner = true;
  public boolean shadeBottomRightCorner = true;

  private Vector3i tempA = new Vector3i();
  private Vector3i tempB = new Vector3i();

  private final Vector3i FRONT_LEFT  = new Vector3i(Vector3i.FRONT).add(Vector3i.LEFT);
  private final Vector3i FRONT_RIGHT = new Vector3i(Vector3i.FRONT).add(Vector3i.RIGHT);

  private final Vector3i BACK_LEFT  = new Vector3i(Vector3i.BACK).add(Vector3i.LEFT);
  private final Vector3i BACK_RIGHT = new Vector3i(Vector3i.BACK).add(Vector3i.RIGHT);

  public VoxelDef(VoxelMap map) {
    this.map = map;
  }

  public void reset() {
    ao = 0.0f;
    material.set(Color.WHITE);
    position.setZero();
    voxelPosition.setZero();
    size.setZero();
    shadeBottomLeftCorner  = false;
    shadeTopRightCorner    = false;
    shadeTopLeftCorner     = false;
    shadeBottomRightCorner = false;
  }

  public void calculateAoFor(float baseAo, TerrainBuilder.Face face) {
    this.ao = baseAo;
    if (face == TerrainBuilder.Face.Back) {
      if (haveAO(Vector3i.BACK, Vector3i.BACK)) {
        shadeTopLeftCorner = shadeTopRightCorner = true;
      }
    }

    if (face == TerrainBuilder.Face.Top) {
      if (haveAO(Vector3i.BACK, Vector3i.TOP)) {
        shadeTopLeftCorner = shadeTopRightCorner = true;
      }

      if (haveAO(Vector3i.FRONT, Vector3i.TOP)) {
        shadeBottomRightCorner = shadeBottomLeftCorner = true;
      }

      if (haveAO(Vector3i.LEFT, Vector3i.TOP)) {
        shadeBottomLeftCorner = true;
        shadeTopLeftCorner    = true;
      }

      if (haveAO(Vector3i.RIGHT, Vector3i.TOP)) {
        shadeBottomRightCorner = true;
        shadeTopRightCorner    = true;
      }

      if (haveAO(FRONT_LEFT, Vector3i.TOP)) {
        shadeBottomLeftCorner    = true;
        // flip here rect
      }

      if (haveAO(FRONT_RIGHT, Vector3i.TOP)) {
        shadeBottomRightCorner    = true;
      }

      if (haveAO(BACK_LEFT, Vector3i.TOP)) {
        shadeTopLeftCorner    = true;
      }

      if (haveAO(BACK_RIGHT, Vector3i.TOP)) {
        shadeTopRightCorner    = true;
        // flip here rect
      }
    }
  }

  private boolean haveAO(Vector3i direction, Vector3i level) {
    return map.isEmpty(tempB.set(voxelPosition).add(level)) && map.isSolid(tempA.set(voxelPosition).add(level).add(direction));
  }
}
