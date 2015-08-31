package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.graphics.skybox.DayNightSkybox;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 31.08.15.
 */
public class UniformLightPositionOnScreen extends BaseUniform {
  private static final String UNIFORM_POS_ON_SCREEN = "u_lightPositonOnScreen";
  private static final String UNIFORM_DOT_DIR_VIEW_NAME = "u_lightDirDotViewDir";

  private static final float OFF_SCREEN_RENDER_RATIO = 2.0F;
  private   final static Vector3 tempVec = new Vector3();
  @Override
  public void defineUniforms() {
    define(UNIFORM_POS_ON_SCREEN, Vector2.class);
    define(UNIFORM_DOT_DIR_VIEW_NAME, Float.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    if (ForgE.time.isDay()) {
      shader.setUniformf(
          UNIFORM_DOT_DIR_VIEW_NAME,
          tempVec.set(env.mainLight.direction).dot(env.mainCamera.direction)
      );
    } else {
      shader.setUniformf( UNIFORM_DOT_DIR_VIEW_NAME, 1 );
    }


    if (DayNightSkybox.class.isInstance(env.skybox)) {
      DayNightSkybox dayNightSkybox = (DayNightSkybox)env.skybox;
      dayNightSkybox.getLightPosition(tempVec);
      env.mainCamera.project(tempVec);

      /*tempVec.x /= env.mainCamera.far;
      tempVec.y /= env.mainCamera.far;

      tempVec.x = (tempVec.x + 1.0f) / 2.0f;
      tempVec.y = (tempVec.y + 1.0f) / 2.0f;
      //tempVec.z /= camera.far;
*/
      shader.setUniformf(
          UNIFORM_POS_ON_SCREEN,
          tempVec.x / env.mainCamera.viewportWidth,
          tempVec.y / env.mainCamera.viewportHeight
      );
    }
  }

  @Override
  public void dispose() {

  }
}
