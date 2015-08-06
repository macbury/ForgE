package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import macbury.forge.graphics.batch.renderable.CubemapSkyboxRenderable;
import macbury.forge.graphics.batch.renderable.DayNightSkyRenderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 06.08.15.
 */
public class DayNightSkyShader extends RenderableBaseShader<DayNightSkyRenderable> {
  private static final String MODELVIEW_UNIFORM = "u_mvpMatrix";
  private Matrix4 tempMat = new Matrix4();
  private Matrix4 mvp     = new Matrix4();
  @Override
  public boolean canRender(Renderable instance) {
    return DayNightSkyRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(DayNightSkyRenderable renderable) {
    tempMat.idt();
    tempMat.set(camera.view);

    tempMat.val[Matrix4.M03] = 0;
    tempMat.val[Matrix4.M13] = 0;
    tempMat.val[Matrix4.M23] = 0;
    tempMat.inv().tra();

    mvp.set(camera.projection).mul(tempMat);

    context.setCullFace(GL20.GL_FRONT);
    context.setDepthMask(false);

    shader.setUniformMatrix(MODELVIEW_UNIFORM, mvp);
  }

  @Override
  public void afterBegin() {

  }
}
