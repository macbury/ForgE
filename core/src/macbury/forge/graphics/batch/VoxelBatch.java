package macbury.forge.graphics.batch;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.BaseRenderableProvider;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.10.14.
 */
public class VoxelBatch implements Disposable {
  public final ShapeRenderer shapeRenderer;
  private final CameraRenderableSorter sorter;
  protected Camera camera;
  protected final RenderContext context;
  protected final Array<BaseRenderable> renderables = new Array<BaseRenderable>();
  private boolean sorted;
  public int renderablesPerFrame;

  public enum RenderType {
    Normal, Wireframe
  }

  protected RenderType type;

  public VoxelBatch(RenderContext customRenderContext) {
    this.context        = customRenderContext;
    this.shapeRenderer  = new ShapeRenderer();
    this.type           = RenderType.Normal;
    this.sorter         = new CameraRenderableSorter();
    renderablesPerFrame = 0;
  }

  public RenderType getType() {
    return type;
  }

  public void setType(RenderType type) {
    this.type = type;
  }

  public void begin(Camera cam) {
    if (camera != null) throw new GdxRuntimeException("Call end() first.");
    camera = cam;
    sorted = false;
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
  public void render() {
    if (camera == null) throw new GdxRuntimeException("Call begin() first.");
    sortUnlessNotSorted();
    if (type == RenderType.Normal) {
      renderTextured();
    } else {
      renderWireframe();
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
            renderable.wireframe.render(shapeRenderer, Color.WHITE);
          }
        }
      } shapeRenderer.end();
    } context.end();
  }

  private void renderTextured() {
    RenderableBaseShader currentShader = null;
    for (int i = 0; i < renderables.size; i++) {
      final BaseRenderable renderable = renderables.get(i);
      if (currentShader != renderable.shader) {
        if (currentShader != null) currentShader.end();
        currentShader = renderable.shader;
        currentShader.begin(camera, context);
      }
      currentShader.render(renderable);
    }
    renderablesPerFrame = renderables.size;
    if (currentShader != null) currentShader.end();
  }

  public void end() {
    camera = null;
    sorted = false;
    renderables.clear();
  }

  @Override
  public void dispose() {
    shapeRenderer.dispose();
    renderables.clear();
    camera = null;
  }
}
