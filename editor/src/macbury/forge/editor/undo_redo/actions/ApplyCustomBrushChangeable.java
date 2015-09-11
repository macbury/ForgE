package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import macbury.forge.blocks.Block;
import macbury.forge.editor.controllers.tools.terrain.properties.CustomBrushChangeableProvider;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 17.03.15.
 */
public class ApplyCustomBrushChangeable extends TerrainCursorChangeable {
  private static final String TAG = "ApplyCustomBrushChangeable";
  private Array<BlockSave> oldBlocks;
  private Vector3i start  = new Vector3i();
  private Vector3i end    = new Vector3i();
  private Vector3i cursor = new Vector3i();
  private Pixmap pixmap;


  public ApplyCustomBrushChangeable(VoxelMap map) {
    super(map);

    this.oldBlocks   = new Array<BlockSave>();

  }

  @Override
  public void revert() {
    for(BlockSave save : oldBlocks) {
      save.applyTo(map);
    }
  }

  private void saveBlock(Vector3i cursor) {
    BlockSave save = new BlockSave(cursor, map.getVoxelForPosition(cursor));
    if (!oldBlocks.contains(save, false)) {
      oldBlocks.add(save);
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
    int hw = pixmap.getWidth()/2;
    int hh = pixmap.getHeight()/2;

    start.set(from).sub(hw, 0, hh);
    end.set(from).add(hw, 0, hh);

    for (int x = start.x; x < end.x; x++) {
      for (int z = start.z; z < end.z; z++) {
        int c = pixmap.getPixel(x-start.x,z-start.z);

        if (c >= 0) {
          cursor.set(x, from.y, z);
          //ForgE.log(TAG, cursor.toString());
          while(true) {
            if (map.isSolid(cursor.x, cursor.y-1, cursor.z) || map.isOutOfBounds(cursor.x, cursor.y-1, cursor.z)) {
              break;
            } else {
              cursor.y -= 1;
            }

          }
          putBlock(getBlockBySelectionMouse(), cursor);
        }
      }
    }
  }


  public void setScaleAndBrush(int scale, CustomBrushChangeableProvider.BrushType brushType) {
    this.pixmap = brushType.getForScale(scale);
  }

}
