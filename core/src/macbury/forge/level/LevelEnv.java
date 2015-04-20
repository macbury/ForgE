package macbury.forge.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 28.10.14.
 */
public class LevelEnv {
  public DirectionalLight mainLight;
  public Color ambientLight;
  public Color skyColor;
  public ChunkMap terrainMap;

  public TextureAsset windDisplacementTexture;
  public Vector2 windDirection = new Vector2(0.1f,0);
  public Vector3 gravity = new Vector3(0, -6f, 0);

  public LevelEnv() {
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
}
