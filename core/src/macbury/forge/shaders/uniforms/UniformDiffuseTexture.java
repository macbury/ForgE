package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.shaders.utils.BaseRenderableMaterialUniform;

/**
 * Created by macbury on 03.05.15.
 */
public class UniformDiffuseTexture extends BaseRenderableMaterialUniform<TextureAttribute> {
  private static final String UNIFORM_DIFFUSE_TEXTURE = "u_diffuseTexture";

  @Override
  public void bindAttribute(ShaderProgram shader, RenderContext context, TextureAttribute attribute) {
    shader.setUniformi(UNIFORM_DIFFUSE_TEXTURE, context.textureBinder.bind(attribute.textureDescription));
  }

  @Override
  public long getAttributeType() {
    return TextureAttribute.Diffuse;
  }

  @Override
  public void dispose() {

  }
}
