package macbury.forge.utils;

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


}
