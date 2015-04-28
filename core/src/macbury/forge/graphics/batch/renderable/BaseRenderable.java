package macbury.forge.graphics.batch.renderable;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Matrix4;
import macbury.forge.graphics.batch.Wireframe;

/**
 * Created by macbury on 18.10.14.
 */
public class BaseRenderable extends Renderable {
  public Wireframe wireframe;
  public int triangleCount = 0;

  public boolean haveTransparency() {
    return haveMaterial() && material.has(BlendingAttribute.Type) && ((BlendingAttribute)material.get(BlendingAttribute.Type)).blended;
  }

  private boolean haveMaterial() {
    return material != null;
  }
}
