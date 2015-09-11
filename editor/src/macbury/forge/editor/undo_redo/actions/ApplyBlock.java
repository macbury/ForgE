package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Gdx;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyBlock extends TerrainCursorChangeable {
  private static final String TAG = "ApplyBlock";
  private Voxel oldVoxel;

  public ApplyBlock(VoxelMap map) {
    super(map);
  }

  @Override
  public void revert() {
    map.setVoxelForPosition(oldVoxel, from);
    oldVoxel = null;
  }

  @Override
  public void apply() {
    if (map.isEmpty(from)) {
      oldVoxel = null;
    } else {
      oldVoxel = new Voxel(map.getVoxelForPosition(from));
    }

    Voxel currentVoxel   = map.findOrInitializeVoxelForPosition(from);
    if (currentVoxel != null) {
      currentVoxel.blockId = getBlockBySelectionMouse().id;
      currentVoxel.alginTo = alignToSide;
      map.setVoxelForPosition(currentVoxel, from);
    }
    ForgE.log(TAG, "Algin for: " + from.toString() + " is " + alignToSide.toString());
  }
}
