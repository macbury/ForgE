package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 13.03.15.
 */
public abstract class BaseUniform implements Disposable {
  public static final String CLASS_PREFIX = "macbury.forge.shaders.uniforms.Uniform";
  public BaseUniform() {

  }

  public abstract void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera);
}
