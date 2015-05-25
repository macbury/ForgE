package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.FrameBufferManager;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.05.15.
 */
public class UniformSunDepthMap extends BaseUniform {
  private static final String UNIFORM_SUN_DEPTH_MAP = "u_sunDepthMap";

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(UNIFORM_SUN_DEPTH_MAP, context.textureBinder.bind(env.mainLight.getDepthTexture()));
  }

  @Override
  public void dispose() {

  }
}
