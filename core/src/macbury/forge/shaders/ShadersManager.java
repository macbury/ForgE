package macbury.forge.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import macbury.forge.assets.ShaderLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

/**
 * Created by macbury on 16.10.14.
 */
public class ShadersManager {
  private static final String SHADERS_PATH = "shaders/";
  private static final String TAG = "ShadersManager";
  private Array<ShaderReloadListener> shaderReloadListeners;
  private HashMap<String, ShaderProgram> shaders;

  public ShadersManager() {
    this.shaderReloadListeners = new Array<ShaderReloadListener>();
    this.shaders               = new HashMap<String, ShaderProgram>();
    reload();
  }

  public ShaderProgram get(String name) {
    return shaders.get(name);
  }

  public void reload() {
    Json json = new Json();
    for(String key : shaders.keySet()) {
      shaders.get(key).dispose();
    }
    shaders.clear();

    FileHandle[] shadersToImport = Gdx.files.internal(SHADERS_PATH).list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.contains(".json");
      }
    });
    for (FileHandle file : shadersToImport) {
      Gdx.app.log(TAG, "Reloading: "+ file.nameWithoutExtension());
      ShaderLoader loader   = json.fromJson(ShaderLoader.class, file);
      FileHandle   fragment = Gdx.files.internal(SHADERS_PATH+loader.getFragment());
      FileHandle   vertex   = Gdx.files.internal(SHADERS_PATH+loader.getVertex());
      ShaderProgram program = new ShaderProgram(vertex, fragment);
      if (program.isCompiled()) {
        shaders.put(file.nameWithoutExtension(), program);
      } else {
        triggerOnShaderError(program);
        Gdx.app.log(TAG, program.getLog());
      }
    }
    triggerOnShaderReload();
  }

  public void addOnShaderReloadListener(ShaderReloadListener listener) {
    if (shaderReloadListeners.indexOf(listener, true) == -1) {
      shaderReloadListeners.add(listener);
    }
  }

  public void removeOnShaderReloadListener(ShaderReloadListener listener) {
    if (shaderReloadListeners.indexOf(listener, true) != -1) {
      shaderReloadListeners.removeValue(listener, true);
    }
  }

  private void triggerOnShaderReload() {
    for (ShaderReloadListener listener : this.shaderReloadListeners) {
      listener.onShadersReload(this);
    }
  }

  private void triggerOnShaderError(ShaderProgram program) {
    for (ShaderReloadListener listener : this.shaderReloadListeners) {
      listener.onShaderError(this, program);
    }
  }

}
