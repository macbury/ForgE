package macbury.forge.editor.views.property_editor;

import com.badlogic.gdx.utils.Array;
import macbury.forge.editor.views.property_editor.editors.AbstractPropertyValue;
import macbury.forge.editor.views.property_editor.editors.ReadOnlyPropertyValue;
import macbury.forge.editor.views.property_editor.editors.StringPropertyValue;
import macbury.forge.level.LevelEnv;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.beans.IntrospectionException;
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
    this.setRowSelectionAllowed(false);
    this.setColumnSelectionAllowed(false);

    setModel(modelData);

    TableColumnModel colmodel         = getColumnModel();
    TableColumn namecol               = colmodel.getColumn(0);
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.LEFT);
    namecol.setCellRenderer(renderer);

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

  class JavaBeanPropertyTableModel extends AbstractTableModel {
    private final Array<ObjectInspector> inspectors;
    private final Array<AbstractPropertyValue> array;

    public JavaBeanPropertyTableModel() {
      this.array = new Array<AbstractPropertyValue>();
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
      }
      return null;
    }

    public void add(AbstractPropertyValue propertyValueEditor) {
      this.array.add(propertyValueEditor);
      addNotify();
    }

    public void add(ObjectInspector inspector) {
      inspectors.add(inspector);
      inspector.insertInto(array);
    }
  }
}
