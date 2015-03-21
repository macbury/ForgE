package macbury.forge.storage.serializers.graphics.math;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by macbury on 21.03.15.
 */
public class BoundingBoxSerializer extends Serializer<BoundingBox> {
  public Vector3 tempA = new Vector3();
  public Vector3 tempB = new Vector3();
  @Override
  public void write(Kryo kryo, Output output, BoundingBox object) {
    kryo.writeObject(output, object.getMin(tempA));
    kryo.writeObject(output, object.getMax(tempB));
  }

  @Override
  public BoundingBox read(Kryo kryo, Input input, Class<BoundingBox> type) {
    Vector3 min     = kryo.readObject(input, Vector3.class);
    Vector3 max     = kryo.readObject(input, Vector3.class);
    return new BoundingBox(min, max);
  }
}
