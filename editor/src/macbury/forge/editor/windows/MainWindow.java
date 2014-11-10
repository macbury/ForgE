package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.TerrainToolsController;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.reloader.ShaderFileChangeListener;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MapPropertySheet;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements ForgEBootListener {
  private static final String WINDOW_MAIN_NAME = "ForgE";
  private LwjglCanvas openGLCanvas;
  private ForgE engine;
  private ProjectController projectController;
  private MainMenu mainMenu;
  private MainToolbarController mainToolbarController;
  private TerrainToolsController terrainToolsController;
  public final ProgressTaskDialog progressTaskDialog;
  public final JobManager jobs;
  private JPanel mainContentPane;
  private JPanel statusBarPanel;


  public JPanel openGlContainer;
  private JLabel statusRenderablesLabel;
  private JLabel statusFpsLabel;
  private JLabel statusMemoryLabel;
  private JToolBar mainToolbar;
  private JLabel mapCursorPositionLabel;
  private JLabel statusTriangleCountLabel;
  private JTree tree1;
  private JTabbedPane toolsPane;
  private JTextArea filterEvents;
  private JList list1;
  private JToolBar terrainToolsToolbar;
  public JProgressBar jobProgressBar;
  private JList list2;
  private JPanel mapSettingsPanel;
  public JSplitPane mainSplitPane;
  private ShaderFileChangeListener shaderFileChangeListener;

  public MainWindow() {
    super();
    setContentPane(mainContentPane);

    setSize(1360, 760);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle(null);
    setVisible(true);

    this.jobs                            = new JobManager();
    Config config                        = new Config();
    config.generateWireframe             = true;
    config.renderStaticOctree            = false;
    config.renderBoundingBox             = false;
    config.debug                         = true;

    engine                               = new ForgE(config);
    engine.setBootListener(this);
    this.progressTaskDialog              = new ProgressTaskDialog();
    projectController                    = new ProjectController();
    mainMenu                             = new MainMenu(projectController);
    terrainToolsController               = new TerrainToolsController(terrainToolsToolbar);
    mainToolbarController                = new MainToolbarController(mainToolbar, mainMenu);
    MapPropertySheet inspectorSheetPanel = new MapPropertySheet();
    openGLCanvas                         = new LwjglCanvas(engine);
    mapSettingsPanel.add(inspectorSheetPanel);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

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
    //projectController.newMap();
  }

  @Override
  public void setTitle(String title) {
    if (title == null || title.length() == 0) {
      super.setTitle(WINDOW_MAIN_NAME);
    } else {
      super.setTitle(WINDOW_MAIN_NAME + " - " + title);
    }
  }


  @Override
  public void dispose() {
    super.dispose();
    if (shaderFileChangeListener != null) {
      shaderFileChangeListener.dispose();
    }
  }
}
