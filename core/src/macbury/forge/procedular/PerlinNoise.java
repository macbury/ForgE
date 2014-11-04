package macbury.forge.procedular;

import macbury.forge.utils.MyMath;

import java.util.Random;

/**
 * Created by macbury on 26.05.14.
 */
public class PerlinNoise implements Noise3D {
  private final int[] noisePermutations;

  public PerlinNoise(long seed) {
    Random rand = new Random();
    rand.setSeed(seed);

    noisePermutations = new int[512];
    int[] noiseTable  = new int[256];

    // Init. the noise table
    for (int i = 0; i < 256; i++) {
      noiseTable[i] = i;
    }

    // Shuffle the array
    for (int i = 0; i < 256; i++) {
      int j = rand.nextInt(256);

      int swap = noiseTable[i];
      noiseTable[i] = noiseTable[j];
      noiseTable[j] = swap;
    }

    // Finally replicate the noise permutations in the remaining 256 index positions
    for (int i = 0; i < 256; i++) {
      noisePermutations[i] = noiseTable[i];
      noisePermutations[i + 256] = noiseTable[i];
    }

  }

  /**
   * Returns the noise value at the given position.
   *
   * @param posX Position on the x-axis
   * @param posY Position on the y-axis
   * @param posZ Position on the z-axis
   * @return The noise value
   */
  @Override
  public double noise(double posX, double posY, double posZ) {

    int xInt = (int) MyMath.fastFloor(posX) & 255;
    int yInt = (int) MyMath.fastFloor(posY) & 255;
    int zInt = (int) MyMath.fastFloor(posZ) & 255;

    double x = posX - MyMath.fastFloor(posX);
    double y = posY - MyMath.fastFloor(posY);
    double z = posZ - MyMath.fastFloor(posZ);

    double u = fade(x);
    double v = fade(y);
    double w = fade(z);

    int a = noisePermutations[xInt] + yInt;
    int aa = noisePermutations[a] + zInt;
    int ab = noisePermutations[(a + 1)] + zInt;
    int b = noisePermutations[(xInt + 1)] + yInt;
    int ba = noisePermutations[b] + zInt;
    int bb = noisePermutations[(b + 1)] + zInt;

    return lerp(w, lerp(v, lerp(u, grad(noisePermutations[aa], x, y, z),
          grad(noisePermutations[ba], x - 1, y, z)),
        lerp(u, grad(noisePermutations[ab], x, y - 1, z),
          grad(noisePermutations[bb], x - 1, y - 1, z))),
      lerp(v, lerp(u, grad(noisePermutations[(aa + 1)], x, y, z - 1),
          grad(noisePermutations[(ba + 1)], x - 1, y, z - 1)),
        lerp(u, grad(noisePermutations[(ab + 1)], x, y - 1, z - 1),
          grad(noisePermutations[(bb + 1)], x - 1, y - 1, z - 1))));
  }

  public double simpleNoise(double posX, double posY, double posZ, double amplitude, double frequency) {
    return MyMath.clamp(MyMath.fastAbs(noise((posX * frequency) / amplitude, (posY * frequency) / amplitude, (posZ * frequency) / amplitude)));
  }

  private static double fade(double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  private static double lerp(double t, double a, double b) {
    return a + t * (b - a);
  }

  private static double grad(int hash, double x, double y, double z) {
    int h = hash & 15;
    double u = h < 8 ? x : y;
    double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }

}