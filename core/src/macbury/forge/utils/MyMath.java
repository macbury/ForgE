package macbury.forge.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 03.11.14.
 */
public class MyMath {


  public static double fastFloor(double d) {
    int i = (int) d;
    return (d < 0 && d != i) ? i - 1 : i;
  }

  public static float fastFloor(float d) {
    int i = (int) d;
    return (d < 0 && d != i) ? i - 1 : i;
  }

  public static double fastAbs(double d) {
    return (d >= 0) ? d : -d;
  }

  public static float fastAbs(float d) {
    return (d >= 0.0) ? d : -d;
  }

  public static double clamp(double value) {
    if (value > 1.0) {
      return 1.0;
    } else if (value < 0.0) {
      return 0.0;
    }
    return value;
  }

  public static void neg(Vector3 vector3, Vector3 out) {
    out.set(vector3).nor();
    if (vector3.x == 0f) {
      out.x = 1f;
    } else {
      out.x = 0f;
    }

    if (vector3.y == 0f) {
      out.y = 1f;
    } else {
      out.y = 0f;
    }

    if (vector3.z == 0f) {
      out.z = 1f;
    } else {
      out.z = 0f;
    }
  }

  public static void round(Vector3 normal) {
    normal.x = MathUtils.roundPositive(normal.x);
    normal.y = MathUtils.roundPositive(normal.y);
    normal.z = MathUtils.roundPositive(normal.z);
  }
}
