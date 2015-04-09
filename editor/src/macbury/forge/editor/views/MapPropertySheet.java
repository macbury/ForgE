package macbury.forge.editor.views;

import com.badlogic.gdx.graphics.*;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import macbury.forge.editor.controllers.tools.inspector.editors.ColorEditor;

import java.awt.*;
import java.awt.Color;

/**
 * Created by macbury on 06.11.14.
 */
public class MapPropertySheet extends PropertySheetPanel {

  public MapPropertySheet() {
    super();

    this.setSortingProperties(false);
    this.setSortingCategories(true);
    this.setMode(PropertySheet.VIEW_AS_CATEGORIES);
    this.setBackground(Color.WHITE);
    this.setToolBarVisible(false);
    PropertyEditorRegistry editor = this.getTable().getEditorRegistry();
    editor.registerEditor(com.badlogic.gdx.graphics.Color.class, ColorEditor.class);
  }
}
