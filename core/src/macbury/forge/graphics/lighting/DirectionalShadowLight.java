package macbury.forge.graphics.lighting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;

/**
 * Created by macbury on 22.05.15.
 */
public class DirectionalShadowLight extends DirectionalLight implements Disposable, ShadowMap, VoxelBatch.CameraProvider {
  private final int shadowMapWidth;
  private final int shadowMapHeight;
  private final float shadowViewportWidth;
  private final float shadowViewportHeight;
  private final float shadowNear;
  private final float shadowFar;
  protected FrameBuffer fbo;
  protected Camera cam;
  protected float halfDepth;
  protected float halfHeight;
  protected final Vector3 tmpV = new Vector3();
  protected TextureDescriptor textureDesc;

  public DirectionalShadowLight(int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight, float shadowNear, float shadowFar) {
    this.shadowMapWidth   = shadowMapWidth;
    this.shadowMapHeight   = shadowMapHeight;
    this.shadowViewportWidth   = shadowViewportWidth;
    this.shadowViewportHeight   = shadowViewportHeight;
    this.shadowNear   = shadowNear;
    this.shadowFar   = shadowFar;

    cam                   = new PerspectiveCamera(120f, shadowMapWidth, shadowMapHeight);
    cam.near              = shadowNear;
    cam.far               = shadowFar;

  }

  public DirectionalShadowLight() {
    this(1024, 1024,30f, 30f, 0.1f, 100f);
  }

  public void update (final Camera camera) {
    update(tmpV.set(camera.direction).scl(halfHeight), camera.direction);
  }

  public void update (final Vector3 center, final Vector3 forward) {
    //cam.position.set(direction).scl(-halfDepth).add(center);
    //cam.direction.set(direction).nor();
    //cam.up.set(forward).nor();
    //cam.normalizeUp();
    cam.position.set(-1,50,-1);
    cam.lookAt(3,0,3);
    cam.update();
  }

  public void begin (final Camera camera) {
    update(camera);
    begin();
  }

  public void begin (final Vector3 center, final Vector3 forward) {
    update(center, forward);
    begin();
  }

  public void begin () {
    initFb();
    fbo.begin();
    ForgE.graphics.clearAll(Color.BLACK);
    Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
    Gdx.gl.glScissor(1, 1, fbo.getWidth() - 2, fbo.getHeight() - 2);
  }

  private void initFb() {
    if (fbo == null) {
      fbo                   = new FrameBuffer(Pixmap.Format.RGBA8888, shadowMapWidth, shadowMapHeight, true);

      halfHeight            = shadowViewportHeight * 0.5f;
      halfDepth             = shadowNear + 0.5f * (shadowFar - shadowNear);
      textureDesc           = new TextureDescriptor();
      textureDesc.minFilter = textureDesc.magFilter = Texture.TextureFilter.Nearest;
      textureDesc.uWrap     = textureDesc.vWrap = Texture.TextureWrap.ClampToEdge;
    }
  }

  public void end () {
    Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    fbo.end();
  }

  public FrameBuffer getFrameBuffer () {
    return fbo;
  }

  public Camera getCamera () {
    return cam;
  }

  @Override
  public void dispose () {
    if (fbo != null) fbo.dispose();
    fbo = null;
  }

  @Override
  public Matrix4 getLightTransMatrix() {
    return cam.combined;
  }

  @Override
  public TextureDescriptor getDepthTexture() {
    textureDesc.texture = fbo.getColorBufferTexture();
    return textureDesc;
  }

  @Override
  public Camera getBatchCamera() {
    return cam;
  }
}

