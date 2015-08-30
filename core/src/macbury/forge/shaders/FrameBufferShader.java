package macbury.forge.shaders;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.graphics.postprocessing.model.PostProcessingStepFactory;
import macbury.forge.shaders.uniforms.UniformFloat;
import macbury.forge.shaders.uniforms.UniformFrameBuffer;
import macbury.forge.shaders.uniforms.UniformProjectionMatrix;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.ShadersManager;

/**
 * Created by macbury on 20.07.15.
 */
public class FrameBufferShader extends BaseShader {
  @Override
  public void afterBegin() {

  }

  public void render(Mesh mesh, int primitiveType) {
    bindGlobalUniforms(camera, context, env);
    mesh.render(shader, primitiveType);
  }

  @Override
  public FileHandle getVertexFile() {
    return ForgE.files.internal("postprocessing:"+vertex+".vert.glsl");
  }

  @Override
  public FileHandle getFragmentFile() {
    return ForgE.files.internal("postprocessing:" + fragment + ".frag.glsl");
  }

  public void load(ShadersManager shaders, PostProcessingStepFactory factory, PostProcessingManager manager) {
    fragment = factory.fragment;
    vertex   = factory.vertex;
    uniforms = factory.uniforms;
    globalUniforms.add(new UniformProjectionMatrix());
    if (factory.customUniforms != null) {
      for (String uniformName : factory.customUniforms.keys()) {
        String value = factory.customUniforms.get(uniformName);
        try {
          float floatValue                      = Float.parseFloat(value);
          UniformFloat uniformFloat             = new UniformFloat(uniformName, floatValue);
          globalUniforms.add(uniformFloat);
        } catch (NumberFormatException e) {
          UniformFrameBuffer uniformFrameBuffer = new UniformFrameBuffer(uniformName, value);
          globalUniforms.add(uniformFrameBuffer);
        }
      }
    }
    load(shaders);
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}