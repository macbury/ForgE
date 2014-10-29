package macbury.forge.shaders.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.level.LevelEnv;

import java.util.HashMap;

/**
 * Created by macbury on 18.10.14.
 */
public abstract class BaseShader implements Disposable {
  public static final String UNIFORM_PROJECTION_MATRIX = "u_projectionMatrix";
  private static final String VERTEX_HELPER_KEY        = "vertex";
  private static final String FRAGMENT_HELPER_KEY      = "fragment";
  private static final String TAG = "BaseShader";

  protected ShaderProgram shader;
  protected Camera camera;
  protected RenderContext context;
  private String fragment;
  private String vertex;
  private HashMap<String, Array<String>> helpers;
  protected LevelEnv env;

  public boolean load(ShadersManager manager) {
    String fragmentSrc   = Gdx.files.internal(ShadersManager.SHADERS_PATH +fragment+".frag.glsl").readString();
    String   vertexSrc   = Gdx.files.internal(ShadersManager.SHADERS_PATH +vertex+".vert.glsl").readString();
    if (shader != null) {
      shader.dispose();
    }

    fragmentSrc = loadHelpers(FRAGMENT_HELPER_KEY) + fragmentSrc;
    vertexSrc   = loadHelpers(VERTEX_HELPER_KEY) + vertexSrc;

    shader                   = new ShaderProgram(vertexSrc, fragmentSrc);
    if (shader.isCompiled()) {
      return true;
    } else {
      Gdx.app.error(TAG, "Error while compiling shader:");
      Gdx.app.error(TAG, getLog());
      Gdx.app.error(TAG, "Fragment SRC === " + fragment);
      Gdx.app.error(TAG, fragmentSrc);
      Gdx.app.error(TAG, "Vertex SRC ===" + vertex);
      Gdx.app.error(TAG, vertexSrc);
      return false;
    }
  }

  private String loadHelpers(String key) {
    String helperSrc = "";
    if (helpers.containsKey(key)) {
      Array<String> helperNames = helpers.get(key);
      for (int i = 0; i < helperNames.size; i++) {
        String helperName = helperNames.get(i);
        helperSrc += Gdx.files.internal(ShadersManager.SHADER_HELPERS_PATH +helperName+".glsl").readString() + "\n";
      }
    }
    return helperSrc;
  }

  public String getLog() {
    return shader.getLog();
  }

  /**
   * Setup uniforms and begin new render context
   * @param camera
   * @param context
   */
  public void begin (Camera camera, RenderContext context, LevelEnv env) {
    this.camera  = camera;
    this.context = context;
    this.env     = env;
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
