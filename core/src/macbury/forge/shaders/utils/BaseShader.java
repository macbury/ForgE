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
import macbury.forge.shaders.DepthShader;
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
  protected String fragment;
  protected String vertex;
  protected HashMap<String, Array<String>> helpers;
  protected Array<String> uniforms;
  protected Array<String> structs;
  protected Array<BaseUniform> globalUniforms;
  protected Array<BaseRenderableUniform> localUniforms;
  protected LevelEnv env;
  protected String uniformsSrc = "";
  protected String name;
  protected String depth;
  protected DepthShader depthShader;
  protected ShaderError currentError;

  public BaseShader() {
    this.globalUniforms               = new Array<BaseUniform>();
    this.localUniforms                = new Array<BaseRenderableUniform>();
  }

  public boolean load(ShadersManager manager) {
    this.currentError                 = null;
    ShaderProgram.pedantic            = false;

    String fragmentSrc   = getFragmentFile().readString();
    String   vertexSrc   = getVertexFile().readString();
    if (shader != null) {
      shader.dispose();
    }

    if (uniforms != null) {
      loadGlobalAndLocalUniforms();
    }

    getUniformsDefinitionsSrc();

    fragmentSrc = applyPrecisions() + applyDebugPrefixes() + applyStructs() + applyUniformsSrc() + loadHelpers(FRAGMENT_HELPER_KEY) + fragmentSrc;
    vertexSrc   = applyPrecisions() + applyDebugPrefixes() + applyStructs() + applyUniformsSrc() + loadHelpers(VERTEX_HELPER_KEY) + vertexSrc;

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

  private String applyPrecisions() {
    return "#ifdef GL_ES \n" +
        "#define LOWP lowp\n" +
        "#define MED mediump\n" +
        "#define HIGH highp\n" +
        "precision mediump float;\n" +
        "#else\n" +
        "#define MED\n" +
        "#define LOWP\n" +
        "#define HIGH\n" +
        "#endif\n";
  }

  public FileHandle getVertexFile() {
    return ForgE.files.internal(ShadersManager.SHADERS_PATH +vertex+".vert.glsl");
  }

  public FileHandle getFragmentFile() {
    return ForgE.files.internal(ShadersManager.SHADERS_PATH +fragment+".frag.glsl");
  }

  private String applyStructs() {
    String output = "";
    if (structs != null) {
      for(String structureName : structs) {
        output += ForgE.files.internal(ShadersManager.SHADER_STRUCTS_PATH +structureName+".glsl").readString() + "\n";
      }
    }
    return output;
  }

  private String applyUniformsSrc() {
    return uniformsSrc;
  }

  private void loadGlobalAndLocalUniforms() {
    for (String uniformName : uniforms) {
      try {
        String uniformClassName = BaseUniform.CLASS_PREFIX+uniformName;

        BaseUniform uniform = (BaseUniform)Class.forName(uniformClassName).newInstance();
        if (BaseRenderableUniform.class.isInstance(uniform)) {
          ForgE.log(TAG, "Adding local uniform: "+uniformClassName);
          localUniforms.add((BaseRenderableUniform) uniform);
        } else {
          ForgE.log(TAG, "Adding global uniform: "+uniformClassName);
          globalUniforms.add(uniform);
        }

      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        throw new GdxRuntimeException("Could not find uniform " + uniformName);
      }
    }

    ForgE.log(TAG, "Local uniforms: " + localUniforms.size);
    ForgE.log(TAG, "Global uniforms: " + globalUniforms.size);
  }

  private void getUniformsDefinitionsSrc() {
    this.uniformsSrc = "";
    for (BaseUniform uniform : globalUniforms) {
      uniformsSrc += uniform.getSrc();
    }

    for (BaseUniform uniform : localUniforms) {
      uniformsSrc += uniform.getSrc();
    }
  }

  private String applyDebugPrefixes() {
    String out = "";
    switch (ForgE.config.getRenderDebug()) {
      case Normals:
        out += "#define normalsDebugFlag\n";
        break;
      case Lighting:
        out += "#define lightingDebugFlag\n";
        break;
    }

    ForgE.log(TAG, "Debug prefixes: \n"+out);
    return out;
  }

  private String loadHelpers(String key) {
    String helperSrc = "";
    if (helpers != null) {
      if (helpers.containsKey(key)) {
        Array<String> helperNames = helpers.get(key);
        for (int i = 0; i < helperNames.size; i++) {
          String helperName = helperNames.get(i);
          helperSrc += loadHelperSrc(helperName);
        }
      }
    }

    return helperSrc;
  }

  private String loadHelperSrc(String helperName) {
    return ForgE.files.internal(ShadersManager.SHADER_HELPERS_PATH +helperName+".glsl").readString() + "\n";
  }

  public String getLog() {
    return shader.getLog();
  }

  public boolean isValid() {
    return shader != null;
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
    if (isValid()) {
      shader.begin();

      context.begin();
      bindGlobalUniforms(camera, context, env);
      afterBegin();
    }
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
    if (isValid()) {
      this.shader.end();
    }

    context.end();
  }

  @Override
  public void dispose() {
    if (isValid())
      shader.dispose();
    shader = null;
    depthShader = null;
    camera = null;
    context = null;
    if (globalUniforms != null) {
      for (BaseUniform uniform : globalUniforms) {
        uniform.dispose();
      }
    }

    if (localUniforms != null) {
      for (BaseUniform uniform : localUniforms) {
        uniform.dispose();
      }
    }

    globalUniforms.clear();
    localUniforms.clear();
    globalUniforms = null;
    localUniforms  = null;
  }

  public DepthShader getDepthShader() {
    return depthShader;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public File getJsonFile() {
    return ForgE.files.internal(ShadersManager.SHADERS_PATH + name + ".json").file();
  }

  public ShaderError getCurrentError() {
    return currentError;
  }

  public void assignDepthShader(ShadersManager shaderProvider) {
    if (depth != null) {
      depthShader = (DepthShader)shaderProvider.get(depth);
      if (depthShader == null) {
        throw new GdxRuntimeException("Could not find depth shader: " + depth + " for " + name);
      }
    }
  }
}
