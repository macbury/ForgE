package macbury.forge.graphics.fbo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FloatFrameBuffer;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.CopyFrameBufferShader;
import macbury.forge.shaders.FrameBufferShader;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.utils.ScreenshotFactory;

/**
 * Created by macbury on 19.05.15.
 */
public class FrameBufferManager implements Disposable {
  private static final String TAG                   = "FrameBufferManager";
  private static final String COPY_FB_SHADER        = "ps-copy";
  private ObjectMap<String, FrameBuffer> frameBuffers;
  private Mesh screenQuad;
  private OrthographicCamera screenCamera;
  private FrameBuffer currentFrameBuffer;

  public FrameBufferManager() {
    frameBuffers = new ObjectMap<String, FrameBuffer>();
    createDefaultFrameBuffers();
  }

  public FrameBuffer create(String fbIdn) {
    return create(fbIdn, Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, Texture.TextureWrap.Repeat, Texture.TextureFilter.Linear);
  }

  public FrameBuffer get(String key) {
    return  frameBuffers.get(key);
  }

  public void renderFB(RenderContext renderContext, LevelEnv env, String outputFrameBufferId, String shaderName) {
    BaseShader baseShader = ForgE.shaders.get(shaderName);
    if (FrameBufferShader.class.isInstance(baseShader)) {
      FrameBufferShader shader = (FrameBufferShader)baseShader;
      renderContext.begin(); {
        begin(outputFrameBufferId); {
          ForgE.graphics.clearAll(Color.CLEAR);
          shader.begin(screenCamera, renderContext, env); {
            shader.render(screenQuad, GL30.GL_TRIANGLE_STRIP);
          } shader.end();
        } end();
      } renderContext.end();
    } else {
      throw new GdxRuntimeException(shaderName + " is not FrameBufferShader");
    }
  }

  public void copyFB(RenderContext renderContext, LevelEnv env, String inputFrameBufferId, String outputFrameBufferId) {
    CopyFrameBufferShader shader = (CopyFrameBufferShader)ForgE.shaders.get(COPY_FB_SHADER);
    renderContext.begin(); {
      Texture srcTexture = get(inputFrameBufferId).getColorBufferTexture();
      begin(outputFrameBufferId); {
        ForgE.graphics.clearAll(Color.CLEAR);
        shader.begin(screenCamera, renderContext, env); {
          shader.render(screenQuad, GL30.GL_TRIANGLE_STRIP, srcTexture);
        } shader.end();
      } end();
    } renderContext.end();
  }

  /**
   * Resizes internal camera for framebuffer use, call this in you ApplicationListener's resize.
   * @param width - new screen width
   * @param height - new screen height
   * @param resizeFramebuffers - whether all of the framebuffers should be recreated to match new screen size
   */
  public void resize(int width, int height, boolean resizeFramebuffers) {
    screenCamera = new OrthographicCamera(width, height);
    clear();

    createScreenQuad();
    createDefaultFrameBuffers();
  }

  /**
   * Creates a new Framebuffer with given params.
   * @param fbIdn - this framebuffer's identifier
   * @param format - pixel format of this framebuffer
   * @param fbWidth - desired width
   * @param fbHeight - desired height
   * @param hasDepth - whether to attach depth buffer
   */
  public FrameBuffer create(String fbIdn, Pixmap.Format format, int fbWidth, int fbHeight, boolean hasDepth, Texture.TextureWrap textureWrap, Texture.TextureFilter filter) {
    FrameBuffer fb = frameBuffers.get(fbIdn);

    if (fb == null || fb.getWidth() != fbWidth || fb.getHeight() != fbHeight) {
      if (fb != null) {
        fb.dispose();
      }
      Gdx.app.log(TAG, "Creating framebuffer: " + fbIdn);
      fb = new FrameBuffer(format, fbWidth, fbHeight, hasDepth);
      fb.getColorBufferTexture().setFilter(filter, filter);
      fb.getColorBufferTexture().setWrap(textureWrap, textureWrap);

    }
    frameBuffers.put(fbIdn, fb);
    return fb;
  }

  /**
   * Creates a new Framebuffer with given params.
   * @param fbIdn - this framebuffer's identifier
   * @param format - pixel format of this framebuffer
   * @param fbWidth - desired width
   * @param fbHeight - desired height
   * @param hasDepth - whether to attach depth buffer
   */
  public FrameBuffer createFloat(String fbIdn, int fbWidth, int fbHeight, boolean hasDepth, Texture.TextureWrap textureWrap, Texture.TextureFilter filter) {
    FrameBuffer fb = frameBuffers.get(fbIdn);

    if (fb == null || fb.getWidth() != fbWidth || fb.getHeight() != fbHeight) {
      if (fb != null) {
        fb.dispose();
      }
      Gdx.app.log(TAG, "Creating float framebuffer: " + fbIdn);
      fb = new FloatFrameBuffer(fbWidth, fbHeight, hasDepth);
      fb.getColorBufferTexture().setFilter(filter, filter);
      fb.getColorBufferTexture().setWrap(textureWrap, textureWrap);

    }
    frameBuffers.put(fbIdn, fb);
    return fb;
  }

  /**
   * Creates a quad which spans entire screen, used for rendering of framebuffers.
   */
  private void createScreenQuad() {
    if (screenQuad != null)
      return;
    screenQuad = new Mesh(true, 4, 6, VertexAttribute.Position(), new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, "a_color"), VertexAttribute.TexCoords(0));


    Vector3 vec0 = new Vector3(0, -1, 0);
    screenCamera.unproject(vec0);
    Vector3 vec1 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
    screenCamera.unproject(vec1);
    screenQuad.setVertices(new float[]{vec0.x, vec0.y, 0, 1, 1, 1, 1, 0, 1,
      vec1.x, vec0.y, 0, 1, 1, 1, 1, 1, 1,
      vec1.x, vec1.y, 0, 1, 1, 1, 1, 1, 0,
      vec0.x, vec1.y, 0, 1, 1, 1, 1, 0, 0});
    screenQuad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
  }


  @Override
  public void dispose() {
    clear();
  }

  private void clear() {
    for (String key : frameBuffers.keys()) {
      frameBuffers.get(key).dispose();
    }
    frameBuffers.clear();
    if (screenQuad != null)
      screenQuad.dispose();
    screenQuad = null;
  }

  public void end() {
    currentFrameBuffer.end();
    currentFrameBuffer = null;
  }

  public void begin(String fbIdn) {
    if (currentFrameBuffer != null)
      throw new GdxRuntimeException("Already binded other buffer!");
    currentFrameBuffer = get(fbIdn);
    if (currentFrameBuffer == null) {
      throw new GdxRuntimeException("Could not found: " + fbIdn);
    }
    currentFrameBuffer.begin();
  }

  public void createDefaultFrameBuffers() {
    create(Fbo.FRAMEBUFFER_FINAL);
    create(Fbo.FRAMEBUFFER_MAIN_COLOR);
    create(Fbo.FRAMEBUFFER_DOWN_SAMPLED_MAIN, Pixmap.Format.RGBA8888, ForgE.config.getInt(Config.Key.BloomTextureSize), ForgE.config.getInt(Config.Key.BloomTextureSize), false, Texture.TextureWrap.MirroredRepeat, Texture.TextureFilter.Linear);
    create(Fbo.FRAMEBUFFER_BLOOM, Pixmap.Format.RGBA8888, ForgE.config.getInt(Config.Key.BloomTextureSize), ForgE.config.getInt(Config.Key.BloomTextureSize), false, Texture.TextureWrap.MirroredRepeat, Texture.TextureFilter.Linear);
    create(Fbo.FRAMEBUFFER_REFLECTIONS, Pixmap.Format.RGBA8888, ForgE.config.getInt(Config.Key.ReflectionBufferSize), ForgE.config.getInt(Config.Key.ReflectionBufferSize), true, Texture.TextureWrap.Repeat, Texture.TextureFilter.Linear);
    create(Fbo.FRAMEBUFFER_REFRACTIONS, Pixmap.Format.RGBA8888, ForgE.config.getInt(Config.Key.RefractionBufferSize), ForgE.config.getInt(Config.Key.RefractionBufferSize), true, Texture.TextureWrap.Repeat, Texture.TextureFilter.Linear);
    createFloat(Fbo.FRAMEBUFFER_SUN_FAR_DEPTH, ForgE.config.getInt(Config.Key.FarShadowMapSize), ForgE.config.getInt(Config.Key.FarShadowMapSize), true, Texture.TextureWrap.ClampToEdge, Texture.TextureFilter.Nearest);
    createFloat(Fbo.FRAMEBUFFER_SUN_NEAR_DEPTH, ForgE.config.getInt(Config.Key.NearShadowMapSize), ForgE.config.getInt(Config.Key.NearShadowMapSize), true, Texture.TextureWrap.ClampToEdge, Texture.TextureFilter.Nearest);
  }

  public ObjectMap<String, FrameBuffer> all() {
    return frameBuffers;
  }

  public FileHandle saveAsPng(String frameBufferName) {
    ForgE.fb.begin(frameBufferName);
    FrameBuffer frameBuffer = get(frameBufferName);
    if (frameBuffer == null) {
      throw new GdxRuntimeException("Cannot save: " + frameBufferName);
    }
    FileHandle saveFile = Gdx.files.absolute("/tmp/fb_out.png");
    ScreenshotFactory.saveScreenshot(saveFile, frameBuffer.getWidth(), frameBuffer.getHeight());
    ForgE.fb.end();
    return saveFile;
  }

  public void destroy(String frameBufferName) {
    if (frameBuffers.containsKey(frameBufferName)) {
      get(frameBufferName).dispose();
      frameBuffers.remove(frameBufferName);
    }

  }
}
