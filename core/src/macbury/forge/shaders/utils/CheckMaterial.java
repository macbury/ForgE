package macbury.forge.shaders.utils;


import com.badlogic.gdx.graphics.g3d.Material;
import macbury.forge.shaders.WaterShader;
import macbury.forge.shaders.attributes.SolidTerrainAttribute;
import macbury.forge.shaders.attributes.WaterAttribute;

/**
 * Created by macbury on 20.07.15.
 */
public class CheckMaterial {
  public static boolean ifHaveWaterAttribute(Material material) {
    return material != null && material.has(WaterAttribute.Type);
  }

  public static boolean ifHaveSolidTerrainAttribute(Material material) {
    return material != null && material.has(SolidTerrainAttribute.Type);
  }
}
