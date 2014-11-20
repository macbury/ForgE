package macbury.forge.graphics.builders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;


/**
 * Created by macbury on 05.11.14.
 */
public class VoxelDef {
  private final VoxelMap map;
  public Vector3i size          = new Vector3i();
  public Vector3i position      = new Vector3i();
  public Vector3i voxelPosition = new Vector3i();
  public Vector3 center         = new Vector3();
  public Color material         = new Color(Color.WHITE);
  public Block block;
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
  public Voxel voxel;

  public VoxelDef(VoxelMap map) {
    this.map = map;
  }

  public void reset() {
    ao = 0.0f;
    position.setZero();
    center.setZero();
    voxelPosition.setZero();
    size.setZero();
    shadeBottomLeftCorner  = false;
    shadeTopRightCorner    = false;
    shadeTopLeftCorner     = false;
    shadeBottomRightCorner = false;
  }

  public void calculateAoFor(float baseAo) {
    if (block.envAO) {
      this.ao = baseAo;
    }

    if (!block.shadeAO) {
      return;
    }
  }

  private boolean haveAO(Vector3i direction, Vector3i level) {
    return map.isEmpty(tempB.set(voxelPosition).add(level)) && map.isNotAir(tempA.set(voxelPosition).add(level).add(direction));
  }
}
