package macbury.forge.editor.windows;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MoreToolbarButton;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame implements ForgEBootListener {
  private final LwjglAWTCanvas openGLCanvas;
  private final ForgE engine;
  private final ProjectController projectController;
  private final MainMenu mainMenu;
  private final MoreToolbarButton moreButton;
  private JPanel contentPane;
  private JButton wireframeButton;
  private JPanel openGlContainer;
  private JButton texturedButton;
  private JPanel statusBarPanel;
  private JLabel statusRenderablesLabel;
  private JLabel statusFpsLabel;
  private JLabel statusMemoryLabel;
  private JButton button1;
  private JButton button2;
  private JToolBar mainToolbar;

  public MainWindow() {
    setContentPane(contentPane);
    setSize(1360, 768);
    setVisible(true);
    //setExtendedState(JFrame.MAXIMIZED_BOTH);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    Config config            = new Config();
    config.generateWireframe = true;
    config.debug             = true;
    engine                   = new ForgE(config);

    engine.setBootListener(this);

    mainMenu     = new MainMenu();
    moreButton   = new MoreToolbarButton(mainMenu);
    openGLCanvas = new LwjglAWTCanvas(engine);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    projectController = new ProjectController();
    projectController.setMainWindow(this);
    projectController.setMainMenu(mainMenu);
    projectController.setWireframeButton(wireframeButton);
    projectController.setTextureButton(texturedButton);
    projectController.setStatusLabel(statusFpsLabel, statusMemoryLabel, statusRenderablesLabel);

    mainToolbar.add(Box.createHorizontalGlue(),4);
    mainToolbar.add(moreButton, 0);
    //setJMenuBar(mainMenu);

    //pack();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    projectController.newMap();
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }
}
