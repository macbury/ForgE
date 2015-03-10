package macbury.forge.storage.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.ForgE;
import macbury.forge.level.LevelState;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 09.03.15.
 */
public class FullLevelStateSerializer extends LevelStateBasicInfoSerializer {
  @Override
  public void write(Kryo kryo, Output output, LevelState object) {
    super.write(kryo, output, object);
    kryo.writeObject(output, object.terrainMap);
  }

  @Override
  public LevelState read(Kryo kryo, Input input, Class<LevelState> type) {
    LevelState state = super.read(kryo, input, type);
    state.setTerrainMap(kryo.readObject(input, ChunkMap.class));
    state.env.windDisplacementTexture = ForgE.assets.getTexture("textures/wind_bump.jpg");
    return state;
  }
}
