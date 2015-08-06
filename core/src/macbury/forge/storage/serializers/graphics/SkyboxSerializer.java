package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.assets.assets.CubemapAsset;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.skybox.CubemapSkybox;
import macbury.forge.graphics.skybox.DayNightSkybox;
import macbury.forge.graphics.skybox.Skybox;

/**
 * Created by macbury on 19.05.15.
 */
public class SkyboxSerializer extends Serializer<Skybox> {
  @Override
  public void write(Kryo kryo, Output output, Skybox object) {
    kryo.writeClass(output, object.getClass());
    if (CubemapSkybox.class.isInstance(object)) {
      CubemapSkybox cubemapSkybox = (CubemapSkybox) object;
      kryo.writeObject(output, cubemapSkybox.rotationDirection);
      output.writeFloat(cubemapSkybox.rotationSpeed);
      kryo.writeObjectOrNull(output, cubemapSkybox.getSkyboxAsset(), CubemapAsset.class);
    } else {
      DayNightSkybox dayNightSkybox = (DayNightSkybox) object;
      kryo.writeObject(output, dayNightSkybox.getMoonAsset());
      kryo.writeObject(output, dayNightSkybox.getSunAsset());
      //throw new GdxRuntimeException("Implement!");
    }
  }

  @Override
  public Skybox read(Kryo kryo, Input input, Class<Skybox> type) {
    Registration skyboxClass = kryo.readClass(input);
    if (skyboxClass.getType() == CubemapSkybox.class) {
      CubemapSkybox skybox = new CubemapSkybox(null);
      skybox.rotationDirection.set(kryo.readObject(input, Vector3.class));
      skybox.rotationSpeed = input.readFloat();
      skybox.setSkyboxAsset(kryo.readObjectOrNull(input, CubemapAsset.class));
      return skybox;
    } else {
      DayNightSkybox dayNightSkybox = new DayNightSkybox();
      dayNightSkybox.setMoonAsset(kryo.readObjectOrNull(input, TextureAsset.class));
      dayNightSkybox.setSunAsset(kryo.readObjectOrNull(input, TextureAsset.class));
     // throw new GdxRuntimeException("Implement!");
      return dayNightSkybox;
    }
  }
}
