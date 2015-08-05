package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Gdx;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 05.08.15.
 */
public class FloodFillChangeable extends TerrainCursorChangeable {
  private static final String TAG = "FloodFillChangeable";
  private Voxel oldVoxels[][][];
  private Vector3i tempA = new Vector3i();
  private boolean[][][] mark;
  private final boolean floodToLevel;
  private Voxel voxelToReplace;

  public FloodFillChangeable(VoxelMap map, boolean floodToLevel) {
    super(map);
    this.floodToLevel = floodToLevel;
    mark = new boolean[map.getWidth()][map.getHeight()][map.getDepth()];
  }


  @Override
  public void revert() {

  }

  @Override
  public void apply() {
    this.voxelToReplace = map.getVoxelForPosition(from);

    flood(from);
  }

  private void flood(Vector3i cursor) {
    //Gdx.app.log(TAG, "Cursor: " + cursor.toString());
    if (floodToLevel && from.y > cursor.y) {
      return;
    }

    if (cursor.x < 0 || cursor.y < 0 || cursor.z < 0) {
      return;
    }

    if (cursor.x >= map.getWidth() || cursor.y >= map.getHeight() || cursor.z >= map.getDepth()) {
      return;
    }

    if (mark[cursor.x][cursor.y][cursor.z]) {
      return;
    }

    Voxel currentVoxel = map.getVoxelForPosition(cursor);

    if (voxelToReplace == null) {
      if (currentVoxel != null)
        return;
    } else if (!voxelToReplace.equals(currentVoxel)) {
      return;
    }

    Voxel newVoxel   = map.findOrInitializeVoxelForPosition(cursor);
    if (newVoxel != null) {
      newVoxel.blockId = getBlockBySelectionMouse().id;
      newVoxel.alginTo = alignToSide;
      map.setVoxelForPosition(newVoxel, cursor);
    }

    mark[cursor.x][cursor.y][cursor.z] = true;

    flood(new Vector3i(cursor).add(Vector3i.LEFT));
    flood(new Vector3i(cursor).add(Vector3i.RIGHT));
    flood(new Vector3i(cursor).add(Vector3i.TOP));
    flood(new Vector3i(cursor).add(Vector3i.BOTTOM));
    flood(new Vector3i(cursor).add(Vector3i.FRONT));
    flood(new Vector3i(cursor).add(Vector3i.BACK));


  }
}
