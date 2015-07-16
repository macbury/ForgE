package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;


/**
 * Created by macbury on 13.03.15.
 */
public class UniformWind extends BaseUniform {
  private static final String UNIFORM_WIND_DISPLACEMENT_TEXTURE = "u_windDisplacementTexture";
  private static final String UNIFORM_WIND_DIR                  = "u_windDirection";

  @Override
  public void defineUniforms() {
    define(UNIFORM_WIND_DISPLACEMENT_TEXTURE, Texture.class);
    define(UNIFORM_WIND_DIR, Vector2.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    if (env.getWindDisplacementTextureAsset() != null) {
      shader.setUniformi(UNIFORM_WIND_DISPLACEMENT_TEXTURE, context.textureBinder.bind(env.getWindDisplacementTexture()));
      shader.setUniformf(UNIFORM_WIND_DIR, env.windDirection);
    }
  }

  @Override
  public void dispose() {

  }
}
