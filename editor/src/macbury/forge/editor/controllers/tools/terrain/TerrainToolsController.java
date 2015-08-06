package macbury.forge.editor.controllers.tools.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.l2fprod.common.propertysheet.Property;
import icons.Utils;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.BlocksController;
import macbury.forge.editor.controllers.MainToolbarController;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.controllers.tools.inspector.properties.DefaultBeanBinder;
import macbury.forge.editor.controllers.tools.terrain.properties.*;
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
import java.beans.PropertyChangeEvent;

/**
 * Created by macbury on 04.11.14.
 */
public class TerrainToolsController implements OnMapChangeListener, SelectionInterface, MainToolbarController.EditorModeListener, ForgEBootListener, DefaultBeanBinder.PropertyChangeListener {
  private static final String TAG = "TErrainToolsController";
  private final JToolBar toolbar;
  private final ButtonGroup toolsGroup;
  private JToggleButton drawPencilButton;
  private final ButtonGroup modifyGroup;
  private JToggleButton ereaserButton;
  private final BlocksController blocksController;
  private final GdxSwingInputProcessor inputProcessor;
  private JToggleButton drawTreePencil;
  private JToggleButton drawBrushButton;
  private MapPropertySheet inspectorSheetPanel;
  private AbstractSelection currentSelection;
  private JToggleButton drawRectButton;
  private SelectionSystem selectionSystem;
  private ChangeManager changeManager;
  private ChunkMap map;
  private LevelEditorScreen screen;
  private JobManager jobs;
  private DefaultBeanBinder binder;
  private TerrainChangeableProvider currentTerrainChangeableProvider;
  private TerrainChangeableProvider currentProvider;
  private PencilChangeableProvider pencilChangeableProvider;

  public TerrainToolsController(JToolBar terrainToolsToolbar, BlocksController blocksController, GdxSwingInputProcessor inputProcessor, MapPropertySheet terrainInspectorPanel) {
    toolbar                   = terrainToolsToolbar;
    this.blocksController     = blocksController;
    this.inputProcessor       = inputProcessor;
    this.inspectorSheetPanel  = terrainInspectorPanel;
    this.toolsGroup           = new ButtonGroup();
    this.modifyGroup          = new ButtonGroup();

  }


  @Override
  public void afterEngineCreate(ForgE engine) {
    pencilChangeableProvider  = new PencilChangeableProvider();
    drawPencilButton          = buildToogleButton("draw_pencil", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.D, pencilChangeableProvider);
    drawRectButton            = buildToogleButton("draw_rect", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.R, new RectChangeableProvider());
    drawBrushButton           = buildToogleButton("draw_brush", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.B, new CustomBrushChangeableProvider());
    buildToogleButton("level_flood", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.L, new LevelFloodBrushChangeableProvider());
    buildToogleButton("draw_bucket", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.F, new FloodFillBrushChangeableProvider());

    //buildToogleButton("draw_airbrush", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.A);
    drawTreePencil            = buildToogleButton("draw_tree", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.T, new TreeChangeableProvider());
    //buildToogleButton("draw_elipsis", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.Q);
    ereaserButton = buildToogleButton("draw_eraser", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.E, new EreaseChangeableProvider());


    updateUI();
  }

  private void updateUI() {
    boolean interfaceEnabled = screen != null;
    //boolean showAppend       = !ereaserButton.isSelected();
    inspectorSheetPanel.updateUI();
    toolbar.setEnabled(interfaceEnabled);
    drawPencilButton.setEnabled(interfaceEnabled);
    drawRectButton.setEnabled(interfaceEnabled);
    drawTreePencil.setEnabled(interfaceEnabled);
    drawBrushButton.setEnabled(interfaceEnabled);
    ereaserButton.setEnabled(interfaceEnabled);
  }

  private JToggleButton buildToogleButton(String iconName, ButtonGroup buttonGroup, int modifier, int keycode, final TerrainChangeableProvider provider) {
    final JToggleButton button = new JToggleButton();
    //ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(Utils.getIcon(iconName));
    toolbar.add(button);
    buttonGroup.add(button);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TerrainToolsController.this.setCurrentProvider(provider);
      }
    });
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
    this.inspectorSheetPanel.clearLosePropertyEditors();
    this.screen           = null;
    this.changeManager    = null;
    this.selectionSystem  = null;
    this.map              = null;
    this.jobs             = null;

    updateUI();
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    drawPencilButton.setSelected(true);
    this.inspectorSheetPanel.clearLosePropertyEditors();
    this.screen          = screen;
    changeManager        = screen.changeManager;
    map                  = screen.level.terrainMap;

    jobs                 = controller.jobs;

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
  public void onSelectionStart(AbstractSelection selection) {

  }

  @Override
  public void onSelectionChange(AbstractSelection selection) {

  }

  @Override
  public void onSelectionEnd(AbstractSelection selection) {
    CursorChangeable currentTask = currentProvider.provide(map);
    currentTask.setBlockPrimary(blocksController.getCurrentPrimaryBlock());
    currentTask.setBlockSecondary(blocksController.getCurrentSecondaryBlock());
    changeManager.addChangeable(currentTask).apply();

  }


  private void setCurrentSelection(AbstractSelection currentSelection) {
    this.currentSelection = currentSelection;
    this.selectionSystem.setSelection(currentSelection);

    updateUI();
  }

  @Override
  public void onEditorModeChange(MainToolbarController.EditorMode editorMode) {
    if (editorMode == MainToolbarController.EditorMode.Terrain) {
      selectionSystem.addListener(this);
      if (currentProvider == null) {
        setCurrentProvider(pencilChangeableProvider);
      } else {
        setCurrentProvider(currentProvider);
      }
    } else {
      unbindInspector();
      if (selectionSystem != null)
        selectionSystem.removeListener(this);
    }

  }

  private void unbindInspector() {
    inspectorSheetPanel.clearLosePropertyEditors();
    if (binder != null) {
      binder.setListener(null);
      binder.unbind();
    }
    binder = null;
    inspectorSheetPanel.updateUI();
  }

  public void setCurrentProvider(TerrainChangeableProvider currentProvider) {
    this.currentProvider = currentProvider;
    setCurrentSelection(currentProvider.getSelection());
    unbindInspector();
    binder = currentProvider.getPropertySheetBeanBinder(inspectorSheetPanel);
    binder.setListener(this);
  }

  @Override
  public void onPropertyChange(DefaultBeanBinder binder, PropertyChangeEvent event, Object object) {
    Property prop = (Property) event.getSource();
    try {
      Gdx.app.log(TAG, "Revert from " + event.getNewValue() + " to " + event.getOldValue()+ " for " + object);
      prop.setValue(event.getNewValue());
      prop.writeToObject(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
