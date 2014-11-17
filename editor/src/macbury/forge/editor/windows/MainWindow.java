package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTInput;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.*;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.views.ImagePanel;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MapPropertySheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class MainWindow extends JFrame implements ForgEBootListener, FocusListener, WindowFocusListener {
  private static final String WINDOW_MAIN_NAME = "ForgE";
  private final BlocksController blocksController;
  private final DirectoryWatcher directoryWatcher;
  private final ShadersController shadersController;
  private final GdxSwingInputProcessor inputProcessor;
  private final LwjglAWTInput input;
  private LwjglAWTCanvas openGLCanvas;
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
  private JTree mapTree;
  private JTabbedPane toolsPane;
  private JList list1;
  private JToolBar terrainToolsToolbar;
  public JProgressBar jobProgressBar;
  private JList blockList;
  private JPanel mapSettingsPanel;
  private JPanel panelPrimaryBlock;
  private JPanel panelSecondaryBlock;
  public JPanel mainSplitPane;
  private boolean bruteFocus;

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

    this.inputProcessor                  = new GdxSwingInputProcessor();
    this.directoryWatcher                = new DirectoryWatcher();
    this.jobs                            = new JobManager();
    Config config                        = new Config();
    config.generateWireframe             = true;
    config.renderStaticOctree            = false;
    config.renderBoundingBox             = false;
    config.debug                         = true;

    engine                               = new ForgE(config);
    blocksController                     = new BlocksController(blockList, directoryWatcher, jobs, (ImagePanel)panelPrimaryBlock, (ImagePanel)panelSecondaryBlock);
    this.progressTaskDialog              = new ProgressTaskDialog();
    projectController                    = new ProjectController();
    mainMenu                             = new MainMenu(projectController);
    terrainToolsController               = new TerrainToolsController(terrainToolsToolbar, blocksController, inputProcessor);
    mainToolbarController                = new MainToolbarController(mainToolbar, mainMenu, inputProcessor);
    shadersController                    = new ShadersController(directoryWatcher);
    MapPropertySheet inspectorSheetPanel = new MapPropertySheet();

    engine.addBootListener(this);
    engine.addBootListener(blocksController);

    openGLCanvas                         = new LwjglAWTCanvas(engine);
    input                                = (LwjglAWTInput)openGLCanvas.getInput();

    mainContentPane.addMouseListener(input);
    mainContentPane.addKeyListener(input);
    mainContentPane.setFocusTraversalKeysEnabled(false);
    mainContentPane.setFocusable(true);
    mainContentPane.grabFocus();
    mainContentPane.addFocusListener(this);

    mapSettingsPanel.add(inspectorSheetPanel);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    projectController.setStatusLabel(statusFpsLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel, statusTriangleCountLabel);

    projectController.addOnMapChangeListener(mainMenu);
    projectController.addOnMapChangeListener(mainToolbarController);
    projectController.addOnMapChangeListener(terrainToolsController);
    projectController.addOnMapChangeListener(blocksController);

    invalidate();
    addWindowFocusListener(this);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    Gdx.graphics.setVSync(true);
    mainSplitPane.setVisible(false);
    ForgE.input.addProcessor(inputProcessor);
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


  private void createUIComponents() {
    panelSecondaryBlock = new ImagePanel();
    panelPrimaryBlock   = new ImagePanel();
  }

  @Override
  public void focusGained(FocusEvent e) {

  }

  @Override
  public void focusLost(FocusEvent e) {
    if (bruteFocus) {
      //mainContentPane.grabFocus();
    }

    //mainContentPane.setFocusable(true);
  }

  @Override
  public void windowGainedFocus(WindowEvent e) {
    bruteFocus = true;
    //mainContentPane.grabFocus();
  }

  @Override
  public void windowLostFocus(WindowEvent e) {
    bruteFocus = false;
  }
}
