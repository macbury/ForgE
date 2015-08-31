package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import macbury.forge.ForgE;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.run_game.RunGameJob;
import macbury.forge.editor.windows.RunningGameConsoleFrame;
import macbury.forge.screens.AbstractScreen;

import javax.swing.*;

/**
 * Created by macbury on 06.05.15.
 */
public class PlayerController implements RunGameJob.Listener {
  private static final String TAG = "PlayerController";
  private final ProjectController projectController;
  private final JobManager jobs;
  private RunGameJob currentRunGameJob;
  private AbstractScreen currentScreen;

  public PlayerController(ProjectController projectController, JobManager jobs) {
    this.projectController = projectController;
    this.jobs              = jobs;
  }

  public void runGame() {
    if (projectController.haveOpenedMap()) {
      projectController.saveMap();
    }

    currentRunGameJob = new RunGameJob(this);
    jobs.enqueue(currentRunGameJob);
  }


  private void showConsole() {
    currentScreen = ForgE.screens.current();
    ForgE.screens.set(null);
  }

  private void restoreScreen() {

    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.screens.set(currentScreen);
        currentScreen = null;
      }
    });
  }

  @Override
  public void onGameStart() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        showConsole();
      }
    });
  }

  @Override
  public void onGameEnd() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        restoreScreen();
      }
    });

  }

  @Override
  public void onLog(String line) {
    Gdx.app.log(TAG, line);
   // if (currentFrame != null)
   //   currentFrame.putToLog(line);
  }
}
