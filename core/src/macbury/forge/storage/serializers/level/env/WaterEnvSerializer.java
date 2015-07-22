package macbury.forge.storage.serializers.level.env;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.level.env.WaterEnv;

/**
 * Created by macbury on 21.07.15.
 */
public class WaterEnvSerializer extends Serializer<WaterEnv> {
  @Override
  public void write(Kryo kryo, Output output, WaterEnv object) {
    kryo.writeObjectOrNull(output, object.getWaterDisplacementTextureAsset(), TextureAsset.class);
    kryo.writeObjectOrNull(output, object.getWaterNormalMapTextureAsset(), TextureAsset.class);
    kryo.writeObjectOrNull(output, object.color, Color.class);
    output.writeFloat(object.elevation);
    output.writeFloat(object.displacementTiling);
    output.writeFloat(object.waterSpeed);
    output.writeFloat(object.waveStrength);
    output.writeFloat(object.colorTint);
    output.writeFloat(object.refractiveFactor);
  }

  @Override
  public WaterEnv read(Kryo kryo, Input input, Class<WaterEnv> type) {
    WaterEnv env            = new WaterEnv();
    env.setWaterDisplacementTextureAsset(kryo.readObjectOrNull(input, TextureAsset.class));
    env.setWaterNormalMapTextureAsset(kryo.readObjectOrNull(input, TextureAsset.class));
    env.color               = kryo.readObjectOrNull(input, Color.class);
    env.elevation           = input.readFloat();
    env.displacementTiling  = input.readFloat();
    env.waterSpeed          = input.readFloat();
    env.waveStrength        = input.readFloat();
    env.colorTint           = input.readFloat();
    env.refractiveFactor    = input.readFloat();
    return env;
  }
}
