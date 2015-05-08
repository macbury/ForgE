package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import macbury.forge.blocks.Block;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.controllers.tools.inspector.properties.EditorScreenBeanInfo;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.TaskPropertySheetProvider;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;
import macbury.forge.voxel.VoxelMap;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by macbury on 17.03.15.
 */
public class ApplyCustomBrushChangeable extends TerrainCursorChangeable implements TaskPropertySheetProvider {
  private static final String TAG = "ApplyCustomBrushChangeable";
  private final Pixmap pixmap;
  private final int scale;

  private Array<BlockSave> oldBlocks;
  private Vector3i start  = new Vector3i();
  private Vector3i end    = new Vector3i();
  private Vector3i cursor = new Vector3i();

  public ApplyCustomBrushChangeable(VoxelMap map, int scale, BrushType brushType) {
    super(map);
    this.scale       = scale;
    this.pixmap      = brushType.getForScale(scale);
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
          //Gdx.app.log(TAG, cursor.toString());
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

  @Override
  public DefaultBeanBinder getPropertySheetBeanBinder(MapPropertySheet mapPropertySheet) {
    return new DefaultBeanBinder(this, mapPropertySheet, new BrushBeanInfo());
  }

  public int getScale() {
    return scale;
  }

  public class BrushBeanInfo extends BaseBeanInfo {

    private static final String BRUSH_CATEGORY = "Brush settings";

    public BrushBeanInfo() {
      super(ApplyCustomBrushChangeable.class);

      ExtendedPropertyDescriptor scaleProperty = addProperty("scale");
      scaleProperty.setDisplayName("Brush size");
      scaleProperty.setCategory(BRUSH_CATEGORY);
      scaleProperty.setPropertyEditorClass(ComboBoxPropertyEditor.class);
    }
  }

  public static class BrushType implements Disposable{
    public static final int MAX_SIZE = 60;
    private final String name;
    private ImageIcon imageIcon;
    private Pixmap basePixmap;
    private HashMap<Float, Pixmap> caches;

    public BrushType(FileHandle handle) {
      this.caches     = new HashMap<Float, Pixmap>();
      this.name       = handle.name();
      this.basePixmap = new Pixmap(handle);
      this.imageIcon  = new ImageIcon(handle.file().getAbsolutePath());
      Image tempIcon  = imageIcon.getImage().getScaledInstance(16, 16, Image.SCALE_FAST);
      imageIcon       = new ImageIcon(tempIcon);
    }

    public Pixmap getForScale(int size) {
      return getForScale((float)size / (float)MAX_SIZE);
    }

    public Pixmap getForScale(float scale) {
      if (!caches.containsKey(scale)) {
        int nh = Math.round(basePixmap.getHeight() * scale);
        int nw = Math.round(basePixmap.getWidth() * scale);
        Pixmap scaledPixmap = new Pixmap(nw, nh, Pixmap.Format.RGBA8888);
        Pixmap.setFilter(Pixmap.Filter.BiLinear);
        scaledPixmap.setColor(Color.WHITE);
        scaledPixmap.fill();
        scaledPixmap.drawPixmap(basePixmap, 0,0, basePixmap.getWidth(), basePixmap.getHeight(), 0, 0, scaledPixmap.getWidth(), scaledPixmap.getHeight());
        //PixmapIO.writePNG(new FileHandle("/tmp/scaled.png"), scaledPixmap);
        //basePixmap.drawPixmap(scaledPixmap, 0,0, 0, 0, basePixmap.getWidth(), basePixmap.getHeight());
        caches.put(scale, scaledPixmap);
      }
      return caches.get(scale);
    }

    @Override
    public void dispose() {
      basePixmap.dispose();
      for (Pixmap pixmap : caches.values()) {
        pixmap.dispose();
      }
    }

    public ImageIcon getImageIcon() {
      return imageIcon;
    }
  }
}
