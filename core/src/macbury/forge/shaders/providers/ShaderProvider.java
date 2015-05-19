package macbury.forge.shaders.providers;

import com.badlogic.gdx.graphics.g3d.Renderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 19.05.15.
 */
public interface ShaderProvider {
  public RenderableBaseShader provide(Renderable renderable);
  public void dispose();
}
