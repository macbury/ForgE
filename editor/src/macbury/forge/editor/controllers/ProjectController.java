package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.parell.Job;
import macbury.forge.editor.parell.JobListener;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.LoadLevelJob;
import macbury.forge.editor.parell.jobs.NewLevelJob;
import macbury.forge.editor.parell.jobs.SaveLevelJob;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by macbury on 18.10.14.
 */
public class ProjectController implements JobListener, ShaderReloadListener, MapCreationWindow.Listener {
  private static final String LEVEL_STATE_LOADED_CALLBACK = "onLevelStateLoaded";
  private static final String TAG = "ProjectController";
  private MainWindow mainWindow;
  public EditorScreen editorScreen;
  private Array<OnMapChangeListener> onMapChangeListenerArray = new Array<OnMapChangeListener>();
  public JobManager jobs;
  private ProgressTaskDialog progressTaskDialog;
  private JProgressBar jobProgressBar;
  private LevelState currentLevelState;

  public void setMainWindow(final MainWindow mainWindow) {
    this.mainWindow         = mainWindow;
    this.jobs               = mainWindow.jobs;

    this.progressTaskDialog = mainWindow.progressTaskDialog;
    this.jobProgressBar     = mainWindow.jobProgressBar;
    jobProgressBar.setVisible(false);

    ForgE.shaders.addOnShaderReloadListener(this);
    this.jobs.addListener(this);

    mainWindow.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {

      }

      @Override
      public void windowClosing(WindowEvent e) {
        if (closeAndSaveChangesMap()) {
          jobs.waitForAllToComplete();
          mainWindow.dispose();
          System.exit(0);
        }
      }

      @Override
      public void windowClosed(WindowEvent e) {

      }

      @Override
      public void windowIconified(WindowEvent e) {

      }

      @Override
      public void windowDeiconified(WindowEvent e) {

      }

      @Override
      public void windowActivated(WindowEvent e) {

      }

      @Override
      public void windowDeactivated(WindowEvent e) {

      }
    });

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

  public void newMap(String storeDir) {
    if (closeAndSaveChangesMap()) {
      LevelState newMapState                 = new LevelState(ForgE.db);
      MapCreationWindow.MapDocument document = new MapCreationWindow.MapDocument(newMapState, storeDir);
      MapCreationWindow newMapWindow = new MapCreationWindow(document, this);
      newMapWindow.show(mainWindow);
    }
  }

  public void newMap() {
    newMap(null);
  }

  @Override
  public void onMapCreationSuccess(MapCreationWindow window, MapCreationWindow.MapDocument document) {
    NewLevelJob job = new NewLevelJob(document.state, document.storeDir);
    job.setCallback(this, LEVEL_STATE_LOADED_CALLBACK);
    jobs.enqueue(job);
  }

  public boolean openMap(FileHandle fileHandle) {
    if (closeAndSaveChangesMap()) {
      LoadLevelJob job = new LoadLevelJob(fileHandle);
      job.setCallback(this, LEVEL_STATE_LOADED_CALLBACK);
      jobs.enqueue(job);
      return true;
    } else {
      return false;
    }
  }

  public void saveMap() {
    SaveLevelJob job = new SaveLevelJob(editorScreen.level.state);
    jobs.enqueue(job);
    editorScreen.changeManager.clear();
  }

  public boolean closeAndSaveChangesMap() {
    if (editorScreen != null && editorScreen.changeManager != null && editorScreen.changeManager.canUndo()) {
      int response = JOptionPane.showOptionDialog(mainWindow,
          "There are changes in map. Do you want to save them?",
          "Save map",
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          null,
          null);

      if (response == 0) {
        saveMap();
        closeMap();
        return true;
      } else if (response == 1) {
        closeMap();
        return true;
      } else {
        return false;
      }
    } else {
      closeMap();
      return true;
    }
  }


  public void deleteFolder(String pathFile) {
    if (closeAndSaveChangesMap()) {
      jobs.waitForAllToComplete();
      int response = JOptionPane.showOptionDialog(mainWindow,
          "Are you sure?",
          "Delete folder",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          null,
          null);

      if (response == 0) {
        Gdx.app.log(TAG, "Removing dir: " + pathFile);
        try {
          Files.deleteIfExists(FileSystems.getDefault().getPath(pathFile));
          //FileUtils.deleteDirectory(new File("directory"));
        } catch (IOException e) {
          e.printStackTrace();
        }

        triggerMapStructureChange();
      }
    }

  }

  public void createFolder(String pathFile) {
    if (closeAndSaveChangesMap()) {
      jobs.waitForAllToComplete();
      String folderName = JOptionPane.showInputDialog("Enter folder name:");
      if (folderName.length() >= 1) {
        File file = new File(pathFile + File.separator + folderName);
        Gdx.app.log(TAG, "Creating directory" + file.getAbsolutePath());
        file.mkdirs();
        triggerMapStructureChange();
      }
    }
  }

  public void moveMap(String source, String target) {
    if (closeAndSaveChangesMap()) {
      jobs.waitForAllToComplete();


      try {
        Path sourcePath = FileSystems.getDefault().getPath(source);
        Path targetPath = FileSystems.getDefault().getPath(target, sourcePath.getFileName().toString());

        Gdx.app.log(TAG, "Move " + sourcePath + " to " + targetPath);
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        triggerMapStructureChange();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void deleteMap(int levelStateId) {
    if (closeAndSaveChangesMap()) {
      jobs.waitForAllToComplete();
      FileHandle levelHandle = ForgE.levels.getFileHandle(levelStateId);
      int response = JOptionPane.showOptionDialog(mainWindow,
          "Are you sure?",
          "Delete map",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          null,
          null);

      if (response == 0) {
        Gdx.app.log(TAG, "Removing map: " + levelHandle.file().getAbsolutePath());
        levelHandle.file().delete();
        triggerMapStructureChange();
      }
    }
  }

  private void triggerMapStructureChange() {
    for (OnMapChangeListener listener : onMapChangeListenerArray) {
      listener.onProjectStructureChange(ProjectController.this);
    }
  }

  public void closeMap() {
    currentLevelState = null;
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
    updateUI();
  }


  public void onLevelStateLoaded(LevelState state, NewLevelJob job) {
    setState(state);
  }

  public void onLevelStateLoaded(LevelState state, LoadLevelJob job) {
    setState(state);
  }

  private void setState(LevelState state) {
    currentLevelState = state;
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


  public LevelState getCurrentLevelState() {
    return currentLevelState;
  }


}
