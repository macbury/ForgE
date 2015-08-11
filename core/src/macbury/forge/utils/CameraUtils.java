package macbury.forge.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.*;
import macbury.forge.shaders.uniforms.UniformClipWaterPlane;

/**
 * Created by macbury on 20.07.15.
 */
public class CameraUtils {
  private static Quaternion tempRotation = new Quaternion();
  private static Vector3 tempVec = new Vector3();
  private static Matrix4 tempMat  = new Matrix4();
  private static Vector3 invertVector = new Vector3(0, -1, 0);
  protected final static Vector3 dir    = new Vector3();
  protected static Vector3 tmp          = new Vector3();
  protected static Vector3 tmp2         = new Vector3();

  public static void invertPitch(Camera camera) {
    camera.direction.y *= -1f;
    camera.update();
  }

  public static void lookAt(Vector3 position, Vector3 target, Vector3 up, Quaternion outRotation) {
    dir.set(target).sub(position).nor();
    tmp.set(up).crs(dir).nor();
    tmp2.set(dir).crs(tmp).nor();
    outRotation.setFromAxes(tmp.x, tmp2.x, dir.x, tmp.y, tmp2.y, dir.y, tmp.z, tmp2.z, dir.z);
  }
}
