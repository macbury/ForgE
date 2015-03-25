package macbury.forge.storage.serializers.graphics.math;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 24.03.15.
 */
public class Vector3iSerializer extends Serializer<Vector3i> {
  @Override
  public void write(Kryo kryo, Output output, Vector3i object) {
    output.writeInt(object.x);
    output.writeInt(object.y);
    output.writeInt(object.z);
  }

  @Override
  public Vector3i read(Kryo kryo, Input input, Class<Vector3i> type) {
    Vector3i vector3i = new Vector3i();
    vector3i.x        = input.readInt();
    vector3i.y        = input.readInt();
    vector3i.z        = input.readInt();
    return vector3i;
  }
}
