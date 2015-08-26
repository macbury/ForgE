package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 26.08.15.
 */
public abstract class UniformFrameBuffer extends BaseUniform {

  public abstract String getFrameBufferName();
  public abstract String getUniformName();

  @Override
  public void defineUniforms() {
    define(getUniformName(), Texture.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(getUniformName(), context.textureBinder.bind(ForgE.fb.get(getFrameBufferName()).getColorBufferTexture()));
  }

  @Override
  public void dispose() {

  }
}
