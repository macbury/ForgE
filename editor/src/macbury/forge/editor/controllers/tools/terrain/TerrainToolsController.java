package macbury.forge.editor.controllers.tools.terrain;

import com.badlogic.gdx.Input;
import icons.Utils;
import macbury.forge.editor.controllers.BlocksController;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.input.KeyShortcutMapping;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.selection.*;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.actions.*;
import macbury.forge.editor.views.MapPropertySheet;
import macbury.forge.voxel.ChunkMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 04.11.14.
 */
public class TerrainToolsController implements OnMapChangeListener, ActionListener, SelectionInterface, MainToolbarController.EditorModeListener {
  private final JToolBar toolbar;
  private final ButtonGroup toolsGroup;
  private final JToggleButton drawPencilButton;
  private final SingleBlockSelection singleBlockSelection;
  private final BoxSelection rectSelection;
  private final ButtonGroup modifyGroup;
  private final EreaseSelection ereaseSelection;
  private final JToggleButton ereaserButton;
  private final BlocksController blocksController;
  private final GdxSwingInputProcessor inputProcessor;
  private final JToggleButton drawTreePencil;
  private final TreeSelection treeSelection;
  private final JToggleButton drawBrushButton;
  private final JSpinner brushSizeSpinner;
  private final JComboBox brushTypeComboBox;
  private final BrushTypeModel brushTypeModel;
  private final BrushSelection brushSelection;
  private final MapPropertySheet inspectorSheetPanel;
  private AbstractSelection currentSelection;
  private final JToggleButton drawRectButton;
  private SelectionSystem selectionSystem;
  private ChangeManager changeManager;
  private ChunkMap map;
  private LevelEditorScreen screen;
  private JobManager jobs;
  private DefaultBeanBinder binder;
  private CursorChangeable currentTask;

  public TerrainToolsController(JToolBar terrainToolsToolbar, BlocksController blocksController, GdxSwingInputProcessor inputProcessor, MapPropertySheet terrainInspectorPanel) {
    toolbar                   = terrainToolsToolbar;
    this.blocksController     = blocksController;
    this.brushSizeSpinner     = new JSpinner();
    this.brushTypeComboBox    = new JComboBox();
    this.inputProcessor       = inputProcessor;
    this.toolsGroup           = new ButtonGroup();
    this.modifyGroup          = new ButtonGroup();
    this.singleBlockSelection = new SingleBlockSelection();
    this.rectSelection        = new BoxSelection();
    this.ereaseSelection      = new EreaseSelection();
    this.treeSelection        = new TreeSelection();
    this.brushSelection       = new BrushSelection();
    drawPencilButton          = buildToogleButton("draw_pencil", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.D);
    drawRectButton            = buildToogleButton("draw_rect", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.R);
    drawBrushButton           = buildToogleButton("draw_brush", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.B);
    //buildToogleButton("draw_bucket", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.F);

    //buildToogleButton("draw_airbrush", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.A);
    drawTreePencil            = buildToogleButton("draw_tree", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.T);
    //buildToogleButton("draw_elipsis", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.Q);
    ereaserButton = buildToogleButton("draw_eraser", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.E);

    //toolbar.addSeparator();
    SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
    spinnerNumberModel.setMinimum(1);
    spinnerNumberModel.setValue(1);
    spinnerNumberModel.setMaximum(ApplyCustomBrushChangeable.BrushType.MAX_SIZE);

    brushSizeSpinner.setSize(50, 16);
    brushSizeSpinner.setModel(spinnerNumberModel);

    this.brushTypeModel = new BrushTypeModel();
    brushTypeComboBox.setSize(200, brushTypeComboBox.getPreferredSize().height);
    brushTypeComboBox.setModel(brushTypeModel);
    brushTypeComboBox.setRenderer(new BrushListRenderer());
    //toolbar.add(brushTypeComboBox);
    //toolbar.add(brushSizeSpinner);

    this.inspectorSheetPanel = terrainInspectorPanel;
    updateUI();
  }

  private void updateUI() {
    boolean interfaceEnabled = screen != null;
    boolean showAppend       = !ereaserButton.isSelected();
    inspectorSheetPanel.updateUI();
    toolbar.setEnabled(interfaceEnabled);
    drawPencilButton.setEnabled(interfaceEnabled);
    drawRectButton.setEnabled(interfaceEnabled);
    drawTreePencil.setEnabled(interfaceEnabled);
    drawBrushButton.setEnabled(interfaceEnabled);
    ereaserButton.setEnabled(interfaceEnabled);
    boolean brushOptionsEnabled = interfaceEnabled && drawBrushButton.isSelected();
    brushSizeSpinner.setEnabled(brushOptionsEnabled);
    brushTypeComboBox.setEnabled(brushOptionsEnabled);
  }

  private JToggleButton buildToogleButton(String iconName, ButtonGroup buttonGroup, int modifier, int keycode) {
    final JToggleButton button = new JToggleButton();
    //ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(Utils.getIcon(iconName));
    toolbar.add(button);
    buttonGroup.add(button);
    button.addActionListener(this);
    button.setToolTipText(iconName);
    inputProcessor.registerMapping(modifier, keycode, new KeyShortcutMapping.KeyShortcutListener() {
      @Override
      public void onKeyShortcut(KeyShortcutMapping shortcutMapping) {
        button.doClick();
      }
    });

    return button;
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    unbindInspector();
    if (selectionSystem != null) {
      selectionSystem.removeListener(this);
    }

    this.screen           = null;
    this.changeManager    = null;
    this.selectionSystem  = null;
    this.map              = null;
    this.jobs             = null;

    updateUI();
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    brushTypeModel.reload();
    drawPencilButton.setSelected(true);
    this.screen          = screen;
    changeManager        = screen.changeManager;
    map                  = screen.level.terrainMap;

    jobs                 = controller.jobs;

    singleBlockSelection.setVoxelSize(map.voxelSize);
    rectSelection.setVoxelSize(map.voxelSize);
    selectionSystem = screen.selectionSystem;

  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {
    updateUI();
  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == drawPencilButton) {
      setCurrentSelection(singleBlockSelection);
    } else if (e.getSource() == drawRectButton) {
      setCurrentSelection(rectSelection);
    } else if (e.getSource() == ereaserButton) {
      setCurrentSelection(ereaseSelection);
    } else if (e.getSource() == drawTreePencil) {
      setCurrentSelection(treeSelection);
    } else if (e.getSource() == drawBrushButton) {
      setCurrentSelection(brushSelection);
    } else {
      updateUI();
    }

  }

  @Override
  public void onSelectionStart(AbstractSelection selection) {

  }

  @Override
  public void onSelectionChange(AbstractSelection selection) {

  }

  @Override
  public void onSelectionEnd(AbstractSelection selection) {
    currentTask.setSelection(selection);
    currentTask.setBlockPrimary(blocksController.getCurrentPrimaryBlock());
    currentTask.setBlockSecondary(blocksController.getCurrentSecondaryBlock());
    changeManager.addChangeable(currentTask).apply();

    putNewTask(selection);
  }

  private void putNewTask(AbstractSelection selection) {
    unbindInspector();
    currentTask = createTaskForSelection(selection);
    if (TaskPropertySheetProvider.class.isInstance(currentTask)) {
      TaskPropertySheetProvider provider = (TaskPropertySheetProvider)currentTask;
      binder = provider.getPropertySheetBeanBinder(inspectorSheetPanel);
    }
  }

  private CursorChangeable createTaskForSelection(AbstractSelection selection) {
    CursorChangeable task = null;
    if (selection == singleBlockSelection) {
      task = new ApplyBlock(map);
    } else if (selection == rectSelection) {
      task = new ApplyRangeBlock(map);
    } else if (selection == ereaseSelection) {
      task = new EraserBlock(map);
    } else if (selection == treeSelection) {
      task = new TreeBuilderChangeable(map);
    } else if (selection == brushSelection) {
      task = new ApplyCustomBrushChangeable(map,(Integer)brushSizeSpinner.getValue(), (ApplyCustomBrushChangeable.BrushType)brushTypeModel.getSelectedItem());
    }
    //
    return task;
  }

  private void setCurrentSelection(AbstractSelection currentSelection) {
    this.currentSelection = currentSelection;
    this.selectionSystem.setSelection(currentSelection);

    putNewTask(currentSelection);

    updateUI();
  }

  @Override
  public void onEditorModeChange(MainToolbarController.EditorMode editorMode) {
    if (editorMode == MainToolbarController.EditorMode.Terrain) {
      selectionSystem.addListener(this);
      if (currentSelection == null) {
        setCurrentSelection(singleBlockSelection);
      } else {
        setCurrentSelection(currentSelection);
      }
    } else {
      unbindInspector();
      if (selectionSystem != null)
        selectionSystem.removeListener(this);
    }

  }

  private void unbindInspector() {
    if (binder != null) {
      binder.unbind();
    }
    binder = null;
    inspectorSheetPanel.updateUI();
  }

}
