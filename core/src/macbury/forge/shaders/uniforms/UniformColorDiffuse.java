package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.BaseRenderableMaterialUniform;
import macbury.forge.shaders.utils.BaseRenderableUniform;

/**
 * Created by macbury on 29.04.15.
 */
public class UniformColorDiffuse extends BaseRenderableMaterialUniform<ColorAttribute> {
  private final static String UNIFORM_COLOR_DIFFUSE = "u_colorDiffuse";

  @Override
  public void dispose() {

  }


  @Override
  public void bindAttribute(ShaderProgram shader, RenderContext context, ColorAttribute attribute) {
    shader.setUniformf(UNIFORM_COLOR_DIFFUSE, attribute.color);
  }

  @Override
  public long getAttributeType() {
    return ColorAttribute.Diffuse;
  }
}
