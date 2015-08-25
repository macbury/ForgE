package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.GdxRuntimeException;
import icons.Utils;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.input.GdxSwingInputProcessor;
import macbury.forge.editor.input.KeyShortcutMapping;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.ChangeManagerListener;
import macbury.forge.editor.utils.InterfaceTrigger;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.views.MoreToolbarButton;
import macbury.forge.time.TimeManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Date;

/**
 * Created by macbury on 25.10.14.
 */
public class MainToolbarController implements OnMapChangeListener, ChangeManagerListener, ActionListener, KeyShortcutMapping.KeyShortcutListener, ChangeListener, AdjustmentListener {
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
  private final JButton codeEditorButton;
  private final CodeEditorController codeEditorController;
  private final JLabel currentTimeLabel;
  private final JScrollBar timeScroll;
  private LevelEditorScreen screen;
  public final InterfaceTrigger<EditorModeListener> editorModeListeners = new InterfaceTrigger<EditorModeListener>();

  public MainToolbarController(ProjectController projectController, JToolBar mainToolbar, MainMenu mainMenu, GdxSwingInputProcessor inputProcessor, PlayerController playerController, CodeEditorController codeEditorController) {
    this.editorModeButtonGroup = new ButtonGroup();
    this.codeEditorController  = codeEditorController;
    this.mainToolbar           = mainToolbar;
    this.projectController     = projectController;
    moreButton                 = new MoreToolbarButton(mainMenu);
    this.playerController      = playerController;

    this.editorRedoButton        = buildButton("redo");
    this.editorUndoButton        = buildButton("undo");

    this.saveMapButton           = buildButton("save");
    this.playMapButton           = buildButton("play");

    this.codeEditorButton        = buildButton("code");
    codeEditorButton.setVisible(false);
    this.terrainEditButton       = buildToogleButton("terrain");
    this.entitiesEditButton      = buildToogleButton("entities");
    undoMapping = inputProcessor.registerMapping(Input.Keys.CONTROL_LEFT, Input.Keys.Z, this);
    redoMapping = inputProcessor.registerMapping(Input.Keys.CONTROL_LEFT, Input.Keys.Y, this);

    JPanel timeScrollContainer = new JPanel(new BorderLayout());
    timeScrollContainer.setPreferredSize(new Dimension(260, 32));

    this.currentTimeLabel    = new JLabel("8:00", SwingConstants.CENTER);
    timeScrollContainer.add(currentTimeLabel, BorderLayout.NORTH);

    this.timeScroll = new JScrollBar(Adjustable.HORIZONTAL);
    timeScroll.setMaximum((int) TimeManager.DAY_IN_SECONDS);
    timeScroll.setValue((int)TimeManager.DEFAULT_TIME);
    timeScrollContainer.add(timeScroll, BorderLayout.SOUTH);

    timeScroll.addAdjustmentListener(this);

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
    mainToolbar.addSeparator();
    mainToolbar.add(playMapButton);
    mainToolbar.addSeparator();

    mainToolbar.add(timeScrollContainer);
    mainToolbar.add(Box.createHorizontalGlue());

    updateRedoUndoButtons();

    editorModeListeners.trigger(new InterfaceTrigger.Trigger<EditorModeListener>() {
      @Override
      public void onListenerTrigger(EditorModeListener listener) {
        listener.onEditorModeChange(EditorMode.None);
      }
    });
  }

  private JToggleButton buildToogleButton(String iconName) {
    JToggleButton button = new JToggleButton();
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(Utils.getIcon(iconName));
    button.setEnabled(false);
    button.addActionListener(this);
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
    terrainEditButton.setEnabled(false);
    entitiesEditButton.setEnabled(false);
    timeScroll.setEnabled(false);
    editorModeListeners.trigger(new InterfaceTrigger.Trigger<EditorModeListener>() {
      @Override
      public void onListenerTrigger(EditorModeListener listener) {
        listener.onEditorModeChange(EditorMode.None);
      }
    });
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    setScreen(screen);
    terrainEditButton.setEnabled(true);
    entitiesEditButton.setEnabled(true);
    selectEditorMode(EditorMode.Terrain);
    timeScroll.setEnabled(true);
    timeScroll.setMaximum((int) TimeManager.DAY_IN_SECONDS);
    timeScroll.setValue(ForgE.time.getSeconds());
    updateTimeInfo();
  }

  private void selectEditorMode(final EditorMode mode) {
    switch (mode) {
      case Terrain:
        terrainEditButton.setSelected(true);
        break;
      case Objects:
        entitiesEditButton.setSelected(true);
        break;
      default: throw new GdxRuntimeException("No support for mode: " +mode);
    }

    editorModeListeners.trigger(new InterfaceTrigger.Trigger<EditorModeListener>() {
      @Override
      public void onListenerTrigger(EditorModeListener listener) {
        listener.onEditorModeChange(mode);
      }
    });
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

    if (e.getSource() == codeEditorButton) {
      codeEditorController.show();
    }

    if (e.getSource() == terrainEditButton) {
      selectEditorMode(EditorMode.Terrain);
    }

    if (e.getSource() == entitiesEditButton) {
      selectEditorMode(EditorMode.Objects);
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

  @Override
  public void stateChanged(ChangeEvent e) {

  }

  @Override
  public void adjustmentValueChanged(AdjustmentEvent e) {
    if (e.getSource() == timeScroll) {
      ForgE.time.setDuration(timeScroll.getModel().getValue());
      updateTimeInfo();
    }
  }

  private void updateTimeInfo() {
    currentTimeLabel.setText(ForgE.time.getFormattedDuration());
  }

  public enum EditorMode {
    Terrain, Objects, None
  }

  public interface EditorModeListener {
    public void onEditorModeChange(EditorMode editorMode);
  }
}
