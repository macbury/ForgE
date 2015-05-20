package macbury.forge.shaders;

import com.badlogic.gdx.graphics.Mesh;
import macbury.forge.shaders.utils.BaseShader;

/**
 * Created by macbury on 20.05.15.
 */
public class FrameBufferShader extends BaseShader {
  @Override
  public void afterBegin() {

  }

  public void render(Mesh mesh, int primitiveType) {
    bindGlobalUniforms(camera, context, env);
    mesh.render(shader, primitiveType);
  }
}
