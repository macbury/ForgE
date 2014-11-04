package macbury.forge.procedular;


/**
 * Created by macbury on 26.05.14.
 */
public interface Noise3D {
  /**
   * Returns the noise value at the given position.
   *
   * @param x Position on the x-axis
   * @param y Position on the y-axis
   * @param z Position on the z-axis
   * @return The noise value
   */
  double noise(double x, double y, double z);
}