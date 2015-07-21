package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.env.LevelEnv;

/**
 * Created by macbury on 13.03.15.
 */
public abstract class BaseUniform implements Disposable {
  protected final static Matrix3 tempMatrix = new Matrix3();
  protected Array<UniformDefinition> uniformDefinitions;
  public static final String CLASS_PREFIX = "macbury.forge.shaders.uniforms.Uniform";
  public BaseUniform() {
    uniformDefinitions = new Array<UniformDefinition>();
    defineUniforms();
  }

  public abstract void defineUniforms();

  protected void define(String uniformName, Class uniformType) {
    uniformDefinitions.add(new UniformDefinition(uniformName, uniformType));
  }

  protected void define(String uniformName, String uniformTypeString) {
    uniformDefinitions.add(new UniformDefinition(uniformName, uniformTypeString));
  }

  public abstract void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera);


  public String getSrc() {
    String output = "";
    for (UniformDefinition definition : uniformDefinitions) {
      output += definition.toString() + '\n';
    }
    return output;
  }

}
