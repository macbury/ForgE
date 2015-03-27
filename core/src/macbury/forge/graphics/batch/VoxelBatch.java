package macbury.forge.graphics.batch;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.BaseRenderableProvider;
import macbury.forge.graphics.batch.renderable.SpriteRenderable;
import macbury.forge.graphics.batch.sprites.Sprite3D;
import macbury.forge.graphics.batch.sprites.Sprite3DCache;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.ShaderProvider;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.10.14.
 */
public class VoxelBatch implements Disposable {
  public final ShapeRenderer shapeRenderer;
  private final SpriteRenderablePool spriteRenderablePool;
  private final CameraRenderableSorter sorter;
  private final ShaderProvider shaderProvider;
  private final Array<Sprite3DCache> spriteCache;
  public long trianglesPerFrame;
  protected Camera camera;
  protected final RenderContext context;
  protected final Array<BaseRenderable> renderables = new Array<BaseRenderable>();
  private boolean sorted;
  public int renderablesPerFrame;

  public VoxelBatch(RenderContext customRenderContext) {
    this.context              = customRenderContext;
    this.shaderProvider       = new ShaderProvider();
    this.shapeRenderer        = new ShapeRenderer();
    this.sorter               = new CameraRenderableSorter();
    this.spriteRenderablePool = new SpriteRenderablePool();
    this.spriteCache          = new Array<Sprite3DCache>();
    renderablesPerFrame       = 0;
    trianglesPerFrame         = 0;
  }

  public Sprite3D build(TextureAsset asset, boolean isStatic, boolean transparent) {
    Sprite3D sprite3D = new Sprite3D(this);
    sprite3D.init(transparent, isStatic);
    sprite3D.setTextureAsset(asset);
    return sprite3D;
  }

  public void begin(Camera cam) {
    if (camera != null) throw new GdxRuntimeException("Call end() first.");
    camera = cam;
    sorted = false;
    trianglesPerFrame = 0;
  }

  /**
   * Add Sprite3D to queue
   * @param sprite3D
   */
  public void add(Sprite3D sprite3D) {
    if (sprite3D.getMesh() != null) {
      SpriteRenderable renderable = spriteRenderablePool.obtain();
      renderable.build(sprite3D);
      add(renderable);
    }

  }

  /**
   * Add BaseRenderable to queue
   * @param renderable
   */
  public void add(final BaseRenderable renderable) {
    if (camera == null) throw new GdxRuntimeException("Call begin() first.");
    renderables.add(renderable);
    renderable.mesh.setAutoBind(false);
  }

  /**
   * Add BaseRenderable to queue
   * @param renderableProvider
   */
  public void add(final BaseRenderableProvider renderableProvider) {
    if (camera == null) throw new GdxRuntimeException("Call begin() first.");
    final int offset = renderables.size;
    renderableProvider.getRenderables(renderables);
    for (int i = offset; i < renderables.size; i++) {
      BaseRenderable renderable = renderables.get(i);
      renderable.mesh.setAutoBind(false);
    }
  }

  /**
   * Render normally all renderables
   */
  public void render(LevelEnv env) {
    if (camera == null) throw new GdxRuntimeException("Call begin() first.");
    sortUnlessNotSorted();
    if (ForgE.config.renderDebug == Config.RenderDebug.Wireframe) {
      renderWireframe();
    } else {
      renderTextured(env);
    }
  }

  /**
   * Render SSAO
   */
  public void renderSSAO() {
    sortUnlessNotSorted();
  }

  private void sortUnlessNotSorted() {
    if (!sorted) {
      sorter.sort(camera, renderables);
      sorted = true;
    }
  }

  private void renderWireframe() {
    context.begin(); {
      shapeRenderer.setProjectionMatrix(camera.combined);
      shapeRenderer.begin(ShapeRenderer.ShapeType.Line); {
        for (int i = 0; i < renderables.size; i++) {
          final BaseRenderable renderable = renderables.get(i);
          if (renderable.wireframe != null) {
            shapeRenderer.setTransformMatrix(renderable.worldTransform);
            renderable.wireframe.render(shapeRenderer, Color.WHITE);
            trianglesPerFrame += renderable.triangleCount;
          }
        }
      } shapeRenderer.end();
    } context.end();
  }

  private void renderTextured(LevelEnv env) {
    RenderableBaseShader currentShader = null;
    RenderableBaseShader currentRenderableShader = null;
    for (int i = 0; i < renderables.size; i++) {
      final BaseRenderable renderable = renderables.get(i);
      currentRenderableShader         = shaderProvider.provide(renderable);

      if (currentShader != currentRenderableShader) {
        if (currentShader != null) currentShader.end();
        currentShader = currentRenderableShader;
        currentShader.begin(camera, context, env);
      }
      currentShader.render(renderable);
      trianglesPerFrame += renderable.triangleCount;
      if (SpriteRenderable.class.isInstance(renderable)) {
        spriteRenderablePool.free((SpriteRenderable)renderable);
      }
    }

    if (currentShader != null) currentShader.end();
  }

  public void end() {
    camera = null;
    sorted = false;
    renderablesPerFrame = renderables.size;

    renderables.clear();
  }

  @Override
  public void dispose() {
    for (Sprite3DCache cache : spriteCache) {
      cache.dispose();
    }
    shapeRenderer.dispose();
    renderables.clear();
    spriteRenderablePool.flush();
    spriteCache.clear();
    camera = null;
  }


  public Sprite3DCache findSpriteCacheForRegion(TextureRegion textureRegion) {
    Sprite3DCache found = null;
    for(Sprite3DCache cache : spriteCache) {
      if (cache.is(textureRegion)) {
        found = cache;
        break;
      }
    }
    if (found == null) {
      found = new Sprite3DCache(textureRegion);
      spriteCache.add(found);
    }
    return found;
  }
}
