package macbury.forge.editor.parell;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.Stack;

public class JobManager extends Thread {
  private static final String TAG = "JobManager";
  private Stack<Job> pendingJobs;
  private Job currentJob;
  private boolean running;
  private Array<JobListener> listeners;

  public JobManager() {
    this.pendingJobs    = new Stack<Job>();
    this.listeners      = new Array<JobListener>();
    this.start();
  }

  public void addListener(JobListener listener) {
    synchronized(listeners) {
      listeners.add(listener);
    }
  }

  public void enqueue(Job job) {
    Gdx.app.log(TAG, "Adding job");
    synchronized (pendingJobs) {
      pendingJobs.push(job);
    }
  }

  @Override
  public void run() {
    this.running = true;
    while(running) {
      try {
        if (processJob()) {
          Thread.sleep(50);
        } else {
          Thread.sleep(250);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean processJob() {
    boolean pushedJobs = false;
    while(!pendingJobs.isEmpty()) {
      pushedJobs = true;

      currentJob = pendingJobs.pop();
      onJobStart(currentJob);

      Exception jobError = currentJob.start();
      if (jobError != null) {
        onJobError(currentJob, jobError);
      }
      onJobFinish(currentJob);
    }
    return pushedJobs;
  }

  public void onJobStart(Job job) {
    Gdx.app.log(TAG, "Starting job: " + job.getClass().getSimpleName() );
    for (int i = 0; i < listeners.size; i++) {
      JobListener listener = listeners.get(i);
      synchronized (listener) {
        listener.onJobStart(job);
      }

    }
  }

  public void onJobError(Job job, Exception e) {
    Gdx.app.log(TAG, "Job failed: " + job.getClass().getSimpleName() );
    for (int i = 0; i < listeners.size; i++) {
      JobListener listener = listeners.get(i);
      listener.onJobError(job, e);
    }
  }

  public void onJobFinish(Job job) {
    Gdx.app.log(TAG, "Finished job: " + job.getClass().getSimpleName() );
    for (int i = 0; i < listeners.size; i++) {
      JobListener listener = listeners.get(i);
      listener.onJobFinish(job);
    }
  }

  public void dispose() {
    pendingJobs.clear();
    running = false;
    currentJob = null;
  }
}
