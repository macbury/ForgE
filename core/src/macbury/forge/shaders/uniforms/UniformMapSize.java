package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformMapSize extends BaseUniform {
  private final String  UNIFORM_MAP_SIZE = "u_mapSize";

  @Override
  public void defineUniforms() {
    define(UNIFORM_MAP_SIZE, Vector2.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_MAP_SIZE, env.terrainMap.getWidth(), env.terrainMap.getDepth());
  }

  @Override
  public void dispose() {

  }
}
