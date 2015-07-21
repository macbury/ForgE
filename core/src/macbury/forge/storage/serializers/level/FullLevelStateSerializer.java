package macbury.forge.storage.serializers.level;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.level.LevelState;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 09.03.15.
 */
public class FullLevelStateSerializer extends LevelStateBasicInfoSerializer {
  @Override
  public void write(Kryo kryo, Output output, LevelState object) {
    super.write(kryo, output, object);
    kryo.writeObject(output, object.env);
    kryo.writeObject(output, object.terrainMap);
  }

  @Override
  public LevelState read(Kryo kryo, Input input, Class<LevelState> type) {
    LevelState state = super.read(kryo, input, type);
    state.env = kryo.readObject(input, LevelEnv.class);
    state.setTerrainMap(kryo.readObject(input, ChunkMap.class));
    //state.env.windDisplacementTextureAsset = ForgE.assets.getTexture("textures/wind_bump.jpg");
    return state;
  }
}
