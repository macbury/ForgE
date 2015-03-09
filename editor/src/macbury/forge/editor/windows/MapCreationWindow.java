package macbury.forge.editor.windows;

import macbury.forge.level.LevelState;
import macbury.forge.voxel.ChunkMap;

import javax.swing.*;
import java.awt.event.*;

public class MapCreationWindow extends JDialog {
  private final Listener listener;
  private final LevelState state;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField mapNameTextField;
  private JSpinner widthSpinner;
  private JSpinner heightSpinner;
  private JSpinner depthSpinner;
  private JComboBox modeComboBox;

  public MapCreationWindow(LevelState state, Listener listener) {
    this.listener = listener;
    this.state    = state;
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

    widthSpinner.setModel(new MapSizeModel(state.getWidth()));
    heightSpinner.setModel(new MapSizeModel(state.getHeight()));
    depthSpinner.setModel(new MapSizeModel(state.getDepth()));

    mapNameTextField.setText(state.getName());

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
      listener.onMapCreationSuccess(this, state);
      dispose();
    }

  }

  private boolean valid() {
    state.setDepth((Integer)depthSpinner.getValue());
    state.setHeight((Integer)heightSpinner.getValue());
    state.setWidth((Integer)widthSpinner.getValue());
    state.setName(mapNameTextField.getText());
    return true;
  }

  private void onCancel() {
// add your code here if necessary
    dispose();
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }

  public interface Listener {
    public void onMapCreationSuccess(MapCreationWindow window, LevelState newState);
  }

  public class MapSizeModel extends SpinnerNumberModel {

    public MapSizeModel(int value) {
      super(value, ChunkMap.CHUNK_SIZE, 1000, ChunkMap.CHUNK_SIZE);
    }
  }
}
