package macbury.forge.editor.views.property_editor.editors;

import macbury.forge.editor.views.property_editor.PropertyCellEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * Created by macbury on 10.04.15.
 */
public class ReadOnlyPropertyValue extends AbstractPropertyValue {

  @Override
  public void editingStopped(ChangeEvent e) {

  }

  @Override
  protected Object getFinalValue() {
    return null;
  }

  @Override
  public Component getEditorComponent(AbstractCellEditor cell) {
    return null;
  }

}
