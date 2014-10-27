package macbury.forge.editor.controllers;

import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.runnables.UpdateStatusBar;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.windows.MainWindow;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by macbury on 18.10.14.
 */
public class ProjectController {
  private MainWindow mainWindow;
  public EditorScreen editorScreen;
  private Array<OnMapChangeListener> onMapChangeListenerArray = new Array<OnMapChangeListener>();

  public void setMainWindow(MainWindow mainWindow) {
    this.mainWindow = mainWindow;
  }

  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public void newMap() {
    mainWindow.setTitle("[ForgE] - New project");
    this.editorScreen = new EditorScreen();
    ForgE.screens.set(editorScreen);

    for (OnMapChangeListener listener : onMapChangeListenerArray) {
      listener.onNewMap(this, this.editorScreen);
    }
  }

  public void setStatusLabel(JLabel statusLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel, JLabel mapCursorPositionLabel) {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(new UpdateStatusBar(this, statusLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel), 0, 250, TimeUnit.MILLISECONDS);
  }

  public void addOnMapChangeListener(OnMapChangeListener listener) {
    if (!onMapChangeListenerArray.contains(listener, true)) {
      onMapChangeListenerArray.add(listener);
    }
  }
}
