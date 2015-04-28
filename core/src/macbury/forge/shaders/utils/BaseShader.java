package macbury.forge.shaders.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.uniforms.BaseUniform;

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
  private Array<String> uniforms;
  private Array<BaseUniform> globalUniforms;
  protected LevelEnv env;

  public boolean load(ShadersManager manager) {
    ShaderProgram.pedantic = false;
    globalUniforms         = new Array<BaseUniform>();

    String fragmentSrc   = Gdx.files.internal(ShadersManager.SHADERS_PATH +fragment+".frag.glsl").readString();
    String   vertexSrc   = Gdx.files.internal(ShadersManager.SHADERS_PATH +vertex+".vert.glsl").readString();
    if (shader != null) {
      shader.dispose();
    }

    if (uniforms != null) {
      loadGlobalUniforms();
    }

    fragmentSrc = applyDebugPrefixes() + loadHelpers(FRAGMENT_HELPER_KEY) + fragmentSrc;
    vertexSrc   = applyDebugPrefixes() + loadHelpers(VERTEX_HELPER_KEY) + vertexSrc;

    ShaderProgram newShaderProgram = new ShaderProgram(vertexSrc, fragmentSrc);
    if (newShaderProgram.isCompiled()) {
      shader = newShaderProgram;
      return true;
    } else {
      Gdx.app.error(TAG, "Error while compiling shader:");
      Gdx.app.error(TAG, newShaderProgram.getLog());
      Gdx.app.error(TAG, "Fragment SRC === " + fragment);
      Gdx.files.external("/tmp/debug.frag.glsl").writeString(fragment, false);
      Gdx.app.error(TAG, fragmentSrc);
      Gdx.app.error(TAG, "Vertex SRC ===" + vertex);
      Gdx.files.external("/tmp/debug.vert.glsl").writeString(vertex, false);
      Gdx.app.error(TAG, vertexSrc);
      return false;
    }
  }

  private void loadGlobalUniforms() {
    for (String uniformName : uniforms) {
      try {
        String uniformClassName = BaseUniform.CLASS_PREFIX+uniformName;
        Gdx.app.log(TAG, "Adding uniform: "+uniformClassName);
        BaseUniform uniform = (BaseUniform)Class.forName(uniformClassName).newInstance();
        globalUniforms.add(uniform);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private String applyDebugPrefixes() {
    String out = "";
    switch (ForgE.config.renderDebug) {
      case Normals:
        out += "#define normalsDebugFlag\n";
        break;
      case Lighting:
        out += "#define lightingDebugFlag\n";
        break;
    }

    Gdx.app.log(TAG, "Debug prefixes: \n"+out);
    return out;
  }

  private String loadHelpers(String key) {
    String helperSrc = "";
    if (helpers.containsKey(key)) {
      Array<String> helperNames = helpers.get(key);
      for (int i = 0; i < helperNames.size; i++) {
        String helperName = helperNames.get(i);
        helperSrc += loadHelperSrc(helperName);
      }
    }
    return helperSrc;
  }

  private String loadHelperSrc(String helperName) {
    return Gdx.files.internal(ShadersManager.SHADER_HELPERS_PATH +helperName+".glsl").readString() + "\n";
  }

  public String getLog() {
    return shader.getLog();
  }

  /**
   * Setup uniforms and begin new render context
   * @param camera
   * @param context
   */
  public void begin(Camera camera, RenderContext context, LevelEnv env) {
    this.camera  = camera;
    this.context = context;
    this.env     = env;
    shader.begin();
    shader.setUniformMatrix(UNIFORM_PROJECTION_MATRIX, camera.combined);
    context.begin();
    bindGlobalUniforms(camera, context, env);
    afterBegin();
  }

  protected void bindGlobalUniforms(Camera camera, RenderContext context, LevelEnv env) {
    for (BaseUniform uniform : globalUniforms) {
      uniform.bind(shader, env, context, camera);
    }
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
