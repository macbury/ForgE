package macbury.forge.shaders.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

/**
 * Created by macbury on 18.10.14.
 */
public abstract class BaseShader<T extends BaseRenderable> implements Disposable {
  public static final String UNIFORM_PROJECTION_MATRIX = "u_projectionMatrix";

  protected ShaderProgram shader;
  protected Camera camera;
  protected RenderContext context;
  private String fragment;
  private String vertex;
  protected Mesh currentMesh;

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

  public abstract boolean canRender (BaseRenderable instance);

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

  public void render(final T renderable) {
    beforeRender(renderable);

    if (currentMesh != renderable.mesh) {
      if (currentMesh != null) {
        currentMesh.unbind(shader);
      }
      currentMesh = renderable.mesh;
      currentMesh.bind(shader);
    }
    currentMesh.render(shader, renderable.primitiveType, 0, currentMesh.getMaxIndices() > 0 ? currentMesh.getMaxIndices() : currentMesh.getMaxVertices(), false);
  }

  /**
   * Setup global uniforms here
   */
  public abstract void afterBegin();

  /**
   * Setup local uniforms for renderable
   * @param renderable
   */
  public abstract void beforeRender(final T renderable);

  public void end () {
    if (currentMesh != null) {
      currentMesh.unbind(shader);
    }
    currentMesh = null;
    this.camera = null;
    this.shader.end();
    context.end();
  }

  @Override
  public void dispose() {
    shader.dispose();
  }
}
