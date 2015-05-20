package macbury.forge.utils;

/**
 * Created by macbury on 20.05.15.
 */
public class OcculsionTimer extends ActionTimer {
  public static final float OCCULSION_TIMER_DELAY = 0.05f;
  private static int rebuildId = 0;
  private int currentRebuild;

  public OcculsionTimer(TimerListener listener) {
    super(OCCULSION_TIMER_DELAY, listener);
    this.currentRebuild = 0;
  }

  @Override
  public void update(float delta) {
    if (rebuildId != currentRebuild) {
      trigger();
      currentRebuild = rebuildId;
    } else {
      super.update(delta);
    }
  }

  public static void runAll() {
    rebuildId += 1;
  }
}
