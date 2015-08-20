package macbury.forge.shaders.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.ShaderError;

import java.io.File;
import java.sql.SQLInput;
import java.util.HashMap;

/**
 * Created by macbury on 18.10.14.
 */
public abstract class BaseShader implements Disposable {
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
  private Array<String> structs;
  private Array<BaseUniform> globalUniforms;
  private Array<BaseRenderableUniform> localUniforms;
  protected LevelEnv env;
  private String uniformsSrc = "";
  private String name;
  private String depth;
  private BaseShader depthShader;
  private ShaderError currentError;

  public boolean load(ShadersManager manager) {
    this.globalUniforms               = new Array<BaseUniform>();
    this.localUniforms                = new Array<BaseRenderableUniform>();
    this.currentError                 = null;
    ShaderProgram.pedantic = false;

    String fragmentSrc   = getFragmentFile().readString();
    String   vertexSrc   = getVertexFile().readString();
    if (shader != null) {
      shader.dispose();
    }

    if (uniforms != null) {
      loadGlobalAndLocalUniforms();
    }

    fragmentSrc = applyDebugPrefixes() + applyStructs() + applyUniformsSrc() + loadHelpers(FRAGMENT_HELPER_KEY) + fragmentSrc;
    vertexSrc   = applyDebugPrefixes() + applyStructs() + applyUniformsSrc() + loadHelpers(VERTEX_HELPER_KEY) + vertexSrc;

    ShaderProgram newShaderProgram = new ShaderProgram(vertexSrc, fragmentSrc);
    if (newShaderProgram.isCompiled()) {
      shader = newShaderProgram;
      return true;
    } else {
      currentError = new ShaderError(newShaderProgram, fragmentSrc, vertexSrc);
      currentError.print();
      return false;
    }
  }

  public FileHandle getVertexFile() {
    return Gdx.files.internal(ShadersManager.SHADERS_PATH +vertex+".vert.glsl");
  }

  public FileHandle getFragmentFile() {
    return Gdx.files.internal(ShadersManager.SHADERS_PATH +fragment+".frag.glsl");
  }

  private String applyStructs() {
    String output = "";
    if (structs != null) {
      for(String structureName : structs) {
        output += Gdx.files.internal(ShadersManager.SHADER_STRUCTS_PATH +structureName+".glsl").readString() + "\n";
      }
    }
    return output;
  }

  private String applyUniformsSrc() {
    return uniformsSrc;
  }

  private void loadGlobalAndLocalUniforms() {
    this.uniformsSrc = "";
    for (String uniformName : uniforms) {
      try {
        String uniformClassName = BaseUniform.CLASS_PREFIX+uniformName;

        BaseUniform uniform = (BaseUniform)Class.forName(uniformClassName).newInstance();
        if (BaseRenderableUniform.class.isInstance(uniform)) {
          Gdx.app.log(TAG, "Adding local uniform: "+uniformClassName);
          localUniforms.add((BaseRenderableUniform) uniform);
        } else {
          Gdx.app.log(TAG, "Adding global uniform: "+uniformClassName);
          globalUniforms.add(uniform);
        }
        uniformsSrc += "// Uniform: "+uniformClassName+'\n' + uniform.getSrc();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        throw new GdxRuntimeException("Could not find uniform " + uniformName);
      }
    }

    Gdx.app.log(TAG, "Local uniforms: " + localUniforms.size);
    Gdx.app.log(TAG, "Global uniforms: " + globalUniforms.size);
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

    context.begin();
    bindGlobalUniforms(camera, context, env);
    afterBegin();
  }

  protected void bindGlobalUniforms(Camera camera, RenderContext context, LevelEnv env) {
    for (BaseUniform uniform : globalUniforms) {
      uniform.bind(shader, env, context, camera);
    }
  }

  protected void bindLocalUniforms(Renderable renderable) {
    for (BaseRenderableUniform uniform : localUniforms) {
      uniform.bind(shader, env, context, camera, renderable);
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
    shader = null;
    depthShader = null;
    camera = null;
    context = null;
    globalUniforms.clear();
    localUniforms.clear();
    globalUniforms = null;
    localUniforms  = null;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public File getJsonFile() {
    return Gdx.files.internal(ShadersManager.SHADERS_PATH + name + ".json").file();
  }

  public ShaderError getCurrentError() {
    return currentError;
  }

  public void assignDepthShader(ShadersManager shaderProvider) {
    if (depth != null) {
      depthShader = shaderProvider.get(depth);
      if (depthShader == null) {
        throw new GdxRuntimeException("Could not find depth shader: " + depth + " for " + name);
      }
    }
  }
}
