package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformFog extends BaseUniform {
  public final String UNIFORM_FOG_COLOR = "u_fogColor";

  @Override
  public void defineUniforms() {
    define(UNIFORM_FOG_COLOR, Color.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_FOG_COLOR, env.fogColor);
  }

  @Override
  public void dispose() {

  }
}
