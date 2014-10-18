package macbury.forge.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.ProjectController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements ForgEBootListener {
  private final LwjglAWTCanvas openGLCanvas;
  private final ForgE engine;
  private final ProjectController projectController;
  private JPanel contentPane;
  private JButton wireframeButton;
  private JPanel openGlContainer;
  private JButton texturedButton;

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

    projectController = new ProjectController();
    projectController.setMainWindow(this);
    projectController.setWireframeButton(wireframeButton);
    projectController.setTextureButton(texturedButton);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    projectController.newMap();
  }

}
