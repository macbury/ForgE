package macbury.forge.level.env;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.skybox.CubemapSkybox;
import macbury.forge.graphics.skybox.Skybox;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 28.10.14.
 */
public class LevelEnv implements Disposable {

  public enum ClipMode {
    None, Reflection, Refraction
  }
  public Skybox skybox;
  public DirectionalLight mainLight;
  public Color ambientLight;
  public Color skyColor;
  public ChunkMap terrainMap;
  private TextureAsset windDisplacementTextureAsset;

  public Vector2 windDirection = new Vector2(0.1f,0);
  public Vector3 gravity = new Vector3(0, -6f, 0);
  private Texture windDisplacementTexture;

  public WaterEnv water;

  public LevelEnv() {
    water        = new WaterEnv();
    skybox       = new CubemapSkybox(null);
    skyColor     = Color.valueOf("3498db");
    mainLight    = new DirectionalLight();
    mainLight.set(1f, 1f, 1f,-1, -1, 0.5f);
    ambientLight = Color.GRAY;
  }

  public Vector3 getGravity() {
    return gravity;
  }

  public void setGravity(Vector3 gravity) {
    this.gravity = gravity;
  }

  public Vector2 getWindDirection() {
    return windDirection;
  }

  public void setWindDirection(Vector2 windDirection) {
    this.windDirection = windDirection;
  }

  public Color getSkyColor() {
    return skyColor;
  }

  public void setSkyColor(Color skyColor) {
    this.skyColor = skyColor;
  }

  public Color getAmbientLight() {
    return ambientLight;
  }

  public void setAmbientLight(Color ambientLight) {
    this.ambientLight = ambientLight;
  }

  public DirectionalLight getMainLight() {
    return mainLight;
  }

  public void setMainLight(DirectionalLight mainLight) {
    this.mainLight = mainLight;
  }

  @Override
  public void dispose() {
    setWindDisplacementTextureAsset(null);
    if (skybox != null)
      skybox.dispose();
    skybox = null;
  }

  public GLTexture getWindDisplacementTexture() {
    if (windDisplacementTexture == null) {
      windDisplacementTexture = windDisplacementTextureAsset.get();
    }
    return windDisplacementTexture;
  }


  public void setWindDisplacementTextureAsset(TextureAsset newWindDisplacementTextureAsset) {
    windDisplacementTexture = null;
    if (windDisplacementTextureAsset != null) {
      windDisplacementTextureAsset.release();
      windDisplacementTextureAsset = null;
      windDisplacementTexture = null;
    }
    this.windDisplacementTextureAsset = newWindDisplacementTextureAsset;
    if(windDisplacementTextureAsset != null)
      windDisplacementTextureAsset.retain();
  }

  public TextureAsset getWindDisplacementTextureAsset() {
    return windDisplacementTextureAsset;
  }

}
