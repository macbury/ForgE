package macbury.forge.storage.serializers.assets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.ForgE;
import macbury.forge.assets.assets.Asset;

/**
 * Created by macbury on 16.03.15.
 */
public class AssetSerializer extends Serializer<Asset> {
  @Override
  public void write(Kryo kryo, Output output, Asset object) {
    kryo.writeClass(output, object.getClass());
    output.writeString(object.getPath());
  }

  @Override
  public Asset read(Kryo kryo, Input input, Class<Asset> type) {
    Class<? extends Asset> assetClass = kryo.readClass(input).getType();
    String path                       = input.readString();
    return ForgE.assets.getAsset(assetClass, path);
  }
}
