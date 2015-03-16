package macbury.forge.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
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

  public LevelEnv() {
    skyColor     = Color.valueOf("3498db");
    mainLight    = new DirectionalLight();
    mainLight.set(0.8f, 0.8f, 0.8f,-1, -1, 0.5f);

    ambientLight = Color.valueOf("cccccc");
  }

}
