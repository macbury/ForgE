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
  private static final String UNIFORM_TEXTURE   = "u_sateliteTexture";

  @Override
  public boolean canRender(Renderable instance) {
    return SunMonRenderable.class.isInstance(instance);
  }

  @Override
  public void beforeRender(SunMonRenderable renderable) {
    context.setCullFace(GL20.GL_NONE);
    context.setDepthMask(false);
    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    DayNightSkybox skybox = (DayNightSkybox)env.skybox;
    shader.setUniformi(UNIFORM_TEXTURE, context.textureBinder.bind(skybox.getSateliteTextureByHour()));
  }

  @Override
  public void afterBegin() {

  }
}
