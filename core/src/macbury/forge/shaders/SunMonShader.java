package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.batch.renderable.DayNightSkyRenderable;
import macbury.forge.graphics.batch.renderable.SunMonRenderable;
import macbury.forge.graphics.skybox.DayNightSkybox;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 07.08.15.
 */
public class SunMonShader extends RenderableBaseShader<SunMonRenderable> {
  private static final String MODELVIEW_UNIFORM = "u_mvpMatrix";
  private static final String UNIFORM_TEXTURE   = "u_sateliteTexture";
  public static final String UNIFORM_WORLD_TRANSFORM = "u_worldTransform";
  private Matrix4 tempMat = new Matrix4();
  private Matrix4 mvp     = new Matrix4();
  private Vector3 tempVec = new Vector3();
  @Override
  public boolean canRender(Renderable instance) {
    return SunMonRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(SunMonRenderable renderable) {
    tempMat.idt();
    tempMat.set(camera.view);

    tempMat.val[Matrix4.M03] = 0;
    tempMat.val[Matrix4.M13] = 10;
    tempMat.val[Matrix4.M23] = 0;
    tempMat.inv().tra();

    mvp.set(camera.projection).mul(tempMat);

    context.setCullFace(GL20.GL_NONE);
    context.setDepthMask(false);
    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    shader.setUniformMatrix(MODELVIEW_UNIFORM, mvp);

    renderable.worldTransform.idt();
    tempVec.set(camera.position).add(0, 4, 40);

   //
    renderable.worldTransform.setTranslation(tempVec);
    renderable.worldTransform.scl(10);
    DayNightSkybox skybox = (DayNightSkybox)env.skybox;
    shader.setUniformi(UNIFORM_TEXTURE, context.textureBinder.bind(skybox.getSunTexture()));
    shader.setUniformMatrix(UNIFORM_WORLD_TRANSFORM, renderable.worldTransform);
  }

  @Override
  public void afterBegin() {

  }
}
