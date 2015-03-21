package macbury.forge.storage.serializers.graphics.math;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by macbury on 16.03.15.
 */
public class Vector3Serializer extends Serializer<Vector3> {
  @Override
  public void write(Kryo kryo, Output output, Vector3 object) {
    output.writeFloat(object.x);
    output.writeFloat(object.y);
    output.writeFloat(object.z);
  }

  @Override
  public Vector3 read(Kryo kryo, Input input, Class<Vector3> type) {
    Vector3 vector3 = new Vector3();
    vector3.x       = input.readFloat();
    vector3.y       = input.readFloat();
    vector3.z       = input.readFloat();
    return vector3;
  }
}
