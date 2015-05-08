package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by macbury on 16.03.15.
 */
public class TreeBuilderChangeable extends TerrainCursorChangeable {
  private static final float MAX_HEIGHT = 2;
  private static final double MIN_HEIHGT = 5;
  private final int height;
  private Array<BlockSave> oldBlocks;

  public TreeBuilderChangeable(VoxelMap map) {
    super(map);
    this.height      = (int)Math.round(MIN_HEIHGT + Math.random() * MAX_HEIGHT);
    this.oldBlocks   = new Array<BlockSave>();
  }

  @Override
  public void revert() {
    for(BlockSave save : oldBlocks) {
      save.applyTo(map);
    }
  }

  private void putBlock(Block type, Vector3i pos) {
    saveBlock(pos);
    Voxel currentVoxel   = map.findOrInitializeVoxelForPosition(pos);
    if (currentVoxel != null) {
      currentVoxel.blockId = type.id;
      currentVoxel.alginTo = alignToSide;
      map.setVoxelForPosition(currentVoxel, pos);
    }
  }

  @Override
  public void apply() {
    oldBlocks.clear();
    Vector3i cursor = new Vector3i();
    for (int y = 0; y < height; y++) {
      cursor.set(from).add(0, y, 0);
      putBlock(blockPrimary, cursor);
    }

    int size = (int)Math.round(1 + Math.random());

    for (int x = -size; x <= size; x++) {
      for (int y = -size; y <= size; y++) {
        for (int z = -size; z <= size; z++) {
          if (Math.abs(x) == size && Math.abs(y) == size && Math.abs(z) == size) {
            continue;
          }

          putBlock(blockSecondary, cursor.set(from).add(x + 1, y+height, z));
          putBlock(blockSecondary, cursor.set(from).add(x - 1, y+height, z));
          putBlock(blockSecondary, cursor.set(from).add(x, y+height, z + 1));
          putBlock(blockSecondary, cursor.set(from).add(x, y+height, z - 1));
          putBlock(blockSecondary, cursor.set(from).add(x, y+height+1, z));
        }
      }
    }

    cursor.set(from);
    //putBlock(woodBlock, cursor);
  }

  private void saveBlock(Vector3i cursor) {
    BlockSave save = new BlockSave(cursor, map.getVoxelForPosition(cursor));
    if (!oldBlocks.contains(save, false)) {
      oldBlocks.add(save);
    }
  }
}
