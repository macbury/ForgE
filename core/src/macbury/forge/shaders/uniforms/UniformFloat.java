package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 30.08.15.
 */
public class UniformFloat extends BaseUniform {
  private final String uniformName;
  private final float value;

  public UniformFloat(String uniformName, float value) {
    super();
    this.uniformName = uniformName;
    this.value       = value;
  }

  @Override
  public void defineUniforms() {
    define(uniformName, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(uniformName, value);
  }

  @Override
  public void dispose() {

  }
}
