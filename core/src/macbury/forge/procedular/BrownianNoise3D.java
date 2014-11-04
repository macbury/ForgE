package macbury.forge.procedular;

/**
 * Created by macbury on 26.05.14.
 */
public class BrownianNoise3D extends BrownianNoise implements Noise3D {

  private final Noise3D other;

  /**
   * Uses the default number of octaves
   * @param other the noise to use as a basis
   */
  public BrownianNoise3D(Noise3D other) {
    this.other = other;
  }

  /**
   * @param octaves the number of octaves to use
   */
  public BrownianNoise3D(Noise3D other, int octaves) {
    this(other);
    setOctaves(octaves);
  }

  /**
   * Returns Fractional Brownian Motion at the given position.
   *
   * @param x Position on the x-axis
   * @param y Position on the y-axis
   * @param z Position on the z-axis
   * @return The noise value in the range [-getScale()..getScale()]
   */
  @Override
  public double noise(double x, double y, double z) {
    double result = 0.0;

    double workingX = x;
    double workingY = y;
    double workingZ = z;
    for (int i = 0; i < getOctaves(); i++) {
      result += other.noise(workingX, workingY, workingZ) * getSpectralWeight(i);

      workingX *= getLacunarity();
      workingY *= getLacunarity();
      workingZ *= getLacunarity();
    }

    return result;
  }

}