package macbury.forge.editor.controllers.tools.inspector.renderers;

import macbury.forge.editor.controllers.tools.terrain.properties.CustomBrushChangeableProvider;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

/**
 * Created by macbury on 11.05.15.
 */
public class BrushImageIconRenderer extends JLabel implements TableCellRenderer, Serializable {

  private static final long serialVersionUID = -11256720632412870L;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    CustomBrushChangeableProvider.BrushType brushType = (CustomBrushChangeableProvider.BrushType)value;
    if (brushType != null) {
      this.setText("");
      this.setIcon(brushType.getImageIcon());
    }
    return this;
  }
}
