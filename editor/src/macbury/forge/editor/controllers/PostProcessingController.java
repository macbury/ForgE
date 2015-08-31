package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by macbury on 31.08.15.
 */
public class PostProcessingController implements OnMapChangeListener, DirectoryWatchJob.DirectoryWatchJobListener {
  private Level currentLevel;
  private JMenu fboMenu;

  public PostProcessingController(DirectoryWatcher directoryWatcher) {
    directoryWatcher.addListener(PostProcessingManager.STORAGE_DIR, this);
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    currentLevel = null;
    updateUI();
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    this.currentLevel = screen.level;
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    if (handle.extension().endsWith("glsl") || handle.extension().endsWith("json")) {
      Gdx.app.postRunnable(new Runnable() {
        @Override
        public void run() {
          reloadPostProcessing();
        }
      });
    }
  }

  private void reloadPostProcessing() {
    if (currentLevel != null) {
      try {
        currentLevel.postProcessing.reload();
        updateUI();
        System.gc();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void setMenu(JMenu fboMenu) {
    this.fboMenu = fboMenu;
    updateUI();
  }

  private void updateUI() {
    fboMenu.removeAll();
    if (fboMenu != null) {
      for (String frameBufferName : ForgE.fb.all().keys()) {
        JMenuItem item = new JMenuItem(frameBufferName);
        item.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

            Gdx.app.postRunnable(new Runnable() {
              @Override
              public void run() {
                File screenshotFile = ForgE.fb.saveAsPng(frameBufferName).file();
                try {
                  Desktop.getDesktop().open(screenshotFile);
                } catch (IOException e1) {
                  e1.printStackTrace();
                }
              }
            });


          }
        });
        fboMenu.add(item);
      }
    }
  }
}
