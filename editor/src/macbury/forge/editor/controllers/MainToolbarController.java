package macbury.forge.editor.controllers;

import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.managers.ChangeManager;
import macbury.forge.editor.managers.ChangeManagerListener;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MoreToolbarButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 25.10.14.
 */
public class MainToolbarController implements OnMapChangeListener, ChangeManagerListener, ActionListener {
  private final ButtonGroup editorModeButtonGroup;
  private final JToolBar mainToolbar;
  private final MoreToolbarButton moreButton;
  private final JToggleButton editorModeTerrainButton;
  private final JToggleButton editorModeEventsButton;
  private final JButton editorRedoButton;
  private final JButton editorUndoButton;
  private EditorScreen screen;


  public MainToolbarController(JToolBar mainToolbar, MainMenu mainMenu) {
    this.editorModeButtonGroup = new ButtonGroup();
    this.mainToolbar           = mainToolbar;
    moreButton                 = new MoreToolbarButton(mainMenu);

    this.editorModeTerrainButton = buildToogleButton("blocks");
    this.editorModeEventsButton  = buildToogleButton("game_objects");

    this.editorRedoButton        = buildButton("redo");
    this.editorUndoButton        = buildButton("undo");

    editorModeButtonGroup.add(editorModeTerrainButton);
    editorModeButtonGroup.add(editorModeEventsButton);

    mainToolbar.add(moreButton);
    mainToolbar.addSeparator();
    mainToolbar.add(editorModeTerrainButton);
    mainToolbar.add(editorModeEventsButton);
    mainToolbar.addSeparator();
    mainToolbar.add(editorUndoButton);
    mainToolbar.add(editorRedoButton);

    mainToolbar.add(Box.createHorizontalGlue());
    updateRedoUndoButtons();
  }

  private JToggleButton buildToogleButton(String iconName) {
    JToggleButton button = new JToggleButton();
    ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(icon);
    return button;
  }

  private JButton buildButton(String iconName) {
    JButton button = new JButton();
    ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(icon);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    setScreen(screen);
  }

  @Override
  public void onChangeManagerChange(ChangeManager changeManager) {
    updateRedoUndoButtons();
  }

  private void updateRedoUndoButtons() {
    if (this.screen != null) {
      editorUndoButton.setEnabled(screen.changeManager.canUndo());
      editorRedoButton.setEnabled(screen.changeManager.canRedo());
    } else {
      editorUndoButton.setEnabled(false);
      editorRedoButton.setEnabled(false);
    }
  }

  public void setScreen(EditorScreen newScreen) {
    if (this.screen != null) {
      this.screen.changeManager.removeListener(this);
    }
    this.screen = newScreen;
    screen.changeManager.addListener(this);
    updateRedoUndoButtons();
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
