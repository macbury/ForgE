package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 13.03.15.
 */
public abstract class BaseUniform implements Disposable {
  protected final static Matrix3 tempMatrix = new Matrix3();

  public static final String CLASS_PREFIX = "macbury.forge.shaders.uniforms.Uniform";
  public BaseUniform() {

  }

  public abstract void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera);
}
