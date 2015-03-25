package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTInput;
import com.ezware.dialog.task.TaskDialogs;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.*;
import macbury.forge.editor.controllers.inspector.InspectorController;
import macbury.forge.editor.controllers.maps.MapTreeController;
import macbury.forge.editor.controllers.tools.ToolsController;
import macbury.forge.editor.controllers.tools.events.EventsController;
import macbury.forge.editor.controllers.tools.terrain.TerrainToolsController;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.views.ImagePanel;
import macbury.forge.editor.views.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class MainWindow extends JFrame implements ForgEBootListener, FocusListener, WindowFocusListener, Thread.UncaughtExceptionHandler {
  private static final String WINDOW_MAIN_NAME = "ForgE";
  private final BlocksController blocksController;
  private final DirectoryWatcher directoryWatcher;
  private final ShadersController shadersController;
  private final GdxSwingInputProcessor inputProcessor;
  private final LwjglAWTInput input;
  private final MapTreeController mapTreeController;
  private final InspectorController inspectorController;
  private final ToolsController toolsController;
  private final EventsController eventsToolsController;
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
  public JTabbedPane toolsPane;
  private JList list1;
  private JToolBar terrainToolsToolbar;
  public JProgressBar jobProgressBar;
  private JList blockList;
  private JPanel mapSettingsPanel;
  private JPanel panelPrimaryBlock;
  private JPanel panelSecondaryBlock;
  public JPanel mainSplitPane;
  private JComboBox brushTypeComboBox;
  private JSpinner brushSizeSpinner;
  private JPanel brushPropertiesPanel;
  private JTextArea logArea;
  private boolean bruteFocus;

  public MainWindow() {
    super();
    $$$setupUI$$$();
    Thread.setDefaultUncaughtExceptionHandler(this);

    Toolkit kit = Toolkit.getDefaultToolkit();
    //Image mainIcon   = kit.createImage(ClassLoader.getSystemResource("icons/main.ico"));
    //setIconImage(mainIcon);

    setContentPane(mainContentPane);
    mainContentPane.setVisible(false);
    setSize(1360, 760);
    //setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    setTitle(null);
    setVisible(true);

    this.inputProcessor = new GdxSwingInputProcessor();
    this.directoryWatcher = new DirectoryWatcher();
    this.jobs = new JobManager();
    Config config = new Config();
    config.generateWireframe = true;
    config.renderStaticOctree = false;
    config.renderBoundingBox = false;
    config.debug = true;

    engine = new ForgE(config);
    toolsController  = new ToolsController(toolsPane);
    blocksController = new BlocksController(blockList, directoryWatcher, jobs, (ImagePanel) panelPrimaryBlock, (ImagePanel) panelSecondaryBlock);
    this.progressTaskDialog = new ProgressTaskDialog();
    projectController = new ProjectController();
    mainMenu = new MainMenu(projectController);
    eventsToolsController  = new EventsController(this);
    terrainToolsController = new TerrainToolsController(terrainToolsToolbar, blocksController, inputProcessor);
    mainToolbarController = new MainToolbarController(projectController, mainToolbar, mainMenu, inputProcessor);
    shadersController = new ShadersController(directoryWatcher);
    mapTreeController = new MapTreeController(mapTree, projectController);
    inspectorController = new InspectorController(mapSettingsPanel);

    engine.addBootListener(this);
    engine.addBootListener(blocksController);
    engine.addBootListener(mapTreeController);

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

    projectController.addOnMapChangeListener(inspectorController);
    projectController.addOnMapChangeListener(mainMenu);
    projectController.addOnMapChangeListener(mainToolbarController);
    projectController.addOnMapChangeListener(terrainToolsController);
    projectController.addOnMapChangeListener(blocksController);
    projectController.addOnMapChangeListener(mapTreeController);
    projectController.addOnMapChangeListener(toolsController);
    projectController.addOnMapChangeListener(eventsToolsController);
    toolsController.register(terrainToolsController, 0);
    toolsController.register(eventsToolsController, 2);
    mainContentPane.setVisible(true);
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
    mainContentPane.setVisible(true);
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
  }

  @Override
  public void windowLostFocus(WindowEvent e) {
    bruteFocus = false;
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    TaskDialogs.showException(e);
    e.printStackTrace();
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
    statusBarPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 11, new Insets(5, 5, 5, 10), -1, -1));
    statusBarPanel.setMinimumSize(new Dimension(646, 32));
    mainContentPane.add(statusBarPanel, BorderLayout.SOUTH);
    statusRenderablesLabel = new JLabel();
    statusRenderablesLabel.setText("Voxels: 6");
    statusBarPanel.add(statusRenderablesLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(62, 26), null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
    statusBarPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(14, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator1, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator2, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    statusMemoryLabel = new JLabel();
    statusMemoryLabel.setText("Memory: 6GB/10GB");
    statusBarPanel.add(statusMemoryLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(138, 26), null, 0, false));
    statusFpsLabel = new JLabel();
    statusFpsLabel.setText("FPS: 60s");
    statusBarPanel.add(statusFpsLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(59, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator3 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator3, new com.intellij.uiDesigner.core.GridConstraints(0, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    mapCursorPositionLabel = new JLabel();
    mapCursorPositionLabel.setText("0x0x0");
    statusBarPanel.add(mapCursorPositionLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 10, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(46, 26), null, 0, false));
    final JToolBar.Separator toolBar$Separator4 = new JToolBar.Separator();
    statusBarPanel.add(toolBar$Separator4, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 26), null, 0, false));
    statusTriangleCountLabel = new JLabel();
    statusTriangleCountLabel.setText("Triangle count: 0");
    statusBarPanel.add(statusTriangleCountLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(112, 26), null, 0, false));
    jobProgressBar = new JProgressBar();
    jobProgressBar.setIndeterminate(true);
    statusBarPanel.add(jobProgressBar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(146, 26), null, 0, false));
    mainToolbar = new JToolBar();
    mainToolbar.setFloatable(false);
    mainContentPane.add(mainToolbar, BorderLayout.NORTH);
    mainSplitPane = new JPanel();
    mainSplitPane.setLayout(new BorderLayout(0, 0));
    mainContentPane.add(mainSplitPane, BorderLayout.CENTER);
    final JSplitPane splitPane1 = new JSplitPane();
    splitPane1.setContinuousLayout(true);
    splitPane1.setDividerLocation(619);
    splitPane1.setOrientation(0);
    splitPane1.setPreferredSize(new Dimension(320, 527));
    splitPane1.setResizeWeight(0.5);
    mainSplitPane.add(splitPane1, BorderLayout.WEST);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new BorderLayout(0, 0));
    splitPane1.setLeftComponent(panel1);
    toolsPane = new JTabbedPane();
    panel1.add(toolsPane, BorderLayout.CENTER);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout(0, 0));
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/soil_layers.png")), panel2, "Terrain");
    terrainToolsToolbar = new JToolBar();
    terrainToolsToolbar.setBackground(UIManager.getColor("List.background"));
    terrainToolsToolbar.setBorderPainted(false);
    terrainToolsToolbar.setFloatable(false);
    terrainToolsToolbar.setOrientation(0);
    terrainToolsToolbar.setRollover(false);
    terrainToolsToolbar.putClientProperty("JToolBar.isRollover", Boolean.FALSE);
    panel2.add(terrainToolsToolbar, BorderLayout.NORTH);
    final JScrollPane scrollPane1 = new JScrollPane();
    panel2.add(scrollPane1, BorderLayout.CENTER);
    scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
    blockList = new JList();
    blockList.setLayoutOrientation(1);
    blockList.setSelectionMode(0);
    scrollPane1.setViewportView(blockList);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridBagLayout());
    panel3.setFocusCycleRoot(true);
    panel3.setMinimumSize(new Dimension(0, 34));
    panel3.setPreferredSize(new Dimension(60, 48));
    panel2.add(panel3, BorderLayout.SOUTH);
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    panel3.add(panel4, gbc);
    panelSecondaryBlock.setBackground(new Color(-65794));
    panelSecondaryBlock.setPreferredSize(new Dimension(32, 32));
    panel4.add(panelSecondaryBlock, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panelSecondaryBlock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
    final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
    panel4.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    panelPrimaryBlock.setBackground(new Color(-65794));
    panelPrimaryBlock.setPreferredSize(new Dimension(32, 32));
    panel4.add(panelPrimaryBlock, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panelPrimaryBlock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new BorderLayout(0, 0));
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/places.png")), panel5, "Objects");
    final JPanel panel6 = new JPanel();
    panel6.setLayout(new BorderLayout(0, 0));
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/ice_cube.png")), panel6, "Events");
    list1 = new JList();
    panel6.add(list1, BorderLayout.CENTER);
    mapSettingsPanel = new JPanel();
    mapSettingsPanel.setLayout(new BorderLayout(0, 0));
    toolsPane.addTab("", new ImageIcon(getClass().getResource("/icons/map_settings.png")), mapSettingsPanel, "Map");
    final JScrollPane scrollPane2 = new JScrollPane();
    splitPane1.setRightComponent(scrollPane2);
    mapTree = new JTree();
    mapTree.setRootVisible(true);
    mapTree.setShowsRootHandles(true);
    scrollPane2.setViewportView(mapTree);
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
}
