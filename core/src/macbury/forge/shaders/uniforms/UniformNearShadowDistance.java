package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;
import macbury.forge.utils.KVStorage;

/**
 * Created by macbury on 21.08.15.
 */
public class UniformNearShadowDistance extends BaseUniform implements KVStorage.OnChangeListener {
  public static final String UNIFORM_NEAR_SHADOW_DISTANCE      = "u_nearShadowDistance";
  private static final float SHADOW_OFFSET = 2;
  private float distance;

  public UniformNearShadowDistance() {
    super();
    ForgE.config.addListener(this);
    updateDistance();
  }

  @Override
  public void defineUniforms() {
    define(UNIFORM_NEAR_SHADOW_DISTANCE, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_NEAR_SHADOW_DISTANCE, distance);
  }

  @Override
  public void dispose() {
    ForgE.config.removeListener(this);
  }

  @Override
  public void onKeyChange(Object key, KVStorage storage) {
    updateDistance();
  }

  private void updateDistance() {
    this.distance = ForgE.config.getInt(Config.Key.NearShadowDistance) + SHADOW_OFFSET;
  }
}
