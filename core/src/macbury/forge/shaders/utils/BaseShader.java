package macbury.forge.shaders.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 18.10.14.
 */
public abstract class BaseShader implements Disposable {
  public static final String UNIFORM_PROJECTION_MATRIX = "u_projectionMatrix";

  protected ShaderProgram shader;
  protected Camera camera;
  protected RenderContext context;
  private String fragment;
  private String vertex;

  public boolean load(ShadersManager manager) {
    FileHandle fragmentSrc   = Gdx.files.internal(ShadersManager.SHADERS_PATH +fragment+".frag.glsl");
    FileHandle   vertexSrc   = Gdx.files.internal(ShadersManager.SHADERS_PATH +vertex+".vert.glsl");
    if (shader != null) {
      shader.dispose();
    }
    shader                   = new ShaderProgram(vertexSrc, fragmentSrc);
    if (shader.isCompiled()) {
      return true;
    } else {
      return false;
    }
  }

  public String getLog() {
    return shader.getLog();
  }


  /**
   * Setup uniforms and begin new render context
   * @param camera
   * @param context
   */
  public void begin (Camera camera, RenderContext context) {
    this.camera  = camera;
    this.context = context;
    shader.begin();
    shader.setUniformMatrix(UNIFORM_PROJECTION_MATRIX, camera.combined);
    context.begin();
    afterBegin();
  }

  /**
   * Setup global uniforms here
   */
  public abstract void afterBegin();


  public void end () {
    this.camera = null;
    this.shader.end();
    context.end();
  }

  @Override
  public void dispose() {
    shader.dispose();
  }
}
