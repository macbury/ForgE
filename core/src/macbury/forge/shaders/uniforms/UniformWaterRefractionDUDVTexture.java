package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.LevelEnv;
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
    shader.setUniformi(UNIFORM_DUDV_TEXTURE, context.textureBinder.bind(env.getWaterDisplacementTexture()));
  }

  @Override
  public void dispose() {

  }
}