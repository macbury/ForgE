package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTInput;
import com.badlogic.gdx.utils.Json;
import com.ezware.dialog.task.TaskDialogs;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.*;
import macbury.forge.editor.controllers.maps.MapTreeController;
import macbury.forge.editor.controllers.resources.ResourcesController;
import macbury.forge.editor.controllers.tools.events.EventsController;
import macbury.forge.editor.controllers.tools.terrain.TerrainToolsController;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.views.ImagePanel;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MapPropertySheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ForgEBootListener, FocusListener, WindowFocusListener, Thread.UncaughtExceptionHandler, WindowListener {
  private static final String WINDOW_MAIN_NAME = "ForgE";
  private final BlocksController blocksController;
  private final DirectoryWatcher directoryWatcher;
  public final ShadersController shadersController;
  private final GdxSwingInputProcessor inputProcessor;
  private final LwjglAWTInput input;
  private final MapTreeController mapTreeController;
  private final EventsController eventsToolsController;
  public static MainWindow current;
  private final PlayerController playerController;
  private final DockFramesController dockFrameController;
  public final CodeEditorWindow codeEditorWindow;
  private final CodeEditorController codeEditorController;
  public final ResourcesController resourcesController;
  private final PostProcessingController postProcessingController;
  private LwjglAWTCanvas openGLCanvas;
  private ForgE engine;
  private ProjectController projectController;
  private MainMenu mainMenu;
  private MainToolbarController mainToolbarController;
  private TerrainToolsController terrainToolsController;
  public final ProgressTaskDialog progressTaskDialog;
  public final JobManager jobs;
  public JPanel mainContentPane;
  private JPanel statusBarPanel;
  public JPanel openGlContainer;
  private JLabel statusRenderablesLabel;
  private JLabel statusFpsLabel;
  private JLabel statusMemoryLabel;
  private JToolBar mainToolbar;
  private JLabel mapCursorPositionLabel;
  private JLabel statusTriangleCountLabel;
  private JTree mapTree;
  public JTabbedPane toolsPane;
  private JToolBar terrainToolsToolbar;
  public JProgressBar jobProgressBar;
  private JList blockList;
  public JPanel objectInspectorContainerPanel;
  private JPanel panelPrimaryBlock;
  private JPanel panelSecondaryBlock;
  public JPanel mainSplitPane;
  private JPanel contentTabsContainer;
  private JTree tree1;
  private JTree tree2;
  public JPanel terrainPanel;
  public JScrollPane mapTreeScroll;
  public MapPropertySheet terrainInspectorPanel;
  public JScrollPane logsScrollView;
  private JComboBox brushTypeComboBox;
  private JSpinner brushSizeSpinner;
  private JPanel brushPropertiesPanel;
  private JTextArea logArea;
  private boolean bruteFocus;

  public MainWindow() {
    super();
    $$$setupUI$$$();
    this.current = this;
    Thread.setDefaultUncaughtExceptionHandler(this);
    setContentPane(mainContentPane);
    mainContentPane.remove(mainSplitPane);
    this.directoryWatcher     = new DirectoryWatcher();
    shadersController         = new ShadersController(directoryWatcher, this);
    terrainInspectorPanel     = new MapPropertySheet();
    this.codeEditorWindow     = new CodeEditorWindow();
    this.resourcesController  = new ResourcesController();
    this.dockFrameController  = new DockFramesController(this);
    this.codeEditorController = new CodeEditorController(codeEditorWindow);
    setResizable(true);
    mainContentPane.setVisible(false);
    setSize(1360, 760);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


    setTitle(null);
    setVisible(true);

    this.inputProcessor = new GdxSwingInputProcessor();

    this.jobs = new JobManager();
    Config config = Config.load("editor");
    engine = new ForgE(config);

    postProcessingController = new PostProcessingController(directoryWatcher);
    blocksController = new BlocksController(blockList, directoryWatcher, jobs, (ImagePanel) panelPrimaryBlock, (ImagePanel) panelSecondaryBlock);
    this.progressTaskDialog = new ProgressTaskDialog();
    projectController = new ProjectController();
    mainMenu = new MainMenu(projectController, blocksController, dockFrameController, shadersController, postProcessingController);
    eventsToolsController = new EventsController(this);
    terrainToolsController = new TerrainToolsController(terrainToolsToolbar, blocksController, inputProcessor, terrainInspectorPanel);
    playerController = new PlayerController(projectController, jobs);
    mainToolbarController = new MainToolbarController(projectController, mainToolbar, mainMenu, inputProcessor, playerController, codeEditorController);

    mapTreeController = new MapTreeController(mapTree, projectController);

    engine.addBootListener(this);
    engine.addBootListener(blocksController);
    engine.addBootListener(mapTreeController);
    engine.addBootListener(terrainToolsController);
    engine.addBootListener(codeEditorController);
    openGLCanvas = new LwjglAWTCanvas(engine);
    input = (LwjglAWTInput) openGLCanvas.getInput();
    mainContentPane.addMouseListener(input);
    mainContentPane.addKeyListener(input);
    mainContentPane.setFocusTraversalKeysEnabled(false);
    mainContentPane.setFocusable(true);
    mainContentPane.grabFocus();
    mainContentPane.addFocusListener(this);

    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    projectController.setStatusLabel(statusFpsLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel, statusTriangleCountLabel);

    mainToolbarController.editorModeListeners.addListener(terrainToolsController);
    mainToolbarController.editorModeListeners.addListener(dockFrameController);
    mainToolbarController.editorModeListeners.addListener(eventsToolsController);
    projectController.addOnMapChangeListener(terrainToolsController);
    projectController.addOnMapChangeListener(mainMenu);
    projectController.addOnMapChangeListener(mainToolbarController);
    projectController.addOnMapChangeListener(postProcessingController);
    projectController.addOnMapChangeListener(blocksController);
    projectController.addOnMapChangeListener(mapTreeController);
    projectController.addOnMapChangeListener(eventsToolsController);
    mainContentPane.setVisible(true);
    invalidate();

    addWindowListener(this);
    addWindowFocusListener(this);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    resourcesController.refresh();
    Gdx.graphics.setVSync(true);
    mainSplitPane.setVisible(false);
    ForgE.input.addProcessor(inputProcessor);
    projectController.setMainWindow(this);
    directoryWatcher.start();
    mainContentPane.setVisible(true);
    projectController.tryOpenLastMap();
    mainMenu.createAllMenus();
    ForgE.shaders.addOnShaderReloadListener(shadersController);
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
    panelPrimaryBlock = new ImagePanel();
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
    Gdx.graphics.setContinuousRendering(true);
  }



  @Override
  public void windowLostFocus(WindowEvent e) {
    bruteFocus = false;
    Gdx.graphics.setContinuousRendering(false);
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    final Throwable err = e;
    e.printStackTrace();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        TaskDialogs.showException(err);
        MainWindow.this.dispose();
        System.exit(0);
      }
    });

  }


  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainContentPane = new JPanel();
    mainContentPane.setLayout(new BorderLayout(0, 0));
    statusBarPanel = new JPanel();
    statusBarPanel.setLayout(new GridLayoutManager(1, 11, new Insets(5, 5, 5, 10), -1, -1));
    statusBarPanel.setMinimumSize(new Dimension(646, 32));
    mainContentPane.add(statusBarPanel, BorderLayout.SOUTH);
    statusRenderablesLabel = new JLabel();
    statusRenderablesLabel.setText("Voxels: 6");
    statusBarPanel.add(statusRenderablesLabel, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(62, 26), null, 0, false));
    final Spacer spacer1 = new Spacer();
    statusBarPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(14, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator1, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    statusMemoryLabel = new JLabel();
    statusMemoryLabel.setText("Memory: 6GB/10GB");
    statusBarPanel.add(statusMemoryLabel, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(138, 26), null, 0, false));
    statusFpsLabel = new JLabel();
    statusFpsLabel.setText("FPS: 60s");
    statusBarPanel.add(statusFpsLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(59, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator3 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator3, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    mapCursorPositionLabel = new JLabel();
    mapCursorPositionLabel.setText("0x0x0");
    statusBarPanel.add(mapCursorPositionLabel, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(46, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator4 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator4, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    statusTriangleCountLabel = new JLabel();
    statusTriangleCountLabel.setText("Triangle count: 0");
    statusBarPanel.add(statusTriangleCountLabel, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(112, 26), null, 0, false));
    jobProgressBar = new JProgressBar();
    jobProgressBar.setIndeterminate(true);
    statusBarPanel.add(jobProgressBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(146, 26), null, 0, false));
    mainToolbar = new JToolBar();
    mainToolbar.setFloatable(false);
    mainContentPane.add(mainToolbar, BorderLayout.NORTH);
    mainSplitPane = new JPanel();
    mainSplitPane.setLayout(new BorderLayout(0, 0));
    mainContentPane.add(mainSplitPane, BorderLayout.CENTER);
    final JSplitPane splitPane1 = new JSplitPane();
    splitPane1.setContinuousLayout(true);
    splitPane1.setDividerLocation(486);
    splitPane1.setOrientation(0);
    splitPane1.setPreferredSize(new Dimension(320, 527));
    splitPane1.setResizeWeight(0.6);
    mainSplitPane.add(splitPane1, BorderLayout.WEST);
    contentTabsContainer = new JPanel();
    contentTabsContainer.setLayout(new BorderLayout(0, 0));
    splitPane1.setLeftComponent(contentTabsContainer);
    toolsPane = new JTabbedPane();
    contentTabsContainer.add(toolsPane, BorderLayout.CENTER);
    mapTreeScroll = new JScrollPane();
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/node_root.png")), mapTreeScroll);
    mapTree = new JTree();
    mapTree.setRootVisible(true);
    mapTree.setShowsRootHandles(true);
    mapTree.putClientProperty("JTree.lineStyle", "");
    mapTreeScroll.setViewportView(mapTree);
    terrainPanel = new JPanel();
    terrainPanel.setLayout(new BorderLayout(0, 0));
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/soil_layers.png")), terrainPanel, "Terrain");
    terrainToolsToolbar = new JToolBar();
    terrainToolsToolbar.setBackground(UIManager.getColor("List.background"));
    terrainToolsToolbar.setBorderPainted(false);
    terrainToolsToolbar.setFloatable(false);
    terrainToolsToolbar.setOrientation(0);
    terrainToolsToolbar.setRollover(false);
    terrainToolsToolbar.putClientProperty("JToolBar.isRollover", Boolean.FALSE);
    terrainPanel.add(terrainToolsToolbar, BorderLayout.NORTH);
    final JScrollPane scrollPane1 = new JScrollPane();
    terrainPanel.add(scrollPane1, BorderLayout.CENTER);
    scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
    blockList = new JList();
    blockList.setLayoutOrientation(1);
    blockList.setSelectionMode(0);
    scrollPane1.setViewportView(blockList);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridBagLayout());
    panel1.setFocusCycleRoot(true);
    panel1.setMinimumSize(new Dimension(0, 34));
    panel1.setPreferredSize(new Dimension(60, 48));
    terrainPanel.add(panel1, BorderLayout.SOUTH);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    panel1.add(panel2, gbc);
    panelSecondaryBlock.setBackground(new Color(-65794));
    panelSecondaryBlock.setPreferredSize(new Dimension(32, 32));
    panel2.add(panelSecondaryBlock, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panelSecondaryBlock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
    final Spacer spacer2 = new Spacer();
    panel2.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    panelPrimaryBlock.setBackground(new Color(-65794));
    panelPrimaryBlock.setPreferredSize(new Dimension(32, 32));
    panel2.add(panelPrimaryBlock, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panelPrimaryBlock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new BorderLayout(0, 0));
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/ice_cube.png")), panel3, "Events");
    final JSplitPane splitPane2 = new JSplitPane();
    splitPane2.setOrientation(0);
    panel3.add(splitPane2, BorderLayout.CENTER);
    final JScrollPane scrollPane2 = new JScrollPane();
    splitPane2.setLeftComponent(scrollPane2);
    tree1 = new JTree();
    scrollPane2.setViewportView(tree1);
    final JScrollPane scrollPane3 = new JScrollPane();
    splitPane2.setRightComponent(scrollPane3);
    tree2 = new JTree();
    scrollPane3.setViewportView(tree2);
    objectInspectorContainerPanel = new JPanel();
    objectInspectorContainerPanel.setLayout(new BorderLayout(0, 0));
    splitPane1.setRightComponent(objectInspectorContainerPanel);
    openGlContainer = new JPanel();
    openGlContainer.setLayout(new BorderLayout(0, 0));
    openGlContainer.setBackground(new Color(-16711423));
    mainSplitPane.add(openGlContainer, BorderLayout.CENTER);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainContentPane;
  }

  @Override
  public void windowOpened(WindowEvent e) {

  }

  @Override
  public void windowClosing(WindowEvent e) {

  }

  @Override
  public void windowClosed(WindowEvent e) {

  }

  @Override
  public void windowIconified(WindowEvent e) {

  }

  @Override
  public void windowDeiconified(WindowEvent e) {

  }

  @Override
  public void windowActivated(WindowEvent e) {
    //projectController.resume();
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
   // projectController.pause();
  }
}
