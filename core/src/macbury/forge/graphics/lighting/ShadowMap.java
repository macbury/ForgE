package macbury.forge.graphics.lighting;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by macbury on 22.05.15.
 */
public interface ShadowMap {
  public Matrix4 getLightTransMatrix();
  public TextureDescriptor getDepthTexture();
}
