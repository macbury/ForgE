package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by macbury on 16.03.15.
 */
public class ColorSerializer extends Serializer<Color> {
  @Override
  public void write(Kryo kryo, Output output, Color object) {
    output.writeFloat(object.r);
    output.writeFloat(object.g);
    output.writeFloat(object.b);
    output.writeFloat(object.a);
  }

  @Override
  public Color read(Kryo kryo, Input input, Class<Color> type) {
    Color color = new Color();
    color.r = input.readFloat();
    color.g = input.readFloat();
    color.b = input.readFloat();
    color.a = input.readFloat();
    return color;
  }
}
