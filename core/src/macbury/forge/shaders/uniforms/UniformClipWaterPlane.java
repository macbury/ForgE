package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 20.07.15.
 */
public class UniformClipWaterPlane extends BaseUniform {
  public static final float  WATER_HEIGHT             = 1.8f;
  private final static Vector3 reflectionNormal = new Vector3(0,-1,0);
  private final static Vector3 refractionNormal = new Vector3(0,1,0);
  private final static String UNIFORM_CLIP_PANE         = "u_clipWaterPlane";
  private final static String UNIFORM_CLIP_NORMAL       = "u_clipWaterPlane.normal";
  private final static String UNIFORM_CLIP_ELEVATION    = "u_clipWaterPlane.elevation";
  private final static String UNIFORM_CLIP_DEF          = "ClipPane";
  private static final float DISABLED_ELEVATION         = -100;

  @Override
  public void dispose() {

  }


  @Override
  public void defineUniforms() {
    define(UNIFORM_CLIP_PANE, UNIFORM_CLIP_DEF);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformf(UNIFORM_CLIP_ELEVATION, WATER_HEIGHT);
    switch (env.clipMode) {
      case Reflection:
        shader.setUniformf(UNIFORM_CLIP_NORMAL, reflectionNormal);
        break;
      case Refraction:
        shader.setUniformf(UNIFORM_CLIP_NORMAL, refractionNormal);
        break;
      default:
        shader.setUniformf(UNIFORM_CLIP_ELEVATION, DISABLED_ELEVATION);
    }
  }


}
