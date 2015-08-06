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
  private TextureAsset waterNormalMapATextureAsset;
  private TextureAsset waterNormalMapBTextureAsset;
  public LevelEnv.ClipMode clipMode = LevelEnv.ClipMode.None;
  private Texture waterDisplacementTexture;
  private Texture waterNormalATexture;
  public float elevation          = 1f;
  public Color color              = new Color(0.0f, 0.3f, 0.5f, 1.0f);
  public float waterSpeed         = 0.02f;
  public float waveStrength       = 0.005f;
  public float displacementTiling = 20f;
  public float colorTint          = 0.2f;
  public float refractiveFactor   = 0.5f;
  public float shineDamper        = 12.0f;
  public float reflectivity       = 0.9f;

  private Texture waterNormalBTexture;

  public TextureAsset getWaterNormalMapATextureAsset() {
    return waterNormalMapATextureAsset;
  }

  public void setWaterNormalMapATextureAsset(TextureAsset newWaterNormalMapTextureAsset) {
    waterNormalATexture = null;
    if (waterNormalMapATextureAsset != null) {
      waterNormalMapATextureAsset.release();
      waterNormalMapATextureAsset = null;
    }
    this.waterNormalMapATextureAsset = newWaterNormalMapTextureAsset;
    if(waterNormalMapATextureAsset != null)
      waterNormalMapATextureAsset.retain();
  }

  public TextureAsset getWaterDisplacementTextureAsset() {
    return waterDisplacementTextureAsset;
  }

  public void setWaterDisplacementTextureAsset(TextureAsset newWaterDisplacementTextureAsset) {
    waterDisplacementTexture = null;
    if (waterDisplacementTextureAsset != null) {
      waterDisplacementTextureAsset.release();
      waterDisplacementTextureAsset = null;
    }
    this.waterDisplacementTextureAsset = newWaterDisplacementTextureAsset;
    if(waterDisplacementTextureAsset != null)
      waterDisplacementTextureAsset.retain();
  }

  public GLTexture getWaterDisplacementTexture() {
    if (waterDisplacementTexture == null && getWaterDisplacementTextureAsset() != null) {
      waterDisplacementTexture = getWaterDisplacementTextureAsset().get();
      waterDisplacementTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    return waterDisplacementTexture;
  }

  @Override
  public void dispose() {
    setWaterDisplacementTextureAsset(null);
    setWaterNormalMapATextureAsset(null);
    setWaterNormalMapBTextureAsset(null);
  }

  public float getElevationWithWaterBlockHeight() {
    return elevation + WATER_BLOCK_HEIGHT;
  }

  public GLTexture getWaterNormalMapATexture() {
    if (waterNormalATexture == null && getWaterNormalMapATextureAsset() != null) {
      waterNormalATexture = getWaterNormalMapATextureAsset().get();
      waterNormalATexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    return waterNormalATexture;
  }

  public GLTexture getWaterNormalMapBTexture() {
    if (waterNormalBTexture == null && getWaterNormalMapBTextureAsset() != null) {
      waterNormalBTexture = getWaterNormalMapBTextureAsset().get();
      waterNormalBTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    return waterNormalBTexture;
  }

  public TextureAsset getWaterNormalMapBTextureAsset() {
    return waterNormalMapBTextureAsset;
  }

  public void setWaterNormalMapBTextureAsset(TextureAsset newWaterNormalMapBTextureAsset) {
    waterNormalBTexture = null;
    if (waterNormalMapBTextureAsset != null) {
      waterNormalMapBTextureAsset.release();
      waterNormalMapBTextureAsset = null;
    }
    this.waterNormalMapBTextureAsset = newWaterNormalMapBTextureAsset;
    if(waterNormalMapBTextureAsset != null)
      waterNormalMapBTextureAsset.retain();
  }
}
