package macbury.forge.editor.windows;

import javax.swing.*;
import java.awt.event.*;

public class MapCreationWindow extends JDialog {
  private final Listener listener;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField textField1;
  private JSpinner spinner1;
  private JSpinner spinner2;
  private JSpinner spinner3;
  private JComboBox comboBox1;

  public MapCreationWindow(Listener listener) {
    this.listener = listener;
    setContentPane(contentPane);
    setModal(true);

    getRootPane().setDefaultButton(buttonOK);
    setTitle("Create new map:");
    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    });

    buttonCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    });

// call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });
    pack();
    setResizable(false);

// call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  public void show(JFrame relativeTo) {
    this.setLocationRelativeTo(relativeTo);
    this.setVisible(true);
  }

  private void onOK() {
    if (valid()) {
      listener.onMapCreationSuccess(this);
      dispose();
    }

  }

  private boolean valid() {
    return true;
  }

  private void onCancel() {
// add your code here if necessary
    dispose();
  }

  public interface Listener {
    public void onMapCreationSuccess(MapCreationWindow window);
  }
}
