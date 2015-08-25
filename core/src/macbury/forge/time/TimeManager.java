package macbury.forge.time;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.time.Duration;
import java.util.Date;

/**
 * Created by macbury on 11.08.15.
 */
public class TimeManager implements Disposable {
  private static final float SATELITE_START_ROTATION = 10;
  private static final float SATELITE_END_ROTATION = -190;

  private float duration;
  private long days;
  public static final float HOUR_IN_SECONDS   = 60 * 60;
  public static final float MINUTE_IN_SECONDS = 60;
  public static final float DAY_IN_SECONDS    = 24 * HOUR_IN_SECONDS;
  public static final float DAY_START_HOUR    = 6 * HOUR_IN_SECONDS + 30 * MINUTE_IN_SECONDS;
  public static final float DAY_END_HOUR      = 20 * HOUR_IN_SECONDS + 35 * MINUTE_IN_SECONDS;
  public static final float DAY_LENGTH        = DAY_END_HOUR - DAY_START_HOUR;
  public static final float NIGHT_LENGTH      = DAY_IN_SECONDS - DAY_END_HOUR + DAY_START_HOUR;
  public static final float DEFAULT_TIME      = 18 * HOUR_IN_SECONDS;
  private float sateliteRotation;
  private float sateliteProgress;

  public TimeManager() {
    this.duration = DEFAULT_TIME;
    days          = 1;
  }

  public void update() {
    this.duration += 10 * Gdx.graphics.getDeltaTime();
    if (duration >= DAY_IN_SECONDS) {
      duration = 0;
      days += 1;
    }
    updateRotation();
  }

  private void updateRotation() {
    if (isDay()) {
      sateliteProgress = (duration - DAY_START_HOUR) / DAY_LENGTH;
    } else {
      float nightSeconds = 0.0f;
      if (duration > DAY_END_HOUR) {
        nightSeconds = duration - DAY_END_HOUR;
      } else {
        nightSeconds = duration + DAY_IN_SECONDS - DAY_END_HOUR;
      }
      sateliteProgress = nightSeconds / NIGHT_LENGTH;
    }
    sateliteRotation = MathUtils.round(MathUtils.lerp(SATELITE_START_ROTATION, SATELITE_END_ROTATION, sateliteProgress));
  }

  @Override
  public void dispose() {
    duration = -1;
  }

  public int getHour() {
    return Math.round(duration / HOUR_IN_SECONDS);
  }

  public boolean isDay() {
    return duration >= DAY_START_HOUR && duration <= DAY_END_HOUR;
  }

  public float getSateliteRotation() {
    return sateliteRotation;
  }

  public float getProgress() {
    return (float)duration / DAY_IN_SECONDS % 1.0f;
  }

  public float getProgressAlpha() {
    return getProgress() * 100 % 1;
  }

  public Date getEditingDate() {
    return new Date((long) (duration * 1000));
  }

  public void setEditingDate(Date date) {
    duration = date.getTime() / 1000l;
    updateRotation();
  }

  public int getSeconds() {
    return (int)duration;
  }

  public String getFormattedDuration() {
    return String.format("%d:%02d", (int)Math.floor(duration/3600f), (int)Math.floor((duration % 3600) / 60f));
  }

  public void setDuration(int value) {
    duration = value;
    updateRotation();
  }

  public void setDuration(float otherDuration) {
    this.duration = otherDuration;
  }
}
