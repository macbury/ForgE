package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.Mesh;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

/**
 * Created by macbury on 20.10.14.
 */
public abstract class RenderableBaseShader<T extends BaseRenderable> extends BaseShader {
  protected Mesh currentMesh;
  public final String UNIFORM_WORLD_TRANSFORM = "u_worldTransform";
  public final String UNIFORM_NORMAL_MATRIX   = "u_normalMatrix";

  public final String UNIFORM_AMBIENT_LIGHT          = "u_ambientLight";
  public final String UNIFORM_MAIN_LIGHT_COLOR       = "u_mainLight.color";
  public final String UNIFORM_MAIN_LIGHT_DIRECTION   = "u_mainLight.direction";

  public abstract boolean canRender (BaseRenderable instance);

  /**
   * Setup local uniforms for renderable
   * @param renderable
   */
  public abstract void beforeRender(final T renderable);

  public void render(final T renderable) {
    beforeRender(renderable);

    if (currentMesh != renderable.mesh) {
      if (currentMesh != null) {
        currentMesh.unbind(shader);
      }
      currentMesh = renderable.mesh;
      currentMesh.bind(shader);
    }
    currentMesh.render(shader, renderable.primitiveType, 0, currentMesh.getMaxIndices() > 0 ? currentMesh.getMaxIndices() : currentMesh.getMaxVertices(), false);
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
