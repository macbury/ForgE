package macbury.forge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import macbury.forge.utils.KVStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by macbury on 18.10.14.
 */
public class Config {


  public enum RenderDebug {
    Textured, Wireframe, Normals, Lighting
  }
  public int bloomTextureSize        = 256;
  public int resolutionWidth         = 1360;
  public int resolutionHeight        = 768;
  public boolean fullscreen          = false;

  public int reflectionBufferSize    = 512;
  public int refractionBufferSize    = 512;
  public int farShadowMapSize        = 512;
  public int nearShadowMapSize       = 1024;
  public float nearShadowDistance    = 10;

  public boolean generateWireframe   = false;
  public boolean debug               = false;
  public RenderDebug renderDebug     = RenderDebug.Textured;
  public boolean renderDynamicOctree = false;
  public boolean renderStaticOctree  = false;
  public boolean renderBoundingBox   = false;
  public boolean cacheGeometry       = false;
  public boolean renderBulletDebug   = false;
  public String editor               = "atom";

  public static Config load(String namespace) {
    Json json       = new Json();
    byte[] encoded  = new byte[0];
    try {
      encoded = Files.readAllBytes(Paths.get("./config_" + namespace + ".json"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    String text     = new String(encoded);
    return json.fromJson(Config.class, text);
  }

  public void setRenderDebugTo(RenderDebug debug) {
    renderDebug = debug;
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.shaders.reload();
      }
    });

  }
}
