package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 16.07.15.
 */
public class UniformProjectionMatrix extends BaseUniform {
  public static final String UNIFORM_PROJECTION_MATRIX = "u_projectionMatrix";

  @Override
  public void defineUniforms() {
    define(UNIFORM_PROJECTION_MATRIX, Matrix4.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformMatrix(UNIFORM_PROJECTION_MATRIX, camera.combined);
  }

  @Override
  public void dispose() {

  }
}
