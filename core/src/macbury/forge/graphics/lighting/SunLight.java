package macbury.forge.graphics.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.camera.GameCamera;

/**
 * Created by macbury on 20.05.15.
 */
public class SunLight extends GameCamera {
  public final Color color = new Color(0, 0, 0, 1);

  public SunLight() {
    super();
    fieldOfView = 120;
    far         = 100;
    near        = 1f;
  }

  public SunLight set (final SunLight copyFrom) {
    return set(copyFrom.color, copyFrom.direction);
  }

  public SunLight set (final Color color, final Vector3 direction) {
    if (color != null) this.color.set(color);
    if (direction != null) this.direction.set(direction).nor();
    return this;
  }

  public SunLight set (final float r, final float g, final float b, final Vector3 direction) {
    this.color.set(r, g, b, 1f);
    if (direction != null) this.direction.set(direction).nor();
    return this;
  }

  public SunLight set (final Color color, final float dirX, final float dirY, final float dirZ) {
    if (color != null) this.color.set(color);
    this.direction.set(dirX, dirY, dirZ).nor();
    return this;
  }

  public SunLight set (final float r, final float g, final float b, final float dirX, final float dirY, final float dirZ) {
    this.color.set(r, g, b, 1f);
    this.direction.set(dirX, dirY, dirZ).nor();
    return this;
  }
}
