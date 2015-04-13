package macbury.forge.editor.views.property_editor.editors;


import macbury.forge.editor.views.property_editor.PropertyCellEditor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by macbury on 10.04.15.
 */
public abstract class AbstractPropertyValue implements CellEditorListener {
  private final JLabel mainPreviewLabel;
  private String title;
  protected Object object;
  protected PropertyDescriptor descriptor;

  public AbstractPropertyValue() {
    this.mainPreviewLabel = new JLabel();
  }

  public void setProperty(PropertyDescriptor descriptor, Object object) {
    this.object = object;
    this.descriptor = descriptor;
    setTitle(descriptor.getName());
  }

  public Component getPreviewComponent() {
    Object val = getValue();
    if (val != null) {
      mainPreviewLabel.setText(getValue().toString());
    } else {
      mainPreviewLabel.setText("");
    }

    return mainPreviewLabel;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = StringUtils.join(
        StringUtils.splitByCharacterTypeCamelCase(StringUtils.capitalize(title)),
        ' '
    );
  }

  public Object getObject() {
    return object;
  }

  public Object getValue() {
    try {
      return descriptor.getReadMethod().invoke(object);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void editingStopped(ChangeEvent e) {
    saveValue();
  }

  protected void previewValue(Object valueToPreview) {
    try {
      descriptor.getWriteMethod().invoke(object, valueToPreview);
    } catch (IllegalAccessException e1) {
      e1.printStackTrace();
    } catch (InvocationTargetException e1) {
      e1.printStackTrace();
    }
  }

  protected void saveValue() {
    try {
      descriptor.getWriteMethod().invoke(object, getFinalValue());
    } catch (IllegalAccessException e1) {
      e1.printStackTrace();
    } catch (InvocationTargetException e1) {
      e1.printStackTrace();
    }
  }

  protected abstract Object getFinalValue();

  @Override
  public void editingCanceled(ChangeEvent e) {

  }

  public abstract Component getEditorComponent(AbstractCellEditor cell);
}
