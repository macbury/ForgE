package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
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
import macbury.forge.editor.windows.MapCreationWindow;
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
public class ProjectController implements JobListener, ShaderReloadListener, MapCreationWindow.Listener {
  private static final String LEVEL_STATE_LOADED_CALLBACK = "onLevelStateLoaded";
  private MainWindow mainWindow;
  public EditorScreen editorScreen;
  private Array<OnMapChangeListener> onMapChangeListenerArray = new Array<OnMapChangeListener>();
  public JobManager jobs;
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
    //TODO: change visibility of main ui
    mainWindow.mainSplitPane.setVisible(true);
    mainWindow.openGlContainer.setVisible(editorScreenEnabled);
    mainWindow.toolsPane.setVisible(editorScreenEnabled);
  }

  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public void newMap() {
    closeMap();
    LevelState newMapState         = new LevelState(ForgE.db);
    MapCreationWindow newMapWindow = new MapCreationWindow(newMapState, this);
    newMapWindow.show(mainWindow);
  }

  @Override
  public void onMapCreationSuccess(MapCreationWindow window, LevelState state) {
    NewLevelJob job = new NewLevelJob(state);
    job.setCallback(this, LEVEL_STATE_LOADED_CALLBACK);
    jobs.enqueue(job);
  }

  public void closeMap() {
    mainWindow.setTitle("");
    if (editorScreen != null) {
      for (OnMapChangeListener listener : onMapChangeListenerArray) {
        listener.onCloseMap(ProjectController.this, ProjectController.this.editorScreen);
      }

      Gdx.app.postRunnable(new Runnable() {
        @Override
        public void run() {
          ForgE.screens.disposeCurrentScreen();
        }
      });

    }

    editorScreen = null;
  }

  public void onLevelStateLoaded(LevelState state, NewLevelJob job) {
    //TODO: save map here
    mainWindow.setTitle(state.name);
    this.editorScreen = new EditorScreen(state);
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.screens.set(editorScreen);

        updateUI();

        for (OnMapChangeListener listener : onMapChangeListenerArray) {
          listener.onNewMap(ProjectController.this, ProjectController.this.editorScreen);
        }
      }
    });
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
  public void onJobStart(final Job job) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        if (job.isBlockingUI()) {
          progressTaskDialog.setLocationRelativeTo(mainWindow);
          progressTaskDialog.setVisible(true);
          mainWindow.setEnabled(false);
        }

        jobProgressBar.setVisible(true);
      }
    });
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
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        jobProgressBar.setVisible(false);
        progressTaskDialog.setVisible(false);
        mainWindow.setEnabled(true);
      }
    });
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

  public void rebuildChunks() {
    if (editorScreen != null) {
      editorScreen.level.terrainMap.rebuildAll();
    }

  }

  public void clearUndoRedo() {
    if (editorScreen != null) {
      editorScreen.changeManager.clear();
    }
  }


}
