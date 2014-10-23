package macbury.forge.level;

import macbury.forge.level.map.ChunkMap;

/**
 * Created by macbury on 19.10.14.
 * For loading level from disk
 */
public class LevelState {
  private static final String MAP_NAME_PREFIX = "MAP_";
  private static int uid = 0;

  public ChunkMap terrainMap;
  public int id;
  public String name;
  /**
   * Initialize blank new level. Mainly use for editor functionality
   * @return
   */
  public static LevelState blank() {
    LevelState state        = new LevelState();
    state.terrainMap        = new ChunkMap();
    state.id                = uid();
    state.name              = MAP_NAME_PREFIX + state.id;

    state.terrainMap.initialize(320,100,240);
    state.terrainMap.buildFloor();
    return state;
  }

  /**
   * Create uid for level.
   * @return
   */
  public static int uid() {
    //TODO: After increase save uid to disk
    return uid += 1;
  }
}
