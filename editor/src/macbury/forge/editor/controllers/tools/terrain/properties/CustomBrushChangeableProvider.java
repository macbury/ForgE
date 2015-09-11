package macbury.forge.editor.controllers.tools.terrain.properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.Disposable;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.tools.inspector.editors.AdvComboBoxPropertyEditor;
import macbury.forge.editor.controllers.tools.inspector.editors.SpinnerPropertyEditor;
import macbury.forge.editor.controllers.tools.inspector.renderers.BrushImageIconRenderer;
import macbury.forge.editor.controllers.tools.terrain.BrushListRenderer;
import macbury.forge.editor.controllers.tools.terrain.BrushTypeModel;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.BrushSelection;
import macbury.forge.editor.undo_redo.actions.ApplyCustomBrushChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by macbury on 11.05.15.
 */
public class CustomBrushChangeableProvider extends TerrainChangeableProvider<ApplyCustomBrushChangeable> {
  private static final String TAG = "CustomBrushChangeableProvider";
  private final BrushTypeModel brushTypeModel;
  private Integer scale;
  private BrushType brushType;
  private static final String BRUSH_CATEGORY = "Brush";

  public CustomBrushChangeableProvider() {
    super(CustomBrushChangeableProvider.class);
    this.brushTypeModel = new BrushTypeModel();
    brushTypeModel.reload();
    this.brushType      = brushTypeModel.getElementAt(0);
    this.scale          = new Integer(1);

    ExtendedPropertyDescriptor scaleProperty = addProperty("scale");
    scaleProperty.setDisplayName("Size");
    scaleProperty.setCategory(BRUSH_CATEGORY);

    ExtendedPropertyDescriptor typeProperty = addProperty("brushType");
    typeProperty.setDisplayName("Type");
    typeProperty.setCategory(BRUSH_CATEGORY);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new BrushSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {
    mapPropertySheet.registerPropertyEditor("brushType", new AdvComboBoxPropertyEditor(brushTypeModel, new BrushListRenderer()));
    mapPropertySheet.registerPropertyRenderer("brushType", new BrushImageIconRenderer());
    mapPropertySheet.registerPropertyEditor("scale", new BrushSizeSpinnerEditor());
  }

  public Integer getScale() {
    return scale;
  }

  public void setScale(Integer scale) {
    this.scale = scale;
  }

  public BrushType getBrushType() {
    return brushType;
  }

  public void setBrushType(BrushType brushType) {
    this.brushType = brushType;
  }

  @Override
  public ApplyCustomBrushChangeable provide(ChunkMap map) {
    ApplyCustomBrushChangeable applyCustomBrushChangeable = new ApplyCustomBrushChangeable(map);
    applyCustomBrushChangeable.setScaleAndBrush(scale, brushType);
    applyCustomBrushChangeable.setSelection(getSelection());
    ForgE.log(TAG, "Setting: " + scale);
    return applyCustomBrushChangeable;
  }


  public static class BrushSizeSpinnerEditor extends SpinnerPropertyEditor {
    public BrushSizeSpinnerEditor() {
      super(Integer.class);
    }

    @Override
    public SpinnerModel getModel() {
      return new SpinnerNumberModel(1, 1, BrushType.MAX_SIZE, 1);
    }
  }


  public static class BrushType implements Disposable {
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
        scaledPixmap.setColor(com.badlogic.gdx.graphics.Color.WHITE);
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
