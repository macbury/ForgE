package macbury.forge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import macbury.forge.utils.KVStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by macbury on 18.10.14.
 */
public class Config extends KVStorage<Config.Key> {
  public enum Key {
    ResolutionWidth, ResolutionHeight, Fullscreen, Debug, NearShadowMapSize, FarShadowMapSize, BloomTextureSize, ReflectionBufferSize, RefractionBufferSize,
    GenerateWireframe, NearShadowDistance, RenderDebug, RenderDynamicOctree, RenderStaticOctree, RenderBoundingBox, RenderBulletDebug, Editor,
  }

  public enum RenderDebug {
    Textured, Wireframe, Normals, Lighting
  }

  @Override
  public void setDefaults() {
    putInt(Key.NearShadowMapSize, 1024);
    putInt(Key.NearShadowMapSize, 1024);
    putInt(Key.NearShadowMapSize, 1024);
    putInt(Key.FarShadowMapSize, 1024);
    putInt(Key.ResolutionWidth, 1360);
    putInt(Key.ResolutionHeight, 768);
    putInt(Key.BloomTextureSize, 512);
    putInt(Key.ReflectionBufferSize, 512);
    putInt(Key.RefractionBufferSize, 512);
    putInt(Key.NearShadowDistance, 10);
    putBool(Key.Fullscreen, false);
    putBool(Key.Debug, false);
    putBool(Key.RenderDynamicOctree, false);
    putBool(Key.RenderStaticOctree, false);
    putBool(Key.RenderBoundingBox, false);
    putBool(Key.RenderBulletDebug, false);
    putBool(Key.GenerateWireframe, false);
    putString(Key.Editor, "atom");
    putRenderDebug(RenderDebug.Textured);
  }


  public static Config load(String namespace) {
    Json json       = new Json();
    byte[] encoded  = new byte[0];
    try {
      encoded = Files.readAllBytes(Paths.get("./config_" + namespace + ".json"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    String text     = new String(encoded);
    ObjectMap<String, Object> values = json.fromJson(ObjectMap.class, text);
    Config config   = new Config();
    for(String rawKey : values.keys()) {
      Key key = Key.valueOf(rawKey);
      config.putObject(key, values.get(rawKey));
    }
    return config;
  }

  public void putRenderDebug(RenderDebug renderDebug) {
    putObject(Key.RenderDebug, renderDebug);
  }

  public RenderDebug getRenderDebug() {
    return (RenderDebug)getObject(Key.RenderDebug);
  }

}
