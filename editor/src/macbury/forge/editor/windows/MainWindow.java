package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.TerrainToolsController;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.reloader.ShaderFileChangeListener;
import macbury.forge.editor.views.MainMenu;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame implements ForgEBootListener {
  private LwjglAWTCanvas openGLCanvas;
  private ForgE engine;
  private ProjectController projectController;
  private MainMenu mainMenu;
  private MainToolbarController mainToolbarController;
  private TerrainToolsController terrainToolsController;
  public final ProgressTaskDialog progressTaskDialog;
  public final JobManager jobs;
  private JPanel contentPane;
  private JPanel statusBarPanel;
  public JPanel openGlContainer;
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
  public JProgressBar jobProgressBar;
  private JList list2;
  private ShaderFileChangeListener shaderFileChangeListener;

  public MainWindow() {
    this.progressTaskDialog = new ProgressTaskDialog();
    this.jobs               = new JobManager();
    setContentPane(contentPane);
    setSize(1360, 768);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);


    Config config             = new Config();
    config.generateWireframe  = true;
    config.renderStaticOctree = false;
    config.renderBoundingBox  = false;
    config.debug              = true;

    engine                    = new ForgE(config);
    engine.setBootListener(this);

    mainMenu                  = new MainMenu();

    openGLCanvas              = new LwjglAWTCanvas(engine);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    terrainToolsController   = new TerrainToolsController(terrainToolsToolbar);
    mainToolbarController    = new MainToolbarController(mainToolbar, mainMenu);

    projectController         = new ProjectController();
    projectController.setStatusLabel(statusFpsLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel, statusTriangleCountLabel);

    projectController.addOnMapChangeListener(mainMenu);
    projectController.addOnMapChangeListener(mainToolbarController);
    projectController.addOnMapChangeListener(terrainToolsController);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    Gdx.graphics.setVSync(true);
    shaderFileChangeListener  = new ShaderFileChangeListener();
    projectController.setMainWindow(this);
    projectController.newMap();
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
