package macbury.forge.editor.controllers.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.editor.utils.ScreenshotFactory;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.level.LevelEnv;
import macbury.forge.screens.AbstractScreen;
import macbury.forge.shaders.TerrainShader;
import macbury.forge.voxel.ChunkMap;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by macbury on 13.03.15.
 */
public class BlockPreviews extends AbstractScreen {
  private final static int PREVIEW_SIZE = 32;
  private static final String TAG = "GenerateBlockPreviews";
  private final Listener listener;
  private final boolean removeOld;
  private final AbstractScreen oldScreen;
  private RenderContext renderContext;
  private PerspectiveCamera camera;
  private ChunkMap voxelMap;
  private TerrainBuilder builder;
  private Chunk mainChunk;
  private TerrainShader shader;
  private LevelEnv levelEnv;
  private FrameBuffer buffer;
  private Block[] blocks;
  private int i;

  public BlockPreviews(Listener listener, boolean removeOld) {
    this.listener   = listener;
    this.removeOld  = removeOld;
    this.oldScreen  = ForgE.screens.current();
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.screens.set(BlockPreviews.this);
      }
    });
  }

  private void clearOld() {
    FileHandle[] filesToRemove = Gdx.files.internal("ed/previews/").list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });

    for (int i = 0; i < filesToRemove.length; i++) {
      FileHandle handle = filesToRemove[i];
      Gdx.app.log(TAG, "Removing: "+ handle.path().toString());
      handle.file().delete();
    }
  }


  public static FileHandle blockPreview(Block block) {
    return  Gdx.files.internal("ed/previews/block_"+block.id+".png");
  }

  private void saveBlockScreenshot(Block block) {
    FileHandle preview = blockPreview(block);

    if (preview.exists() && !removeOld) {
      Gdx.app.log(TAG, "Skipping: " + block.name);
      return;
    } else {
      Gdx.app.log(TAG, "Generating block name: " + block.name);
    }

    this.voxelMap            = ChunkMap.build();
    this.builder             = new TerrainBuilder(voxelMap);

    voxelMap.initialize(ChunkMap.CHUNK_SIZE,ChunkMap.CHUNK_SIZE,ChunkMap.CHUNK_SIZE);
    voxelMap.splitIntoChunks();

    mainChunk = voxelMap.chunks.get(0);
    voxelMap.setBlockForPosition(block, 1, 1, 1);
    voxelMap.rebuildAll();

    builder.begin(); {
      builder.cursor.set(mainChunk);
      while(builder.next()) {
        builder.buildFaceForChunk(mainChunk);
      }

      builder.assembleMesh(mainChunk);
    } builder.end();

    mainChunk.updateBoundingBox();

    if (mainChunk.renderables.size == 0) {
      throw new GdxRuntimeException("No renderables for chunk!");
    }

    ForgE.graphics.clearAll(Color.CLEAR);
    shader.begin(camera, renderContext, levelEnv); {
      for (int i = 0; i < mainChunk.renderables.size; i++) {
        VoxelFaceRenderable renderable = mainChunk.renderables.get(i);
        shader.render(renderable);
      }
    } shader.end();

    Gdx.app.log(TAG, "Saving: " + preview.file().getAbsoluteFile());
    ScreenshotFactory.saveScreenshot(new FileHandle(preview.file().getAbsoluteFile()), PREVIEW_SIZE, PREVIEW_SIZE);
    mainChunk.dispose();
    voxelMap.dispose();
    builder.dispose();
  }

  @Override
  protected void initialize() {
    if (removeOld) {
      clearOld();
    }

    ForgE.blocks.loadAtlasAndUvsIfNull();
    this.levelEnv            = new LevelEnv();
    this.shader              = (TerrainShader)ForgE.shaders.get("block-preview");

    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.camera              = new PerspectiveCamera(40, PREVIEW_SIZE/2, PREVIEW_SIZE/2);
    //this.buffer              = new FrameBuffer(Pixmap.Format.RGBA8888, PREVIEW_SIZE, PREVIEW_SIZE, true);
    camera.near = 0.01f;
    camera.far  = 5f;
    camera.position.set(0f, 2.5f, 0f);
    camera.lookAt(new Vector3(0.2f,2.35f,0.2f));
    camera.update();

    this.blocks = ForgE.blocks.list();
    this.i      = 1;
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glViewport(0,0, PREVIEW_SIZE, PREVIEW_SIZE);
    if (i < blocks.length) {
      saveBlockScreenshot(blocks[i]);
      i++;
    } else {
      listener.onGeneratePreviewsCompleted();

      ForgE.screens.reset();
      if (oldScreen != null) {
        ForgE.screens.set(oldScreen);
      }
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    //buffer.dispose();
    shader.dispose();

  }

  public interface Listener {
    public void onGeneratePreviewsCompleted();
  }
}
