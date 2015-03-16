package macbury.forge.editor.undo_redo.actions;

import macbury.forge.blocks.Block;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 16.03.15.
 */
public class BlockSave {
  public final Voxel voxel;
  public final Vector3i pos;

  public BlockSave(Vector3i pos, Voxel voxel) {
    if (voxel == null) {
      this.voxel = null;
    } else {
      this.voxel = new Voxel(voxel);
    }
    this.pos   = new Vector3i(pos);
  }

  public void applyTo(VoxelMap map) {
    if (voxel == null) {
      map.setEmptyForPosition(pos);
    } else {
      map.setBlockForPosition(voxel.getBlock(), pos);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (BlockSave.class.isInstance(obj)) {
      BlockSave other = (BlockSave)obj;
      return (other.pos.equals(pos));
    } else {
      return super.equals(obj);
    }

  }
}
