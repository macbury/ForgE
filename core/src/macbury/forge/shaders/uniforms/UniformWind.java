package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;


/**
 * Created by macbury on 13.03.15.
 */
public class UniformWind extends BaseUniform {
  private static final String UNIFORM_WIND_DISPLACEMENT_TEXTURE = "u_windDisplacementTexture";
  private static final String UNIFORM_WIND_DIR                  = "u_windDirection";

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    if (env.windDisplacementTexture != null && env.windDisplacementTexture.isLoaded()) {
      shader.setUniformi(UNIFORM_WIND_DISPLACEMENT_TEXTURE, context.textureBinder.bind(env.windDisplacementTexture.get()));
      shader.setUniformf(UNIFORM_WIND_DIR, env.windDirection);
    }
  }

  @Override
  public void dispose() {

  }
}
