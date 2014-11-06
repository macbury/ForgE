package macbury.forge.editor.controllers;

import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.BoxSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.selection.SingleBlockSelection;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.Changeable;
import macbury.forge.editor.undo_redo.actions.ApplyBlock;
import macbury.forge.editor.undo_redo.actions.ApplyRangeBlock;
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
  private final BoxSelection boxSelection;
  private AbstractSelection currentSelection;
  private final JToggleButton drawRectButton;
  private SelectionSystem selectionSystem;
  private ChangeManager changeManager;
  private ChunkMap map;
  private EditorScreen screen;
  private JobManager jobs;

  public TerrainToolsController(JToolBar terrainToolsToolbar) {
    toolbar          = terrainToolsToolbar;
    this.toolsGroup  = new ButtonGroup();

    this.singleBlockSelection = new SingleBlockSelection();
    this.boxSelection         = new BoxSelection();

    drawPencilButton          = buildToogleButton("draw_pencil", toolsGroup);
    drawRectButton            = buildToogleButton("draw_rect", toolsGroup);
    buildToogleButton("draw_bucket", toolsGroup);

    buildToogleButton("draw_airbrush", toolsGroup);
    buildToogleButton("draw_elipsis", toolsGroup);
    buildToogleButton("draw_eraser", toolsGroup);

    toolbar.addSeparator();
    updateUI();
  }

  private void updateUI() {
    boolean interfaceEnabled = screen != null;

    toolbar.setEnabled(interfaceEnabled);
    drawPencilButton.setEnabled(interfaceEnabled);
    drawRectButton.setEnabled(interfaceEnabled);
  }

  private JToggleButton buildToogleButton(String iconName, ButtonGroup buttonGroup) {
    JToggleButton button = new JToggleButton();
    ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(icon);
    toolbar.add(button);
    buttonGroup.add(button);
    button.addActionListener(this);
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
    boxSelection.setVoxelSize(map.voxelSize);

    selectionSystem.addListener(this);

    setCurrentSelection(singleBlockSelection);
    updateUI();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == drawPencilButton) {
      setCurrentSelection(singleBlockSelection);
    } else if (e.getSource() == drawRectButton) {
      setCurrentSelection(boxSelection);
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
    if (selection == singleBlockSelection) {
      task = new ApplyBlock(selection, map);
    } else if (selection == boxSelection) {
      task = new ApplyRangeBlock(selection, map);
    }
    changeManager.addChangeable(task).apply();
  }

  private void setCurrentSelection(AbstractSelection currentSelection) {
    this.currentSelection = currentSelection;
    this.selectionSystem.setSelection(currentSelection);
  }
}
