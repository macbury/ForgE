package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 21.07.15.
 */
public class UniformWaterRefractionDUDVTexture  extends BaseUniform {
  public final String UNIFORM_DUDV_TEXTURE = "u_waterRefractionDUDVMap";

  @Override
  public void defineUniforms() {
    define(UNIFORM_DUDV_TEXTURE, Texture.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    GLTexture texture = env.water.getWaterDisplacementTexture();
    if (texture != null) {
      shader.setUniformi(UNIFORM_DUDV_TEXTURE, context.textureBinder.bind(texture));
    }

  }

  @Override
  public void dispose() {

  }
}