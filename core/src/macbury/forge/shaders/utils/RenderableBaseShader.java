package macbury.forge.shaders.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.ForgE;
import macbury.forge.graphics.GraphicsUtils;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

/**
 * Created by macbury on 20.10.14.
 */
public abstract class RenderableBaseShader<T extends Renderable> extends BaseShader {
  protected Mesh currentMesh;

  public abstract boolean canRender (Renderable instance);

  /**
   * Setup local uniforms for renderable
   * @param renderable
   */
  public abstract void beforeRender(final T renderable);

  public void render(final T renderable) {
    if (currentMesh != renderable.mesh) {
      if (currentMesh != null) {
        currentMesh.unbind(shader);
      }
      currentMesh = renderable.mesh;
      currentMesh.bind(shader);
    }

    beforeRender(renderable);
    bindLocalUniforms(renderable);
    renderWithCurrentMesh(renderable);
  }

  protected void renderWithCurrentMesh(final T renderable) {
    currentMesh.render(shader, renderable.primitiveType, renderable.meshPartOffset, currentMesh.getMaxIndices() > 0 ? currentMesh.getMaxIndices() : currentMesh.getMaxVertices(), false);
  }

  @Override
  public void end() {
    super.end();
    if (currentMesh != null) {
      currentMesh.unbind(shader);
    }
    currentMesh = null;
  }
}
