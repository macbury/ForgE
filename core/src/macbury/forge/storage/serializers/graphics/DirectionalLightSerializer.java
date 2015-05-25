package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.lighting.DirectionalShadowLight;
import macbury.forge.graphics.lighting.SunLight;

/**
 * Created by macbury on 16.03.15.
 */
public class DirectionalLightSerializer extends Serializer<DirectionalShadowLight> {
  @Override
  public void write(Kryo kryo, Output output, DirectionalShadowLight object) {
    kryo.writeObject(output, object.direction);
    kryo.writeObject(output, object.color);
  }

  @Override
  public DirectionalShadowLight read(Kryo kryo, Input input, Class<DirectionalShadowLight> type) {
    DirectionalShadowLight light = new DirectionalShadowLight();
    light.direction.set(kryo.readObject(input, Vector3.class));
    light.color.set(kryo.readObject(input, Color.class));
    return light;
  }
}
