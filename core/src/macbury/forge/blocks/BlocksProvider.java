package macbury.forge.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by macbury on 11.11.14.
 */
public class BlocksProvider implements Disposable {
  public final static String BLOCKS_PATH = "blocks/";
  public final static String BLOCK_EXT = "block";
  private static final String TAG = "BlocksProvider";
  private static final String TILEMAP_PATH = "textures/tilemap.atlas";
  private Block[] blocks;
  private TextureAtlas textureAtlas;

  public BlocksProvider() {
    reload();
  }

  public void reload() {
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
      Block block           = json.fromJson(Block.class, blockFile.readString());
      block.name            = blockFile.nameWithoutExtension().split("_")[1];
      block.id              = Byte.valueOf(blockFile.nameWithoutExtension().split("_")[0]);
      if (this.blocks[block.id] != null) {
        throw new GdxRuntimeException("Block with id "+block.id+" already added!");
      }
      this.blocks[block.id] = block;
      Gdx.app.log(TAG, "Loaded block: " +block.toString());
    }

    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        loadTilemap();
      }
    });
  }

  public void loadTilemap() {
    if (textureAtlas != null) {
      textureAtlas.dispose();
    }

    FileHandle textureAtlasFile = getTextureAtlasFile();

    if (textureAtlasFile.exists()) {
      this.textureAtlas = new TextureAtlas(textureAtlasFile);

      for (int i = 1; i < blocks.length; i++) {
        blocks[i].createUVMapping(textureAtlas);
      }
    }
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

  public Block[] list() {
    return blocks;
  }

  public GLTexture getTerrainTexture() {
    return textureAtlas.getTextures().first();
  }
}
