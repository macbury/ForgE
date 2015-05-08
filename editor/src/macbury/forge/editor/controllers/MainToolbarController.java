package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import icons.Utils;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.input.KeyShortcutMapping;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.ChangeManagerListener;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MoreToolbarButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 25.10.14.
 */
public class MainToolbarController implements OnMapChangeListener, ChangeManagerListener, ActionListener, KeyShortcutMapping.KeyShortcutListener {
  private static final String TAG = "MainToolbarController";
  private final ButtonGroup editorModeButtonGroup;
  private final JToolBar mainToolbar;
  private final MoreToolbarButton moreButton;
  private final JButton editorRedoButton;
  private final JButton editorUndoButton;
  private final KeyShortcutMapping undoMapping;
  private final KeyShortcutMapping redoMapping;
  private final ProjectController projectController;
  private final JButton saveMapButton;
  private final JButton playMapButton;
  private final PlayerController playerController;
  private final JToggleButton terrainEditButton;
  private final JToggleButton entitiesEditButton;
  private LevelEditorScreen screen;

  public MainToolbarController(ProjectController projectController, JToolBar mainToolbar, MainMenu mainMenu, GdxSwingInputProcessor inputProcessor, PlayerController playerController) {
    this.editorModeButtonGroup = new ButtonGroup();
    this.mainToolbar           = mainToolbar;
    this.projectController     = projectController;
    moreButton                 = new MoreToolbarButton(mainMenu);
    this.playerController      = playerController;

    this.editorRedoButton        = buildButton("redo");
    this.editorUndoButton        = buildButton("undo");

    this.saveMapButton           = buildButton("save");
    this.playMapButton           = buildButton("play");

    this.terrainEditButton       = buildToogleButton("terrain");
    this.entitiesEditButton      = buildToogleButton("entities");
    undoMapping = inputProcessor.registerMapping(Input.Keys.CONTROL_LEFT, Input.Keys.Z, this);
    redoMapping = inputProcessor.registerMapping(Input.Keys.CONTROL_LEFT, Input.Keys.Y, this);

    undoMapping.addListener(this);
    redoMapping.addListener(this);

    ButtonGroup editButtonsGroup = new ButtonGroup();
    editButtonsGroup.add(terrainEditButton);
    editButtonsGroup.add(entitiesEditButton);

    mainToolbar.add(moreButton);
    mainToolbar.addSeparator();
    mainToolbar.add(saveMapButton);
    mainToolbar.addSeparator();
    mainToolbar.add(editorUndoButton);
    mainToolbar.add(editorRedoButton);
    mainToolbar.addSeparator();
    mainToolbar.add(terrainEditButton);
    mainToolbar.add(entitiesEditButton);
    mainToolbar.add(Box.createHorizontalGlue());
    mainToolbar.add(playMapButton);


    updateRedoUndoButtons();
  }

  private JToggleButton buildToogleButton(String iconName) {
    JToggleButton button = new JToggleButton();
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(Utils.getIcon(iconName));
    button.setEnabled(false);
    return button;
  }

  private JButton buildButton(String iconName) {
    JButton button = new JButton();
    //ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(Utils.getIcon(iconName));
    button.addActionListener(this);
    return button;
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    setScreen(null);
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    setScreen(screen);
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  @Override
  public void onChangeManagerChange(ChangeManager changeManager) {
    updateRedoUndoButtons();
  }

  private void updateRedoUndoButtons() {
    if (this.screen != null) {
      editorUndoButton.setEnabled(screen.changeManager.canUndo());
      editorRedoButton.setEnabled(screen.changeManager.canRedo());
      saveMapButton.setEnabled(screen.changeManager.canUndo());
    } else {
      editorUndoButton.setEnabled(false);
      editorRedoButton.setEnabled(false);
      saveMapButton.setEnabled(false);
    }
  }

  public void setScreen(LevelEditorScreen newScreen) {
    if (this.screen != null) {
      this.screen.changeManager.removeListener(this);
      this.screen = null;
    }
    if (newScreen != null) {
      this.screen = newScreen;
      screen.changeManager.addListener(this);
    }
    updateRedoUndoButtons();

    boolean visible = screen != null;
    terrainEditButton.setEnabled(visible);
    entitiesEditButton.setEnabled(visible);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == editorUndoButton) {
      undo();
    }

    if (e.getSource() == editorRedoButton) {
      redo();
    }

    if (e.getSource() == saveMapButton) {
      projectController.saveMap();
    }

    if (e.getSource() == playMapButton) {

      playerController.runGame();
    }
  }

  private void undo() {
    if (screen != null && screen.changeManager.canUndo()) {
      screen.changeManager.undo();
    }
  }

  private void redo() {
    if (screen != null && screen.changeManager.canRedo()) {
      screen.changeManager.redo();
    }
  }

  @Override
  public void onKeyShortcut(KeyShortcutMapping shortcutMapping) {
    Gdx.app.log(TAG, "Undo redo shortcut!");
    if (undoMapping == shortcutMapping) {
      undo();
    } else if (redoMapping == shortcutMapping) {
      redo();
    }
  }
}
