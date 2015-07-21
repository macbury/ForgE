package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.07.15.
 */
public class UniformRefractionTexture extends BaseUniform {
  public final String UNIFORM_REFRACTION_TEXTURE = "u_refractionTexture";

  @Override
  public void defineUniforms() {
    define(UNIFORM_REFRACTION_TEXTURE, Texture.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(UNIFORM_REFRACTION_TEXTURE, context.textureBinder.bind(ForgE.fb.get(Fbo.FRAMEBUFFER_REFRACTIONS).getColorBufferTexture()));
  }

  @Override
  public void dispose() {

  }
}
