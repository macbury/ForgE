package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.light.OrthographicDirectionalLight;

/**
 * Created by macbury on 14.08.15.
 */
public class OrthographicDirectionalLightSerializer extends Serializer<OrthographicDirectionalLight> {
  @Override
  public void write(Kryo kryo, Output output, OrthographicDirectionalLight object) {
    kryo.writeObject(output, object.direction);
    kryo.writeObject(output, object.color);
  }

  @Override
  public OrthographicDirectionalLight read(Kryo kryo, Input input, Class<OrthographicDirectionalLight> type) {
    OrthographicDirectionalLight light = new OrthographicDirectionalLight();
    light.direction.set(kryo.readObject(input, Vector3.class));
    light.color.set(kryo.readObject(input, Color.class));
    return light;
  }
}
