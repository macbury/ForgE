package macbury.forge.utils;

/**
 * Created by macbury on 04.09.14.
 */
public class ActionTimer {
  private TimerListener listener;
  private float time;
  private float currentTime;
  private boolean running = false;
  public ActionTimer(float time, TimerListener listener) {
    this.listener = listener;
    setInterval(time);
  }
  public void start() {
    if (!running)
      currentTime = time;
    running = true;

  }
  public void stop() {
    this.running = false;
  }
  public void update(float delta) {
    if (this.running) {
      currentTime += delta;
      if (currentTime > time) {
        trigger();
      }
    }
  }

  protected void trigger() {
    currentTime = 0;
    listener.onTimerTick(this);
  }

  public void setInterval(float time) {
    this.time = time;
  }

  public interface TimerListener {
    public void onTimerTick(ActionTimer timer);
  }
}
