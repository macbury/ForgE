package macbury.forge.editor.views.property_editor.editors;

import com.badlogic.gdx.Gdx;
import macbury.forge.editor.views.property_editor.PropertyCellEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by macbury on 10.04.15.
 */
public class StringPropertyValue extends AbstractPropertyValue {

  private static final String TAG = "StringPropertyValue";
  private JPanel panel;
  private JTextField textField;


  @Override
  protected Object getFinalValue() {
    return textField.getText();
  }

  @Override
  public Component getEditorComponent(final AbstractCellEditor editor) {
    System.out.print("Editing started \n");
    panel     = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    textField = new JTextField(50);

    textField.setText((String) getValue());
    textField.setBorder(BorderFactory.createLineBorder(Color.white));
    textField.setBackground(Color.white);
    panel.add(textField);
    panel.setBackground(Color.WHITE);
    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
          editor.stopCellEditing();
        }
      }
    });
    return textField;
  }

}
