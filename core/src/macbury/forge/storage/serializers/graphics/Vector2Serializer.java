package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by macbury on 16.03.15.
 */
public class Vector2Serializer extends Serializer<Vector2> {
  @Override
  public void write(Kryo kryo, Output output, Vector2 object) {
    output.writeFloat(object.x);
    output.writeFloat(object.y);
  }

  @Override
  public Vector2 read(Kryo kryo, Input input, Class<Vector2> type) {
    Vector2 vector2 = new Vector2();
    vector2.x = input.readFloat();
    vector2.y = input.readFloat();
    return vector2;
  }
}
