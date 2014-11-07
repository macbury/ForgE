package macbury.forge.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import macbury.forge.voxel.VoxelMaterial;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 19.10.14.
 * For loading level from disk
 */
public class LevelState {
  private static final String MAP_NAME_PREFIX = "MAP_";
  private static int uid = 0;

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
    state.id                = uid();
    state.name              = MAP_NAME_PREFIX + state.id;
    state.env               = new LevelEnv();
    state.terrainMap.initialize(width,height,depth);
    state.terrainMap.buildFloor();
    return state;
  }

  public static LevelState heightMapTest() {
    LevelState state        = new LevelState();
    state.terrainMap        = new ChunkMap(ChunkMap.TERRAIN_TILE_SIZE);
    state.id                = uid();
    state.name              = MAP_NAME_PREFIX + state.id;

    state.terrainMap.initialize(250,50,250);
    state.terrainMap.buildFloor();

    Pixmap pixmap = new Pixmap(Gdx.files.internal("heightmap.png"));
    Color color   = new Color();

    for (int x = 0; x < state.terrainMap.getWidth(); x++) {
      for (int z = 0; z < state.terrainMap.getDepth(); z++) {
        int rawColor = pixmap.getPixel(x, z);
        color.set(rawColor);
        int height        = (int)Math.floor(color.r * (state.terrainMap.getHeight() - 5));

        for (int y = 1; y < height; y++) {
          VoxelMaterial mat = state.terrainMap.materials.get(Math.max((int)Math.round((state.terrainMap.materials.size-1) * Math.random()), 1));
          state.terrainMap.setMaterialForPosition(mat, x,y,z);
        }
      }
    }

    pixmap.dispose();

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
