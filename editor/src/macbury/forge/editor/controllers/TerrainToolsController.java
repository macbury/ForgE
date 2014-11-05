package macbury.forge.editor.controllers;

import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.BoxSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.selection.SingleBlockSelection;
import macbury.forge.editor.systems.SelectionSystem;
import macbury.forge.editor.undo_redo.ChangeManager;
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
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    drawPencilButton.setSelected(true);
    unbind();
    selectionSystem      = screen.selectionSystem;
    changeManager        = screen.changeManager;
    map                  = screen.level.terrainMap;

    singleBlockSelection.setVoxelSize(map.voxelSize);
    boxSelection.setVoxelSize(map.voxelSize);

    selectionSystem.addListener(this);

    setCurrentSelection(singleBlockSelection);
  }

  private void unbind() {
    if (selectionSystem != null) {
      selectionSystem.removeListener(this);
    }
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
    changeManager.addChangeable(new ApplyRangeBlock(selection, map)).apply();
  }

  private void setCurrentSelection(AbstractSelection currentSelection) {
    this.currentSelection = currentSelection;
    this.selectionSystem.setSelection(currentSelection);
  }
}
