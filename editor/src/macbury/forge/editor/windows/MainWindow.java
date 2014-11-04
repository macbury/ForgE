package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.TerrainToolsController;
import macbury.forge.editor.reloader.ShaderFileChangeListener;
import macbury.forge.editor.views.MainMenu;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame implements ForgEBootListener {
  private final LwjglAWTCanvas openGLCanvas;
  private final ForgE engine;
  private final ProjectController projectController;
  private final MainMenu mainMenu;
  private final MainToolbarController mainToolbarController;
  private final TerrainToolsController terrainToolsController;
  private final ProgressTaskDialog progressTaskDialog;
  private JPanel contentPane;
  private JPanel statusBarPanel;
  private JPanel openGlContainer;
  private JLabel statusRenderablesLabel;
  private JLabel statusFpsLabel;
  private JLabel statusMemoryLabel;
  private JToolBar mainToolbar;
  private JLabel mapCursorPositionLabel;
  private JLabel statusTriangleCountLabel;
  private JTree tree1;
  private JTabbedPane tabbedPane1;
  private JTextArea filterEvents;
  private JList list1;
  private JToolBar terrainToolsToolbar;
  private ShaderFileChangeListener shaderFileChangeListener;

  public MainWindow() {
    this.progressTaskDialog = new ProgressTaskDialog();
    setContentPane(contentPane);
    setSize(1360, 768);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    progressTaskDialog.setVisible(true);
    progressTaskDialog.setLocationRelativeTo(this);

    Config config            = new Config();
    config.generateWireframe = true;
    config.renderStaticOctree = false;
    config.renderBoundingBox = false;
    config.debug             = true;

    engine                   = new ForgE(config);

    engine.setBootListener(this);

    mainMenu     = new MainMenu();

    openGLCanvas = new LwjglAWTCanvas(engine);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    terrainToolsController   = new TerrainToolsController(terrainToolsToolbar);
    mainToolbarController    = new MainToolbarController(mainToolbar, mainMenu);

    projectController = new ProjectController();
    projectController.setMainWindow(this);
    projectController.setStatusLabel(statusFpsLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel, statusTriangleCountLabel);

    projectController.addOnMapChangeListener(mainMenu);
    projectController.addOnMapChangeListener(mainToolbarController);
    projectController.addOnMapChangeListener(terrainToolsController);
  }

  public void centreWindow() {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    this.setLocation(x, y);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    Gdx.graphics.setVSync(true);
    projectController.newMap();
    shaderFileChangeListener = new ShaderFileChangeListener();
    progressTaskDialog.setVisible(false);
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }

  @Override
  public void dispose() {
    super.dispose();
    if (shaderFileChangeListener != null) {
      shaderFileChangeListener.dispose();
    }
  }
}
