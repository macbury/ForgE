package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.assets.assets.CubemapAsset;
import macbury.forge.graphics.Skybox;

/**
 * Created by macbury on 19.05.15.
 */
public class SkyboxSerializer extends Serializer<Skybox> {
  @Override
  public void write(Kryo kryo, Output output, Skybox object) {
    kryo.writeObject(output, object.rotationDirection);
    output.writeFloat(object.rotationSpeed);
    kryo.writeObjectOrNull(output, object.getSkyboxAsset(), CubemapAsset.class);
  }

  @Override
  public Skybox read(Kryo kryo, Input input, Class<Skybox> type) {
    Skybox skybox = new Skybox(null);
    skybox.rotationDirection.set(kryo.readObject(input, Vector3.class));
    skybox.rotationSpeed = input.readFloat();
    skybox.setSkyboxAsset(kryo.readObjectOrNull(input, CubemapAsset.class));
    return skybox;
  }
}
