package macbury.forge.editor.parell.jobs.run_game;

import com.badlogic.gdx.Gdx;
import macbury.forge.editor.parell.Job;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbury on 06.05.15.
 */
public class RunGameJob extends Job<Void> {
  private static final String TAG = "RunGameJob";
  private Listener listener;
  private Process currentGradleProcess;
  private BufferedReader bufferReader;

  public RunGameJob(Listener listener) {
    super(null);
    this.listener = listener;
  }

  @Override
  public boolean isBlockingUI() {
    return true;
  }

  @Override
  public boolean performCallbackOnOpenGlThread() {
    return false;
  }

  @Override
  public Void perform() {
    listener.onGameStart();

    currentGradleProcess = createGradleProcess();
    try {
      currentGradleProcess.waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    listener.onGameEnd();
    return null;
  }

  private void logException(Exception e) {
    listener.onLog(e.getLocalizedMessage());
  }

  private void createAndReadBuffer() {
    this.bufferReader    = new BufferedReader(new InputStreamReader(currentGradleProcess.getInputStream()));
    while (currentGradleProcess.isAlive()) {
      try {
        if (this.bufferReader.ready()) {
          while (this.bufferReader.ready()) {
            listener.onLog(this.bufferReader.readLine());
          }
        } else {
          Thread.sleep(100);
        }

      } catch (IOException e) {
        e.printStackTrace();
        logException(e);
      } catch (InterruptedException e) {
        e.printStackTrace();
        logException(e);
      }
    }

    try {
      bufferReader.close();
    } catch (IOException e) {
      e.printStackTrace();
      logException(e);

    }
  }

  private Process createGradleProcess() {
    String workingDir = null;
    try {
      workingDir = Gdx.files.internal("../../").file().getCanonicalPath();
    } catch (IOException e) {

      e.printStackTrace();
    }

    String command = "/usr/bin/gnome-terminal --working-directory='"+workingDir+"' --command='./gradlew desktop:run'";
    Gdx.app.log(TAG, "Exec: " + command);
    Gdx.app.log(TAG, "Running in: " + workingDir);
    try {
      return Runtime.getRuntime().exec(new String[]{"/bin/sh" , "-ic",command});
    } catch (IOException e) {
      e.printStackTrace();
    }
/*
    ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-ic",command);
    processBuilder.redirectErrorStream(true);
    try {
      return processBuilder.start();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
    return null;
  }

  @Override
  public void dispose() {
    listener = null;
    currentGradleProcess = null;

  }

  public interface Listener {
    public void onGameStart();
    public void onGameEnd();
    public void onLog(String line);
  }
}
