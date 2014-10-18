package macbury.forge.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.graphics.batch.VoxelBatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ForgEBootListener, ActionListener {
  private final LwjglAWTCanvas openGLCanvas;
  private final ForgE engine;
  private JPanel contentPane;
  private JButton wireframeButton;
  private JPanel openGlContainer;
  private JButton texturedButton;
  private EditorScreen editorScreen;

  public MainWindow() {
    setContentPane(contentPane);
    pack();
    setSize(1360, 768);
    setVisible(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    Config config            = new Config();
    config.generateWireframe = true;
    engine                   = new ForgE(config);

    engine.setBootListener(this);

    openGLCanvas = new LwjglAWTCanvas(engine);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    setTitle("[ForgE] - New project");

    wireframeButton.addActionListener(this);
    texturedButton.addActionListener(this);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    this.editorScreen = new EditorScreen();
    engine.screens.set(editorScreen);
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == wireframeButton) {
      editorScreen.batch.setType(VoxelBatch.RenderType.Wireframe);
    } else {
      editorScreen.batch.setType(VoxelBatch.RenderType.Normal);
    }
  }
}
