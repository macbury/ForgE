package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by macbury on 16.03.15.
 */
public class DirectionalLightSerializer extends Serializer<DirectionalLight> {
  @Override
  public void write(Kryo kryo, Output output, DirectionalLight object) {
    kryo.writeObject(output, object.direction);
    kryo.writeObject(output, object.color);
  }

  @Override
  public DirectionalLight read(Kryo kryo, Input input, Class<DirectionalLight> type) {
    DirectionalLight light = new DirectionalLight();
    light.direction.set(kryo.readObject(input, Vector3.class));
    light.color.set(kryo.readObject(input, Color.class));
    return light;
  }
}
