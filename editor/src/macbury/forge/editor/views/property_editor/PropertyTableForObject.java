package macbury.forge.editor.views.property_editor;

import com.badlogic.gdx.utils.Array;
import macbury.forge.editor.views.property_editor.editors.AbstractPropertyValue;
import macbury.forge.editor.views.property_editor.editors.ReadOnlyPropertyValue;
import macbury.forge.editor.views.property_editor.editors.StringPropertyValue;
import macbury.forge.level.LevelEnv;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.*;
import java.awt.*;
import java.beans.IntrospectionException;
import java.util.EventObject;
import java.util.HashMap;

/**
 * Created by macbury on 10.04.15.
 */
public class PropertyTableForObject extends JTable {
  private final JavaBeanPropertyTableModel modelData;
  private HashMap<Class, Class<? extends AbstractPropertyValue>> editorMappings;

  public PropertyTableForObject(Object objectToInspect) {
    super();

    this.editorMappings = new HashMap<Class, Class<? extends AbstractPropertyValue>>();
    this.modelData      = new JavaBeanPropertyTableModel();
    this.setRowSelectionAllowed(true);
    this.setColumnSelectionAllowed(false);

    setModel(modelData);

    TableColumnModel colmodel         = getColumnModel();
    TableColumn namecol               = colmodel.getColumn(0);
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.LEFT);
    namecol.setCellRenderer(renderer);
    namecol.setResizable(false);

    TableColumn valueCol              = colmodel.getColumn(1);
    valueCol.setCellRenderer(modelData);
    valueCol.setCellEditor(new PropertyCellEditor());
    valueCol.setResizable(true);
    //setDefaultRenderer(Object.class, modelData);
    setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    //table.setShowVerticalLines(false);

    register(String.class, StringPropertyValue.class);
    addToInspector(objectToInspect);
  }

  public void register(Class klass, Class<? extends AbstractPropertyValue> editorKlass) {
    editorMappings.put(klass, editorKlass);
  }

  public Class<? extends AbstractPropertyValue> getEditorClassFor(Class klass) {
    if (editorMappings.containsKey(klass)){
      return editorMappings.get(klass);
    } else {
      return ReadOnlyPropertyValue.class;
    }
  }

  private void addToInspector(Object objectToInspect) {
    try {
      ObjectInspector inspector = new ObjectInspector(this, objectToInspect);
      modelData.add(inspector);
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
  }

  class JavaBeanPropertyTableModel extends AbstractTableModel implements TableCellRenderer {
    private final Array<ObjectInspector> inspectors;
    private final Array<AbstractPropertyValue> array;

    public JavaBeanPropertyTableModel() {
      this.array      = new Array<AbstractPropertyValue>();
      this.inspectors = new Array<ObjectInspector>();
    }

    @Override
    public int getRowCount() {
      return array.size;
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      AbstractPropertyValue propertyValue = array.get(rowIndex);
      if (columnIndex == 0) {
        return propertyValue.getTitle();
      } else {
        return propertyValue;
      }
    }

    public void add(AbstractPropertyValue propertyValueEditor) {
      this.array.add(propertyValueEditor);
      addNotify();
    }

    public void add(ObjectInspector inspector) {
      inspectors.add(inspector);
      inspector.insertInto(array);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      if (columnIndex == 1) {
        return !ReadOnlyPropertyValue.class.isInstance(array.get(rowIndex));
      } else {
        return false;
      }
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      if (column == 1) {
        if (AbstractPropertyValue.class.isInstance(value)) {
          AbstractPropertyValue abstractPropertyValue = (AbstractPropertyValue)value;

          return abstractPropertyValue.getPreviewComponent();
        } else {
          throw new RuntimeException("No");
        }
      } else {
        JLabel label = new JLabel(value.toString());
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        return label;
      }

    }


  }

}
