package macbury.forge.editor.controllers;

import macbury.forge.ForgE;
import macbury.forge.editor.runnables.UpdateStatusBar;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.views.MainMenu;
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
  private MainMenu mainMenu;

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
    refresh();
  }

  private void refresh() {
    this.mainMenu.setEditor(editorScreen);
  }


  public void setStatusLabel(JLabel statusLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel) {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(new UpdateStatusBar(this, statusLabel, statusMemoryLabel, statusRenderablesLabel), 0, 1, TimeUnit.SECONDS);
  }

  public void setMainMenu(MainMenu mainMenu) {
    this.mainMenu = mainMenu;


  }

}
