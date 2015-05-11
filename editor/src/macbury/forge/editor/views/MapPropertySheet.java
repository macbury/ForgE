package macbury.forge.editor.views;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.Array;
import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import macbury.forge.editor.controllers.tools.inspector.editors.AdvComboBoxPropertyEditor;
import macbury.forge.editor.controllers.tools.inspector.editors.ColorEditor;

import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Color;

/**
 * Created by macbury on 06.11.14.
 */
public class MapPropertySheet extends PropertySheetPanel {
  private Array<Property> loseProperties = new Array<Property>();
  public MapPropertySheet() {
    super();

    this.setSortingProperties(false);
    this.setSortingCategories(true);
    this.setMode(PropertySheet.VIEW_AS_CATEGORIES);
    this.setBackground(Color.WHITE);
    this.setToolBarVisible(true);
    PropertyEditorRegistry editor = this.getTable().getEditorRegistry();
    editor.registerEditor(com.badlogic.gdx.graphics.Color.class, ColorEditor.class);

  }

  public Property find(String name) {
    for (Property property : getProperties()) {
      if (property.getName() == name) {
        return property;
      }
    }

    return null;
  }

  public void registerPropertyEditor(String propertyName, AbstractPropertyEditor propertyEditor) {
    Property property = find(propertyName);
    if (property == null) {
      throw new RuntimeException("Property not found: " + propertyName);
    }

    PropertyEditorRegistry editor = getEditorRegistry();
    editor.registerEditor(property, propertyEditor);
    loseProperties.add(property);
  }

  public void registerPropertyRenderer(String propertyName, TableCellRenderer renderer) {
    Property property = find(propertyName);
    if (property == null) {
      throw new RuntimeException("Property not found: " + propertyName);
    }

    getRendererRegistry().registerRenderer(property, renderer);
    loseProperties.add(property);
  }

  public void clearLosePropertyEditors() {
    PropertyEditorRegistry editor = getEditorRegistry();
    for (Property property : loseProperties) {
      editor.unregisterEditor(property);
      getRendererRegistry().unregisterRenderer(property);
    }
    loseProperties.clear();
  }
}
