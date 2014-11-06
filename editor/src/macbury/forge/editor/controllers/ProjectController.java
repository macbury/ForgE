package macbury.forge.editor.controllers;

import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.parell.Job;
import macbury.forge.editor.parell.JobListener;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.NewLevelJob;
import macbury.forge.editor.runnables.UpdateStatusBar;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.editor.windows.ProgressTaskDialog;
import macbury.forge.level.LevelState;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.ShaderReloadListener;
import macbury.forge.shaders.utils.ShadersManager;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by macbury on 18.10.14.
 */
public class ProjectController implements JobListener, ShaderReloadListener {
  private static final String LEVEL_STATE_LOADED_CALLBACK = "onLevelStateLoaded";
  private MainWindow mainWindow;
  public EditorScreen editorScreen;
  private Array<OnMapChangeListener> onMapChangeListenerArray = new Array<OnMapChangeListener>();
  private JobManager jobs;
  private ProgressTaskDialog progressTaskDialog;
  private JProgressBar jobProgressBar;

  public void setMainWindow(MainWindow mainWindow) {
    this.mainWindow         = mainWindow;
    this.jobs               = mainWindow.jobs;

    this.progressTaskDialog = mainWindow.progressTaskDialog;
    this.jobProgressBar     = mainWindow.jobProgressBar;
    jobProgressBar.setVisible(false);

    ForgE.shaders.addOnShaderReloadListener(this);
    this.jobs.addListener(this);

    updateUI();
  }

  private void updateUI() {
    boolean editorScreenEnabled = editorScreen != null;
    //mainWindow.openGlContainer.setVisible(editorScreenEnabled);
  }

  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public void newMap() {
    NewLevelJob job = new NewLevelJob();
    job.setCallback(this, LEVEL_STATE_LOADED_CALLBACK);
    jobs.enqueue(job);
  }

  public void onLevelStateLoaded(LevelState state, NewLevelJob job) {
    mainWindow.setTitle("[ForgE] - New project");
    this.editorScreen = new EditorScreen(state);
    ForgE.screens.set(editorScreen);
    updateUI();

    for (OnMapChangeListener listener : onMapChangeListenerArray) {
      listener.onNewMap(this, this.editorScreen);
    }
  }

  public void setStatusLabel(JLabel statusLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel, JLabel mapCursorPositionLabel, JLabel statusTriangleCountLabel) {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(new UpdateStatusBar(this, statusLabel, statusMemoryLabel, statusRenderablesLabel, mapCursorPositionLabel, statusTriangleCountLabel), 0, 250, TimeUnit.MILLISECONDS);
  }

  public void addOnMapChangeListener(OnMapChangeListener listener) {
    if (!onMapChangeListenerArray.contains(listener, true)) {
      onMapChangeListenerArray.add(listener);
    }
  }

  @Override
  public void onJobStart(Job job) {
    if (job.isBlockingUI()) {
      progressTaskDialog.setLocationRelativeTo(mainWindow);
      progressTaskDialog.setVisible(true);
      mainWindow.setEnabled(false);
    } else {
      jobProgressBar.setVisible(true);
    }
  }

  @Override
  public void onJobError(Job job, Exception e) {
    JOptionPane.showMessageDialog(mainWindow,
      e.toString(),
      "Job error",
      JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void onJobFinish(Job job) {
    if (job.isBlockingUI()) {
      progressTaskDialog.setVisible(false);
      mainWindow.setEnabled(true);
    } else {
      jobProgressBar.setVisible(false);
    }
  }

  @Override
  public void onShadersReload(ShadersManager shaderManager) {

  }

  @Override
  public void onShaderError(ShadersManager shaderManager, BaseShader program) {
    JOptionPane.showMessageDialog(mainWindow,
      program.getLog(),
      "Shader Error",
      JOptionPane.ERROR_MESSAGE);
  }
}
