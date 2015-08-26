package macbury.forge.shaders;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Created by macbury on 26.08.15.
 */
public class CopyFrameBufferShader extends FrameBufferShader {
  private static final String UNIFORM_SRC_TEXTURE = "u_srcTexture";



  public void render(Mesh mesh, int primitiveType, Texture srcTexture) {
    shader.setUniformi(UNIFORM_SRC_TEXTURE, context.textureBinder.bind(srcTexture));
    render(mesh, primitiveType);
  }
}
