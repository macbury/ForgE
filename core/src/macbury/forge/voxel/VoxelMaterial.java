package macbury.forge.voxel;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by macbury on 20.10.14.
 */
public class VoxelMaterial extends Color {

  public VoxelMaterial(float r, float g, float b, float a) {
    super(r, g, b, a);
  }

  public static VoxelMaterial air() {
    return new VoxelMaterial(0,0,0,0);
  }

  public static VoxelMaterial rgb(int r, int g, int b) {
    return new VoxelMaterial((float)r/255f, (float)g/255f, (float)b/255f, 1f);
  }

  public boolean isAir() {
    return a == 0.0f;
  }
}
