package macbury.forge.storage.serializers.level.env;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.skybox.Skybox;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.level.env.WaterEnv;

/**
 * Created by macbury on 16.03.15.
 */
public class LevelEnvSerializer extends Serializer<LevelEnv> {
  @Override
  public void write(Kryo kryo, Output output, LevelEnv object) {
    kryo.writeObject(output, object.windDirection);
    kryo.writeObject(output, object.mainLight);
    kryo.writeObject(output, object.skyColor);
    kryo.writeObject(output, object.fogColor);
    kryo.writeObject(output, object.ambientLight);

    kryo.writeObjectOrNull(output, object.getWindDisplacementTextureAsset(), TextureAsset.class);
    kryo.writeObjectOrNull(output, object.skybox, Skybox.class);
    kryo.writeObjectOrNull(output, object.water, WaterEnv.class);

  }

  @Override
  public LevelEnv read(Kryo kryo, Input input, Class<LevelEnv> type) {
    LevelEnv env                = new LevelEnv();
    env.windDirection           = kryo.readObject(input, Vector2.class);
    env.mainLight               = kryo.readObject(input, DirectionalLight.class);
    env.skyColor                = kryo.readObject(input, Color.class);
    env.fogColor                = kryo.readObject(input, Color.class);
    env.ambientLight            = kryo.readObject(input, Color.class);

    env.setWindDisplacementTextureAsset(kryo.readObjectOrNull(input, TextureAsset.class));
    env.skybox                  = kryo.readObjectOrNull(input, Skybox.class);
    env.water                   = kryo.readObjectOrNull(input, WaterEnv.class);
    return env;
  }
}
