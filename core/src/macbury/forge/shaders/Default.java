package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import macbury.forge.graphics.batch.renderable.SpriteRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.shaders.utils.ColorRenderableBaseShader;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 29.04.15.
 */
public class Default  extends ColorRenderableBaseShader<Renderable> {

  @Override
  public boolean canRender(Renderable instance) {
    return instance.getClass() == Renderable.class && instance.material != null;
  }

  @Override
  public void beforeRender(Renderable renderable) {

  }

  @Override
  public void afterBegin() {
    context.setCullFace(GL30.GL_BACK);
    context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    context.setDepthMask(true);
    context.setDepthTest(GL30.GL_LEQUAL);
  }
}
