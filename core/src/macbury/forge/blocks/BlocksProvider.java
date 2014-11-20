package macbury.forge.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by macbury on 11.11.14.
 */
public class BlocksProvider implements Disposable {
  public final static String BLOCKS_PATH = "blocks/";
  public final static String BLOCKS_SHAPE_PATH = BLOCKS_PATH + "shapes/";
  public final static String BLOCK_EXT = "block";
  public final static String SHAPE_EXT = "shape";
  private static final String TAG = "BlocksProvider";
  private static final String TILEMAP_PATH = "textures/tilemap.atlas";
  private Block[] blocks;
  private TextureAtlas textureAtlas;
  private boolean reloadAtlas;
  private HashMap<String, BlockShape> shapes;

  public BlocksProvider() {
    reload();
  }

  public void reload() {
    Gdx.app.log(TAG, "Reloading blocks...");

    loadShapes();
    loadBlocks();

    reloadAtlas = true;
  }

  private void loadShapes() {
    Json json = new Json();
    FileHandle[] shapesFiles = Gdx.files.internal(BLOCKS_SHAPE_PATH).list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith("."+SHAPE_EXT);
      }
    });

    this.shapes = new HashMap<String, BlockShape>();

    for (FileHandle blockShapeFile : shapesFiles) {
      BlockShape blockShape = json.fromJson(BlockShape.class, blockShapeFile.readString());
      blockShape.name       = blockShapeFile.nameWithoutExtension();
      this.shapes.put(blockShape.name, blockShape);
      Gdx.app.log(TAG, "Loaded block shape: " + blockShape.name);
    }
  }

  private void loadBlocks() {
    Json json = new Json();
    FileHandle[] blocksFiles = Gdx.files.internal(BLOCKS_PATH).list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith("."+BLOCK_EXT);
      }
    });

    this.blocks     = new Block[blocksFiles.length + 1];
    this.blocks[0]  = new AirBlock();

    for (FileHandle blockFile : blocksFiles) {
      try {
        Block block            = json.fromJson(Block.class, blockFile.readString());
        String[] nameParts     = blockFile.nameWithoutExtension().split("_");
        block.name             = String.join(" ", Arrays.asList(Arrays.copyOfRange(nameParts, 1, nameParts.length)));
        if (shapes.containsKey(block.shape)) {
          block.blockShape       = shapes.get(block.shape);
        } else {
          throw new GdxRuntimeException("Could not find shape with name: "+block.shape + " for " + block.name);
        }

        block.id               = Byte.valueOf(nameParts[0]);
        if (this.blocks[block.id] != null) {
          throw new GdxRuntimeException("Block with id "+block.id+" already added!");
        }
        this.blocks[block.id] = block;
        Gdx.app.log(TAG, "Loaded block: " +block.toString());

      } catch (SerializationException exception) {
        throw new GdxRuntimeException("Could not load block: "+blockFile.name() + " because " + exception.getMessage());
      }


    }
  }

  public void loadTilemap() {
    if (textureAtlas != null) {
      textureAtlas.dispose();
      textureAtlas = null;
    }

    FileHandle textureAtlasFile = getTextureAtlasFile();

    if (textureAtlasFile.exists()) {
      this.textureAtlas = new TextureAtlas(textureAtlasFile);

      for (int i = 1; i < blocks.length; i++) {
        blocks[i].createUVMapping(textureAtlas);
      }
    }

    reloadAtlas = false;
  }

  public FileHandle getTextureAtlasFile() {
    return Gdx.files.internal(TILEMAP_PATH);
  }

  public Block find(byte blockId) {
    return blocks[blockId];
  }

  public Block find(int blockId) {
    return blocks[blockId];
  }

  @Override
  public void dispose() {
    textureAtlas.dispose();
    blocks = null;
  }

  public void loadAtlasAndUvsIfNull() {
    if (textureAtlas == null || reloadAtlas) {
      loadTilemap();
    }
  }

  public Block[] list() {
    return blocks;
  }

  public GLTexture getTerrainTexture() {
    loadAtlasAndUvsIfNull();
    return textureAtlas.getTextures().first();
  }
}
