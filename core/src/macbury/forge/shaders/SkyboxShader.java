package macbury.forge.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.batch.renderable.SkyboxRenderable;
import macbury.forge.shaders.uniforms.UniformWorldTransform;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.05.15.
 */
public class SkyboxShader extends RenderableBaseShader<SkyboxRenderable> {
  private static final String MODELVIEW_UNIFORM = "u_mvpMatrix";
  private static final String UNIFORM_CUBEMAP = "u_skyboxCubemap";
  private Matrix4 tempMat = new Matrix4();
  private Matrix4 mvp     = new Matrix4();

  @Override
  public boolean canRender(Renderable instance) {
    return SkyboxRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(SkyboxRenderable renderable) {
    tempMat.idt();
    tempMat.set(camera.view);

    tempMat.val[Matrix4.M03] = 0;
    tempMat.val[Matrix4.M13] = 0;
    tempMat.val[Matrix4.M23] = 0;
    tempMat.rotate(env.skybox.rotationDirection, env.skybox.rotation);
    tempMat.inv().tra();

    mvp.set(camera.projection).mul(tempMat);

    context.setCullFace(GL20.GL_FRONT);
    context.setDepthMask(false);

    shader.setUniformMatrix(MODELVIEW_UNIFORM, mvp);
    shader.setUniformi(
        UNIFORM_CUBEMAP,
        context.textureBinder.bind(env.skybox.getSkyboxCubemap())
    );
  }

  @Override
  public void afterBegin() {

  }
}
