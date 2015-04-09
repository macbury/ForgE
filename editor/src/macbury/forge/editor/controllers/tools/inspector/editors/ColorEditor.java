package macbury.forge.editor.controllers.tools.inspector.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.badlogic.gdx.graphics.Color;
import com.bric.swing.ColorPicker;
import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.renderer.ColorCellRenderer;
import icons.Utils;
import macbury.forge.editor.windows.MainWindow;


/**
 * Color editor.
 *
 * @author Bartosz Firyn (SarXos)
 */
public class ColorEditor extends AbstractPropertyEditor {

  protected class ColorEditorComponent extends JPanel {

    private static final long serialVersionUID = 411604969565728959L;

    private JLabel label = null;
    private JButton button = null;

    public ColorEditorComponent() {

      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

      label = new JLabel();
      label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
      label.setAlignmentX(JTextField.LEFT_ALIGNMENT);
      label.setBackground(UIManager.getColor("Table.selectionBackground"));
      label.setForeground(UIManager.getColor("Table.selectionForeground"));

      Dimension size = new Dimension(24, 15);

      Image pencil = null;
      try {
        pencil = ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/edit.png"));
      } catch (IOException e) {
        throw new RuntimeException("Cannot load resource", e);
      }

      button = new JButton();
      button.setPreferredSize(size);
      button.setSize(size);
      button.setMaximumSize(size);
      button.setMinimumSize(size);
      button.setAction(new AbstractAction("", new ImageIcon(pencil)) {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
          selectColor();
        }
      });

      JPanel left = new JPanel();
      left.setLayout(new BorderLayout(0, 0));
      left.add(label);
      left.setAlignmentX(JPanel.LEFT_ALIGNMENT);

      JPanel right = new JPanel();
      right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
      right.add(button);
      right.setPreferredSize(new Dimension(size.width, 25));
      right.setAlignmentX(JPanel.RIGHT_ALIGNMENT);

      add(left, BorderLayout.CENTER);
      add(right, BorderLayout.EAST);
    }

    public void setColor(Color color) {
      label.setText(color.toString());
      ColorEditor.this.color = color;
    }
  }

  private ColorEditorComponent colorEditor = null;
  private Color color;

  public ColorEditor() {
    editor = colorEditor = new ColorEditorComponent();
  }

  @Override
  public Object getValue() {
    return color;
  }

  @Override
  public void setValue(Object value) {
    colorEditor.setColor((Color) value);
  }

  protected void selectColor() {
    java.awt.Color selectedColor = ColorPicker.showDialog(MainWindow.current, Utils.fromLibgdx(color));

    if (selectedColor != null) {
      Color newColor = Utils.fromAwt(selectedColor);

      colorEditor.setColor(newColor);

      firePropertyChange(color, newColor);

    }
  }

  protected void selectNull() {
    //Color oldColor = color;
    //colorEditor.setColor(null);
    //firePropertyChange(oldColor, null);
  }
}