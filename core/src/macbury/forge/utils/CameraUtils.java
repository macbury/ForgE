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
  public static void invertPitch(Camera camera) {
    camera.direction.y *= -1f;
    camera.update();
    //camera.view.getRotation(tempRotation);
    //camera.view.setFromEulerAngles(tempRotation.getYaw(), -tempRotation.getPitch(), tempRotation.getRoll());
  }
}
