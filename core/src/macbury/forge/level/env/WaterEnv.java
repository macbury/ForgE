package macbury.forge.level.env;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.assets.assets.TextureAsset;

/**
 * Created by macbury on 21.07.15.
 */
public class WaterEnv implements Disposable {

  private static final float WATER_BLOCK_HEIGHT         = 0.75f;
  private TextureAsset waterDisplacementTextureAsset;
  private TextureAsset waterNormalMapTextureAsset;
  public LevelEnv.ClipMode clipMode = LevelEnv.ClipMode.None;
  private Texture waterDisplacementTexture;
  public float elevation          = 1f;
  public Color color              = new Color(0.0f, 0.3f, 0.5f, 1.0f);
  public float waterSpeed         = 0.05f;
  public float waveStrength       = 0.005f;
  public float displacementTiling = 0.1f;
  public float colorTint          = 0.2f;
  public float refractiveFactor   = 0.5f;

  public TextureAsset getWaterNormalMapTextureAsset() {
    return waterNormalMapTextureAsset;
  }

  public void setWaterNormalMapTextureAsset(TextureAsset waterNormalMapTextureAsset) {
    this.waterNormalMapTextureAsset = waterNormalMapTextureAsset;
  }

  public TextureAsset getWaterDisplacementTextureAsset() {
    return waterDisplacementTextureAsset;
  }

  public void setWaterDisplacementTextureAsset(TextureAsset newWaterDisplacementTextureAsset) {
    waterDisplacementTexture = null;
    if (waterDisplacementTextureAsset != null) {
      waterDisplacementTextureAsset.release();
      waterDisplacementTextureAsset = null;
      waterDisplacementTextureAsset = null;
    }
    this.waterDisplacementTextureAsset = newWaterDisplacementTextureAsset;
    if(waterDisplacementTextureAsset != null)
      waterDisplacementTextureAsset.retain();
  }

  public GLTexture getWaterDisplacementTexture() {
    if (waterDisplacementTexture == null && getWaterDisplacementTextureAsset() != null) {
      waterDisplacementTexture = getWaterDisplacementTextureAsset().get();
    }
    return waterDisplacementTexture;
  }

  @Override
  public void dispose() {
    setWaterDisplacementTextureAsset(null);
    setWaterNormalMapTextureAsset(null);
  }

  public float getElevationWithWaterBlockHeight() {
    return elevation + WATER_BLOCK_HEIGHT;
  }
}
