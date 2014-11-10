package macbury.forge.level;

import macbury.forge.ForgE;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 19.10.14.
 * For loading level from disk
 */
public class LevelState {
  private static final String MAP_NAME_PREFIX = "MAP_";

  public LevelEnv env;
  public ChunkMap terrainMap;
  public int id;
  public String name;
  /**
   * Initialize blank new level. Mainly use for editor functionality
   * @return
   */
  public static LevelState blank(int width, int height, int depth) {
    LevelState state        = new LevelState();
    state.terrainMap        = new ChunkMap(ChunkMap.TERRAIN_TILE_SIZE);
    state.id                = ForgE.db.uid();
    state.name              = MAP_NAME_PREFIX + state.id;
    state.env               = new LevelEnv();
    state.terrainMap.initialize(width,height,depth);
    state.terrainMap.buildFloor();
    return state;
  }

}
