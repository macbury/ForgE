package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.graphics.skybox.DayNightSkybox;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 11.08.15.
 */
public class UniformSkyMap extends BaseUniform {
  public final String UNIFORM_SKY_MAP = "u_skyMapTexture";
  public final String UNIFORM_SKY_MAP_PROGRESS = "u_skyMapProgress";
  @Override
  public void defineUniforms() {
    define(UNIFORM_SKY_MAP, Texture.class);
    define(UNIFORM_SKY_MAP_PROGRESS, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    if (DayNightSkybox.class.isInstance(env.skybox)) {
      DayNightSkybox skybox = (DayNightSkybox)env.skybox;
      shader.setUniformi(UNIFORM_SKY_MAP, context.textureBinder.bind(skybox.getSkyMapTexture()));
      shader.setUniformf(UNIFORM_SKY_MAP_PROGRESS, ForgE.time.getProgress());
    }
  }

  @Override
  public void dispose() {

  }
}
