package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.BlocksController;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.TerrainToolsController;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MapPropertySheet;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements ForgEBootListener {
  private static final String WINDOW_MAIN_NAME = "ForgE";
  private final BlocksController blocksController;
  private final DirectoryWatcher directoryWatcher;
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
  private JList blockList;
  private JPanel mapSettingsPanel;
  public JSplitPane mainSplitPane;

  public MainWindow() {
    super();
    Toolkit kit      = Toolkit.getDefaultToolkit();
    Image mainIcon   = kit.createImage(ClassLoader.getSystemResource("icons/main.ico"));
    setIconImage(mainIcon);
    setContentPane(mainContentPane);

    setSize(1360, 760);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle(null);
    setVisible(true);

    this.directoryWatcher                = new DirectoryWatcher();
    this.jobs                            = new JobManager();
    Config config                        = new Config();
    config.generateWireframe             = true;
    config.renderStaticOctree            = false;
    config.renderBoundingBox             = false;
    config.debug                         = true;

    engine                               = new ForgE(config);
    blocksController                     = new BlocksController(blockList, directoryWatcher, jobs);
    this.progressTaskDialog              = new ProgressTaskDialog();
    projectController                    = new ProjectController();
    mainMenu                             = new MainMenu(projectController);
    terrainToolsController               = new TerrainToolsController(terrainToolsToolbar);
    mainToolbarController                = new MainToolbarController(mainToolbar, mainMenu);

    MapPropertySheet inspectorSheetPanel = new MapPropertySheet();
    engine.addBootListener(this);
    engine.addBootListener(blocksController);
    openGLCanvas                         = new LwjglCanvas(engine);

    mapSettingsPanel.add(inspectorSheetPanel);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    projectController.setStatusLabel(statusFpsLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel, statusTriangleCountLabel);

    projectController.addOnMapChangeListener(mainMenu);
    projectController.addOnMapChangeListener(mainToolbarController);
    projectController.addOnMapChangeListener(terrainToolsController);
    projectController.addOnMapChangeListener(blocksController);
    invalidate();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    Gdx.graphics.setVSync(true);
    projectController.setMainWindow(this);
    directoryWatcher.start();
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
    directoryWatcher.dispose();
  }
}
