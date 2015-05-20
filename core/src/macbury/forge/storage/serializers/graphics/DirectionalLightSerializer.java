package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.lighting.SunLight;

/**
 * Created by macbury on 16.03.15.
 */
public class DirectionalLightSerializer extends Serializer<SunLight> {
  @Override
  public void write(Kryo kryo, Output output, SunLight object) {
    kryo.writeObject(output, object.direction);
    kryo.writeObject(output, object.color);
  }

  @Override
  public SunLight read(Kryo kryo, Input input, Class<SunLight> type) {
    SunLight light = new SunLight();
    light.direction.set(kryo.readObject(input, Vector3.class));
    light.color.set(kryo.readObject(input, Color.class));
    return light;
  }
}
