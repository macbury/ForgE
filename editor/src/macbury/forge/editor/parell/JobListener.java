package macbury.forge.editor.parell;

/**
 * Created by macbury on 06.11.14.
 */

public interface JobListener {
  public void onJobStart(Job job);
  public void onJobError(Job job, Exception e);
  public void onJobFinish(Job job);
}