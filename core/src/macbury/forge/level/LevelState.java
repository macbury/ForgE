package macbury.forge.level;

import macbury.forge.ForgE;
import macbury.forge.db.GameDatabase;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.voxel.ChunkMap;

/**
 * Created by macbury on 19.10.14.
 * For loading level from disk
 */
public class LevelState {
  public static final String MAP_NAME_PREFIX = "MAP_";
  public static final String FILE_EXT = ".level";
  public static final String MAP_STORAGE_DIR = "db/maps/";
  private int width;
  private int depth;
  private int height;

  public LevelEnv env;
  public ChunkMap terrainMap;
  public int id;
  public String name;

  public LevelState() {
    width   = ChunkMap.CHUNK_SIZE * 5;
    depth   = ChunkMap.CHUNK_SIZE * 5;
    height  = ChunkMap.CHUNK_SIZE * 2;
    env     = new LevelEnv();
  }

  public LevelState(GameDatabase db) {
    this();
    id      = db.uid();
    name    = MAP_NAME_PREFIX + id;
  }

  /**
   * Initialize arrays and textures
   */
  public void bootstrap() {
    terrainMap              = ChunkMap.build();
    terrainMap.initialize(width, height, depth);
    terrainMap.splitIntoChunks();
    terrainMap.buildFloor();
    terrainMap.rebuildAll();
    env.terrainMap              = terrainMap;
    env.setWindDisplacementTextureAsset(ForgE.assets.getTexture("graphics/textures/wind_bump.jpg"));
    env.water.setWaterDisplacementTextureAsset(ForgE.assets.getTexture("graphics/textures/waterDUDV.png"));
    env.skybox.setSkyboxAsset(ForgE.assets.getCubemap("graphics/textures/skybox/day.png"));
  }

  public void setTerrainMap(ChunkMap chunkMap) {
    env.terrainMap = chunkMap;
    terrainMap     = chunkMap;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getDepth() {
    return depth;
  }

  public int getHeight() {
    return height;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
