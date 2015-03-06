package macbury.forge.editor.controllers;

import com.badlogic.gdx.Input;
import macbury.forge.blocks.Block;
import icons.Utils;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.input.KeyShortcutMapping;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.selection.*;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.Changeable;
import macbury.forge.editor.undo_redo.actions.ApplyBlock;
import macbury.forge.editor.undo_redo.actions.ApplyRangeBlock;
import macbury.forge.editor.undo_redo.actions.EraserBlock;
import macbury.forge.voxel.ChunkMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 04.11.14.
 */
public class TerrainToolsController implements OnMapChangeListener, ActionListener, SelectionInterface {
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
  private AbstractSelection currentSelection;
  private final JToggleButton drawRectButton;
  private SelectionSystem selectionSystem;
  private ChangeManager changeManager;
  private ChunkMap map;
  private EditorScreen screen;
  private JobManager jobs;

  public TerrainToolsController(JToolBar terrainToolsToolbar, BlocksController blocksController, GdxSwingInputProcessor inputProcessor) {
    toolbar                   = terrainToolsToolbar;
    this.blocksController     = blocksController;
    this.inputProcessor       = inputProcessor;
    this.toolsGroup           = new ButtonGroup();
    this.modifyGroup          = new ButtonGroup();

    this.singleBlockSelection = new SingleBlockSelection();
    this.rectSelection        = new BoxSelection();
    this.ereaseSelection      = new EreaseSelection();

    drawPencilButton          = buildToogleButton("draw_pencil", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.D);
    drawRectButton            = buildToogleButton("draw_rect", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.R);

    buildToogleButton("draw_bucket", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.F);

    buildToogleButton("draw_airbrush", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.A);
    buildToogleButton("draw_tree", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.T);
    buildToogleButton("draw_elipsis", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.Q);
    ereaserButton = buildToogleButton("draw_eraser", toolsGroup, Input.Keys.SHIFT_LEFT, Input.Keys.E);

    toolbar.addSeparator();

    updateUI();
  }

  private void updateUI() {
    boolean interfaceEnabled = screen != null;
    boolean showAppend       = !ereaserButton.isSelected();


    toolbar.setEnabled(interfaceEnabled);
    drawPencilButton.setEnabled(interfaceEnabled);
    drawRectButton.setEnabled(interfaceEnabled);

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
  public void onCloseMap(ProjectController controller, EditorScreen screen) {
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
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    drawPencilButton.setSelected(true);
    this.screen          = screen;
    selectionSystem      = screen.selectionSystem;
    changeManager        = screen.changeManager;
    map                  = screen.level.terrainMap;

    jobs                 = controller.jobs;

    singleBlockSelection.setVoxelSize(map.voxelSize);
    rectSelection.setVoxelSize(map.voxelSize);

    selectionSystem.addListener(this);

    setCurrentSelection(singleBlockSelection);
    updateUI();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == drawPencilButton) {
      setCurrentSelection(singleBlockSelection);
    } else if (e.getSource() == drawRectButton) {
      setCurrentSelection(rectSelection);
    } else if (e.getSource() == ereaserButton) {
      setCurrentSelection(ereaseSelection);
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
    createTaskForSelection(selection);
  }

  private void createTaskForSelection(AbstractSelection selection) {
    Changeable task = null;
    Block blockToDraw = selection.getSelectedMouseButton() == Input.Buttons.LEFT ? blocksController.getCurrentPrimaryBlock() : blocksController.getCurrentSecondaryBlock();
    if (selection == singleBlockSelection) {
      task = new ApplyBlock(selection, map, blockToDraw);
    } else if (selection == rectSelection) {
      task = new ApplyRangeBlock(selection, map, blockToDraw);
    } else if (selection == ereaseSelection) {
      task = new EraserBlock(selection, map);
    }
    changeManager.addChangeable(task).apply();
  }

  private void setCurrentSelection(AbstractSelection currentSelection) {
    this.currentSelection = currentSelection;
    this.selectionSystem.setSelection(currentSelection);
    updateUI();
  }
}
