package macbury.forge.editor.controllers.tools.terrain.properties;

import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import macbury.forge.editor.controllers.tools.inspector.editors.SpinnerPropertyEditor;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SingleBlockSelection;
import macbury.forge.editor.undo_redo.actions.TreeBuilderChangeable;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

import javax.swing.*;

/**
 * Created by macbury on 11.05.15.
 */
public class TreeChangeableProvider extends TerrainChangeableProvider<TreeBuilderChangeable> {
  private static final String BRUSH_CATEGORY = "Tree";
  private int height = TreeBuilderChangeable.MIN_HEIGHT;
  private int radius = 1;

  public TreeChangeableProvider() {
    super(TreeChangeableProvider.class);

    ExtendedPropertyDescriptor heightProperty = addProperty("height");
    heightProperty.setDisplayName("Height");
    heightProperty.setCategory(BRUSH_CATEGORY);

    ExtendedPropertyDescriptor radiusProperty = addProperty("radius");
    radiusProperty.setDisplayName("Radius");
    radiusProperty.setCategory(BRUSH_CATEGORY);
  }

  @Override
  protected AbstractSelection buildSelection() {
    return new SingleBlockSelection();
  }

  @Override
  public void getPropertyEditorsAndRenderers(MapPropertySheet mapPropertySheet) {
    mapPropertySheet.registerPropertyEditor("height", new TreeSizeSpinnerEditor());
    mapPropertySheet.registerPropertyEditor("radius", new TreeRadiusSpinnerEditor());
  }

  public static class TreeRadiusSpinnerEditor extends SpinnerPropertyEditor {
    public TreeRadiusSpinnerEditor() {
      super(Integer.class);
    }

    @Override
    public SpinnerModel getModel() {
      return new SpinnerNumberModel(1, 1, 5, 1);
    }
  }

  public static class TreeSizeSpinnerEditor extends SpinnerPropertyEditor {
    public TreeSizeSpinnerEditor() {
      super(Integer.class);
    }

    @Override
    public SpinnerModel getModel() {
      return new SpinnerNumberModel(TreeBuilderChangeable.MIN_HEIGHT, TreeBuilderChangeable.MIN_HEIGHT, TreeBuilderChangeable.MAX_HEIGHT, 1);
    }
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  @Override
  public TreeBuilderChangeable provide(ChunkMap map) {
    TreeBuilderChangeable changeable = new TreeBuilderChangeable(map, height, radius);
    changeable.setSelection(getSelection());
    return changeable;
  }
}
