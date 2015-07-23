package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 22.07.15.
 */
public class UniformWaterNormalTexture extends BaseUniform {
  public final String UNIFORM_NORMAL_A_TEXTURE = "u_waterNormalATexture";
  public final String UNIFORM_NORMAL_B_TEXTURE = "u_waterNormalBTexture";
  @Override
  public void defineUniforms() {
    define(UNIFORM_NORMAL_A_TEXTURE, Texture.class);
    define(UNIFORM_NORMAL_B_TEXTURE, Texture.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    GLTexture normalTexture = env.water.getWaterNormalMapATexture();
    if (normalTexture != null) {
      shader.setUniformi(UNIFORM_NORMAL_A_TEXTURE, context.textureBinder.bind(normalTexture));
    }

    normalTexture = env.water.getWaterNormalMapBTexture();
    if (normalTexture != null) {
      shader.setUniformi(UNIFORM_NORMAL_B_TEXTURE, context.textureBinder.bind(normalTexture));
    }
  }

  @Override
  public void dispose() {

  }
}
