package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import macbury.forge.editor.undo_redo.actions.base.ChunkChangeable;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;
import org.lwjgl.Sys;

/**
 * Created by macbury on 05.08.15.
 */
public class FloodFillChangeable extends ChunkChangeable {
  private static final String TAG = "FloodFillChangeable";
  private boolean[][][] mark;
  private final boolean floodToLevel;
  private Voxel voxelToReplace;
  private int targetLevel;

  public FloodFillChangeable(ChunkMap map, boolean floodToLevel) {
    super(map);
    this.floodToLevel = floodToLevel;
  }


  @Override
  public void apply() {
    this.voxelToReplace = map.getVoxelForPosition(from);

    mark = new boolean[map.getWidth()][map.getHeight()][map.getDepth()];
    this.targetLevel = from.y;
    floodFill(new Vector3i(from), getBlockIdForPosition(from), getBlockBySelectionMouse().id);
  }

  private int getBlockIdForPosition(Vector3i pos) {
    Voxel seedVoxel = map.getVoxelForPosition(pos);
    if (seedVoxel == null || seedVoxel.isAir()) {
      return -1;
    } else {
      return seedVoxel.blockId;
    }
  }

  private void floodFill(Vector3i start, int seedBlockId, byte targetBlockId) {
    Array<Vector3i> stack = new Array<Vector3i>();
    stack.add(start);

    while (stack.size > 0) {
      Vector3i cursor = stack.pop();
      if (isInsideMap(cursor) && getBlockIdForPosition(cursor) == seedBlockId) {
        saveChunkForCursor(cursor);
        putBlock(cursor, targetBlockId);

        stack.add(new Vector3i(cursor).add(Vector3i.LEFT));
        stack.add(new Vector3i(cursor).add(Vector3i.RIGHT));
        stack.add(new Vector3i(cursor).add(Vector3i.TOP));
        stack.add(new Vector3i(cursor).add(Vector3i.BOTTOM));
        stack.add(new Vector3i(cursor).add(Vector3i.FRONT));
        stack.add(new Vector3i(cursor).add(Vector3i.BACK));
      }
    }
    System.gc();
  }

  private void putBlock(Vector3i cursor, byte targetBlockId) {
    Voxel newVoxel   = map.findOrInitializeVoxelForPosition(cursor);
    if (newVoxel != null) {
      newVoxel.blockId = targetBlockId;
      newVoxel.alginTo = alignToSide;
      map.setVoxelForPosition(newVoxel, cursor);
    }
  }

  private boolean isInsideMap(Vector3i cursor) {
    if (cursor.x < 0 || cursor.y < 0 || cursor.z < 0) {
      return false;
    }

    if (cursor.x >= map.getWidth() || cursor.y >= map.getHeight() || cursor.z >= map.getDepth()) {
      return false;
    }

    if (cursor.y > targetLevel && floodToLevel) {
      return false;
    }

    return true;
  }


  @Override
  public boolean runInThread() {
    return true;
  }
}
