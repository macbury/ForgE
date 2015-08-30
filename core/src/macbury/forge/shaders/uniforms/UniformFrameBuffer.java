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
public class UniformFrameBuffer extends BaseUniform {
  private final String uniformName;
  private final String fbName;

  public UniformFrameBuffer(String uniformName, String fbName) {
    super();
    this.uniformName = uniformName;
    this.fbName      = fbName;
  }

  @Override
  public void defineUniforms() {
    define(uniformName, Texture.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(uniformName, context.textureBinder.bind(ForgE.fb.get(fbName).getColorBufferTexture()));
  }

  @Override
  public void dispose() {

  }
}
