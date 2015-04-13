package macbury.forge.editor.views.property_editor;

import macbury.forge.editor.views.property_editor.editors.AbstractPropertyValue;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by macbury on 13.04.15.
 */
public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor {
  private AbstractPropertyValue currentCellEditor;
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (AbstractPropertyValue.class.isInstance(value)) {
      AbstractPropertyValue abstractPropertyValue = (AbstractPropertyValue)value;
      addCellEditorListener(abstractPropertyValue);
      if (currentCellEditor != null) {
        removeCellEditorListener(currentCellEditor);
      }
      currentCellEditor = abstractPropertyValue;
      return abstractPropertyValue.getEditorComponent(this);
    } else {
      throw new RuntimeException("No");
    }
  }

  @Override
  public Object getCellEditorValue() {
    return null;
  }
}
