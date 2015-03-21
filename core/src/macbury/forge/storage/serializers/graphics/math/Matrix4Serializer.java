package macbury.forge.storage.serializers.graphics.math;

import com.badlogic.gdx.math.Matrix4;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by macbury on 21.03.15.
 */
public class Matrix4Serializer extends Serializer<Matrix4> {
  @Override
  public void write(Kryo kryo, Output output, Matrix4 mat) {
    output.writeInt(mat.val.length);
    output.writeFloats(mat.val);
  }

  @Override
  public Matrix4 read(Kryo kryo, Input input, Class<Matrix4> type) {
    int len      = input.readInt();
    Matrix4 mat4 = new Matrix4(input.readFloats(len));
    return mat4;
  }
}
