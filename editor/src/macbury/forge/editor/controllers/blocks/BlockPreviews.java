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
import macbury.forge.ForgE;
import macbury.forge.blocks.Block;
import macbury.forge.editor.utils.ScreenshotFactory;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.builders.Chunk;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.TerrainShader;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.voxel.ChunkMap;
import macbury.forge.voxel.VoxelMap;

/**
 * Created by macbury on 13.03.15.
 */
public class BlockPreviews implements Runnable {
  private final static int PREVIEW_SIZE = 48;
  private static final String TAG = "GenerateBlockPreviews";
  private final Listener listener;
  private final boolean removeOld;
  private RenderContext renderContext;
  private PerspectiveCamera camera;
  private ChunkMap voxelMap;
  private TerrainBuilder builder;
  private Chunk mainChunk;
  private TerrainShader shader;
  private LevelEnv levelEnv;

  public BlockPreviews(Listener listener, boolean removeOld) {
    this.listener   = listener;
    this.removeOld  = removeOld;
  }

  @Override
  public void run() {
    ForgE.blocks.loadAtlasAndUvsIfNull();
    this.levelEnv            = new LevelEnv();
    this.shader              = (TerrainShader)ForgE.shaders.get("block-preview");
    this.voxelMap            = ChunkMap.build();
    this.builder             = new TerrainBuilder(voxelMap);
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.camera              = new PerspectiveCamera(40, PREVIEW_SIZE/2, PREVIEW_SIZE/2);
    FrameBuffer buffer       = new FrameBuffer(Pixmap.Format.RGBA8888, PREVIEW_SIZE, PREVIEW_SIZE, true);
    camera.near = 0.01f;
    camera.far  = 5f;
    camera.position.set(0f, 2.5f, 0f);
    camera.lookAt(new Vector3(0.2f,2.35f,0.2f));
    camera.update();

    voxelMap.initialize(ChunkMap.CHUNK_SIZE,ChunkMap.CHUNK_SIZE,ChunkMap.CHUNK_SIZE);
    voxelMap.splitIntoChunks();

    mainChunk = voxelMap.chunks.get(0);

    buffer.begin(); {
      Block[] blocks = ForgE.blocks.list();
      for (int i = 1; i < blocks.length; i++) {
        saveBlockScreenshot(blocks[i]);
      }
    } buffer.end();

    listener.onGeneratePreviewsCompleted();
    buffer.dispose();
    mainChunk.dispose();
    voxelMap.dispose();
    builder.dispose();
    shader.dispose();
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

    ForgE.graphics.clearAll(Color.CLEAR);
    voxelMap.setBlockForPosition(block, 1, 1, 1);
    mainChunk.clearFaces();
    builder.begin(); {
      builder.cursor.set(mainChunk);
      try {
        while(builder.next()) {
          builder.buildFaceForChunk(mainChunk);
        }
      } catch (Block.NoUvForBlockSide e) {
        Gdx.app.error(TAG, e.toString());
      }
    } builder.end();

    mainChunk.updateBoundingBox();
    shader.begin(camera, renderContext, levelEnv); {
      for (int i = 0; i < mainChunk.renderables.size; i++) {
        VoxelFaceRenderable renderable = mainChunk.renderables.get(i);
        shader.render(renderable);
      }
    } shader.end();

    ScreenshotFactory.saveScreenshot(new FileHandle(preview.file().getAbsoluteFile()), PREVIEW_SIZE, PREVIEW_SIZE);
  }

  public interface Listener {
    public void onGeneratePreviewsCompleted();
  }
}
