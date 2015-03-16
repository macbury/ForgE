package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.blocks.Block;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 16.03.15.
 */
public class TreeBuilderChangeable extends TerrainCursorChangeable {
  private static final float MAX_HEIGHT = 3;
  private static final double MIN_HEIHGT = 4;
  private final Block leafesBlock;
  private final Block woodBlock;
  private final int height;

  public TreeBuilderChangeable(AbstractSelection selection, VoxelMap map, Block woodBlock, Block leafesBlock) {
    super(selection, map);
    this.leafesBlock = leafesBlock;
    this.woodBlock   = woodBlock;
    this.height      = (int)Math.round(MIN_HEIHGT + Math.random() * MAX_HEIGHT);
  }

  @Override
  public void revert() {

  }

  private void putBlock(Block type, Vector3i pos) {
    Voxel currentVoxel   = map.findOrInitializeVoxelForPosition(pos);
    if (currentVoxel != null) {
      currentVoxel.blockId = type.id;
      currentVoxel.alginTo = alignToSide;
      map.setVoxelForPosition(currentVoxel, pos);
    }
  }

  @Override
  public void apply() {
    Vector3i cursor = new Vector3i();
    for (int y = 0; y < height; y++) {
      cursor.set(from).add(0, y, 0);
      putBlock(woodBlock, cursor);
    }

    int size = 1;

    for (int x = -size; x <= size; x++) {
      for (int y = -size; y <= size; y++) {
        for (int z = -size; z <= size; z++) {
          if (Math.abs(x) == size && Math.abs(y) == size && Math.abs(z) == size) {
            continue;
          }

          putBlock(leafesBlock, cursor.set(from).add(x + 1, y+height, z));
          putBlock(leafesBlock, cursor.set(from).add(x - 1, y+height, z));
          putBlock(leafesBlock, cursor.set(from).add(x, y+height, z + 1));
          putBlock(leafesBlock, cursor.set(from).add(x, y+height, z - 1));
        }
      }
    }

    cursor.set(from);
    //putBlock(woodBlock, cursor);
  }
}
