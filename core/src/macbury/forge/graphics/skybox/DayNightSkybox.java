package macbury.forge.graphics.skybox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.DayNightSkyRenderable;
import macbury.forge.graphics.batch.renderable.SunMonRenderable;
import macbury.forge.graphics.camera.GameCamera;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.utils.CameraUtils;
import macbury.forge.utils.QuadBuilder;

/**
 * Created by macbury on 04.08.15.
 */
public class DayNightSkybox extends Skybox {
  private final static float SATELITE_MESH_SIZE = 0.5f;
  private final static int DISTANCE_TO_SUN = 40;
  private final static int SUN_SIZE = 10;
  private static final String TAG = "DayNightSkybox";
  private TextureAsset sunAsset;
  private TextureAsset moonAsset;
  private Texture sunTexture;
  private Texture moonTexture;
  private DayNightSkyRenderable dayNightRenderable;
  private SunMonRenderable sunMonRenderable;
  private float rotation = 0.0f;
  private Matrix4 tempMat       = new Matrix4();
  private Vector3 tempPosition  = new Vector3();
  private Vector3 tempCamPosition  = new Vector3();
  private Quaternion sateliteRotation = new Quaternion();

  @Override
  public void update(float delta) {
    //rotation -= delta * 0.3f;
    rotation = -90f;
    Gdx.app.log(TAG, "Rotation: " + rotation);
  }

  @Override
  public void render(VoxelBatch batch, LevelEnv env, GameCamera camera) {
    super.render(batch, env, camera);

    renderSatelites(camera, env, batch);
  }
  private void renderSatelites(GameCamera camera, LevelEnv env, VoxelBatch batch) {
    buildSunMoon();
    tempCamPosition.set(camera.position);
    tempMat.idt();
    tempMat.translate(tempCamPosition);
    tempMat.rotate(Vector3.X, rotation);
    tempMat.translate(0, 0, DISTANCE_TO_SUN);
    tempMat.getTranslation(tempPosition);

    CameraUtils.lookAt(tempCamPosition, tempPosition, camera.up, sateliteRotation);

    sunMonRenderable.worldTransform.setToTranslation(tempPosition);
    sunMonRenderable.worldTransform.scl(SUN_SIZE);
    sunMonRenderable.worldTransform.rotate(sateliteRotation);

    env.mainLight.direction.set(tempCamPosition).sub(tempPosition).nor();
    batch.add(sunMonRenderable);
    batch.render(env);
  }

  @Override
  public void dispose() {
    setSunAsset(null);
    setMoonAsset(null);
    if (dayNightRenderable != null) {
      dayNightRenderable.mesh.dispose();
      dayNightRenderable = null;
    }

    if (sunMonRenderable != null) {
      sunMonRenderable.mesh.dispose();
      sunMonRenderable = null;
    }
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    //renderables.add(buildSunMoon());
    renderables.add(getCubeRenderable());

  }

  private Renderable getCubeRenderable() {
    if (dayNightRenderable == null) {
      this.dayNightRenderable           = new DayNightSkyRenderable();
      this.dayNightRenderable.mesh      = buildMesh();

      dayNightRenderable.primitiveType  = GL30.GL_TRIANGLE_STRIP;
      dayNightRenderable.worldTransform.idt();
    }

    return dayNightRenderable;
  }

  private SunMonRenderable buildSunMoon() {
    if (sunMonRenderable == null) {

      this.sunMonRenderable           = new SunMonRenderable();
      TextureRegion  region           = new TextureRegion(getSunTexture());
      sunMonRenderable.mesh           = QuadBuilder.build(SATELITE_MESH_SIZE, region);
      sunMonRenderable.primitiveType  = GL30.GL_TRIANGLES;
    }

    return sunMonRenderable;
  }

  public void setSunAsset(TextureAsset newSunAsset) {
    if (sunAsset != null) {
      if (sunTexture != null) {
        sunTexture = null;
      }
      sunAsset.release();
      sunAsset = null;
    }
    this.sunAsset = newSunAsset;
    if (sunAsset != null)
      sunAsset.retain();
  }

  public TextureAsset getSunAsset() {
    return sunAsset;
  }

  public void setMoonAsset(TextureAsset newMoonAsset) {
    if (moonAsset != null) {
      if (moonTexture != null) {
        moonTexture = null;
      }
      moonAsset.release();
      moonAsset = null;
    }
    this.moonAsset = newMoonAsset;
    if (moonAsset != null)
      moonAsset.retain();
  }

  public TextureAsset getMoonAsset() {
    return moonAsset;
  }


  public Texture getSunTexture() {
    if (sunTexture == null) {
      sunTexture = sunAsset.get();
    }
    return sunTexture;
  }

  public Texture getMoonTexture() {
    if (moonTexture == null) {
      moonTexture = moonAsset.get();
    }

    return moonTexture;
  }
}
