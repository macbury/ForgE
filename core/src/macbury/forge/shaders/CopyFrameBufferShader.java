package macbury.forge.shaders;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import macbury.forge.shaders.utils.BaseShader;

/**
 * Created by macbury on 26.08.15.
 */
public class CopyFrameBufferShader extends BaseShader {
  private static final String UNIFORM_SRC_TEXTURE = "u_srcTexture";



  public void render(Mesh mesh, int primitiveType, Texture srcTexture) {
    shader.setUniformi(UNIFORM_SRC_TEXTURE, context.textureBinder.bind(srcTexture));
    bindGlobalUniforms(camera, context, env);
    mesh.render(shader, primitiveType);
  }

  @Override
  public void afterBegin() {

  }
}
