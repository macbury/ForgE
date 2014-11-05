package macbury.forge.editor.undo_redo.actions;

import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.voxel.VoxelMap;
import macbury.forge.voxel.VoxelMaterial;

/**
 * Created by macbury on 03.11.14.
 */
public class ApplyBlock extends TerrainCursorChangeable {
  private VoxelMaterial oldVoxelMaterial;

  public ApplyBlock(AbstractSelection selection, VoxelMap map) {
    super(selection, map);
  }

  @Override
  public void revert() {
    if (oldVoxelMaterial == null) {
      map.setEmptyForPosition(from);
    } else {
      map.setMaterialForPosition(oldVoxelMaterial, from);
    }
  }

  @Override
  public void apply() {
    if (map.isEmpty(from)){
      oldVoxelMaterial = null;
    } else {
      oldVoxelMaterial = map.getMaterialForPosition(from);
    }

    map.setMaterialForPosition(map.materials.get(3), from);
  }
}
