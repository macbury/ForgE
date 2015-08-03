package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 21.07.15.
 */
public class UniformWaterSettings extends BaseUniform {
  public final String UNIFORM_WATER_COLOR               = "u_waterColor";
  public final String UNIFORM_WATER_COLOR_TINT          = "u_waterColorTint";
  public final String UNIFORM_WATER_WAVE_STRENGTH       = "u_waterWaveStrength";
  public final String UNIFORM_WATER_DISPLACEMENT_TILING = "u_waterDisplacementTiling";
  public final String UNIFORM_WATER_SPEED               = "u_waterSpeed";
  public final String UNIFORM_WATER_REFRACTION_FACTOR   = "u_waterRefractionFactor";
  public final String UNIFORM_WATER_REFLECTIVITY        = "u_waterReflectivity";
  public final String UNIFORM_WATER_SHINE_DAMPER        = "u_waterShineDamper";
  @Override
  public void defineUniforms() {
    define(UNIFORM_WATER_COLOR, Color.class);
    define(UNIFORM_WATER_WAVE_STRENGTH, Float.class);
    define(UNIFORM_WATER_DISPLACEMENT_TILING, Float.class);
    define(UNIFORM_WATER_SPEED, Float.class);
    define(UNIFORM_WATER_COLOR_TINT, Float.class);
    define(UNIFORM_WATER_REFRACTION_FACTOR, Float.class);
    define(UNIFORM_WATER_REFLECTIVITY, Float.class);
    define(UNIFORM_WATER_SHINE_DAMPER, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_WATER_COLOR, env.water.color);
    shader.setUniformf(UNIFORM_WATER_WAVE_STRENGTH, env.water.waveStrength);
    shader.setUniformf(UNIFORM_WATER_DISPLACEMENT_TILING, env.water.displacementTiling);
    shader.setUniformf(UNIFORM_WATER_SPEED, env.water.waterSpeed);
    shader.setUniformf(UNIFORM_WATER_COLOR_TINT, env.water.colorTint);
    shader.setUniformf(UNIFORM_WATER_REFRACTION_FACTOR, env.water.refractiveFactor);
    shader.setUniformf(UNIFORM_WATER_REFLECTIVITY, env.water.reflectivity);
    shader.setUniformf(UNIFORM_WATER_SHINE_DAMPER, env.water.shineDamper);
  }

  @Override
  public void dispose() {

  }
}
