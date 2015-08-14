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
    return false;
  }

  @Override
  public boolean performCallbackOnOpenGlThread() {
    return false;
  }

  @Override
  public Void perform() {
    listener.onGameStart();

    currentGradleProcess = createGradleProcess();
    if (currentGradleProcess != null) {
      createAndReadBuffer();
    } else {

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
    List<String> argumentsList = new ArrayList<String>();
    argumentsList.add("/bin/bash");
    argumentsList.add("gradlew");
    argumentsList.add("desktop:run");
    argumentsList.add("-PappArgs\"['--fullscreen']\"");

    ProcessBuilder processBuilder = new ProcessBuilder(argumentsList.toArray(new String[argumentsList.size()]));
    processBuilder.redirectErrorStream(true);
    String workingDir = Gdx.files.internal("../../").file().getAbsolutePath();
    Gdx.app.log(TAG, "Running in: " + workingDir);
    processBuilder.directory(new File(workingDir));
    //processBuilder.directory(new File("/home/macbury/Projects/ForgE/"));
    try {
      return processBuilder.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
