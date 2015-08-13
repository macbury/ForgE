package macbury.forge.graphics.skybox;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.ForgE;
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
  private TextureAsset skyMapAsset;
  private TextureAsset starsAlphaAsset;
  private Texture skyMapTexture;
  private Texture starsAlphaTexture;
  private Texture sunTexture;
  private Texture moonTexture;
  private DayNightSkyRenderable dayNightRenderable;
  private SunMonRenderable sunMonRenderable;
  private Matrix4 tempMat       = new Matrix4();
  private Vector3 tempPosition  = new Vector3();
  private Vector3 tempCamPosition  = new Vector3();
  private Quaternion sateliteRotation = new Quaternion();
  private Pixmap skyMapPixmap;
  private Color fogA = new Color();
  private Color fogB = new Color();
  private Color fogColor = new Color();
  @Override
  public void update(float delta, GameCamera camera) {
    //rotation -= delta * 0.3f;
    updateSatelites(camera);
    updateFogColor();
  }

  private void updateFogColor() {
    int maxW    = getSkyMapPixmap().getWidth() - 1;
    int maxH    = getSkyMapPixmap().getHeight() - 1;
    int mapPosA = MathUtils.clamp((int) Math.floor(maxW * ForgE.time.getProgress()), 0, maxW);
    int mapPosB = MathUtils.clamp((int) Math.ceil(maxW * ForgE.time.getProgress()), 0, maxW);
    float alpha = ForgE.time.getProgressAlpha();
    fogA.set(getSkyMapPixmap().getPixel(mapPosA, maxH));
    fogB.set(getSkyMapPixmap().getPixel(mapPosB, maxH));

    fogColor.set(
        MathUtils.lerp(fogA.r, fogB.r, alpha),
        MathUtils.lerp(fogA.g, fogB.g, alpha),
        MathUtils.lerp(fogA.b, fogB.b, alpha),
        1.0f
    );
  }

  private void updateSatelites(GameCamera camera) {
    tempCamPosition.set(camera.position);
    tempMat.idt();
    tempMat.translate(tempCamPosition);
    tempMat.rotate(Vector3.X, ForgE.time.getSateliteRotation());
    tempMat.translate(0, 0, DISTANCE_TO_SUN);
    tempMat.getTranslation(tempPosition);

    CameraUtils.lookAt(tempCamPosition, tempPosition, camera.up, sateliteRotation);

    if (sunMonRenderable != null) {
      sunMonRenderable.worldTransform.setToTranslation(tempPosition);
      sunMonRenderable.worldTransform.scl(SUN_SIZE);
      sunMonRenderable.worldTransform.rotate(sateliteRotation);
    }
  }

  @Override
  public void render(VoxelBatch batch, LevelEnv env, GameCamera camera) {
    super.render(batch, env, camera);

    renderSatelites(camera, env, batch);
  }

  private void renderSatelites(GameCamera camera, LevelEnv env, VoxelBatch batch) {
    //env.mainLight.direction.set(tempCamPosition).sub(tempPosition).nor();
    env.fogColor.set(fogColor);

    batch.add(buildSunMoon());
    batch.render(env);
  }

  @Override
  public void dispose() {
    setSunAsset(null);
    setMoonAsset(null);
    setSkyMapAsset(null);
    if (dayNightRenderable != null) {
      dayNightRenderable.mesh.dispose();
      dayNightRenderable = null;
    }

    if (sunMonRenderable != null) {
      sunMonRenderable.mesh.dispose();
      sunMonRenderable = null;
    }
    skyMapPixmap = null;
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    //renderables.add(buildSunMoon());
    renderables.add(getCubeRenderable());

  }

  private Renderable getCubeRenderable() {
    if (dayNightRenderable == null) {
      this.dayNightRenderable           = new DayNightSkyRenderable();
      //this.dayNightRenderable.mesh      = buildMesh();
      MeshBuilder meshBuilder = new MeshBuilder();
      meshBuilder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.TexCoords(0)), GL30.GL_TRIANGLES); {
        meshBuilder.sphere(10,10,10,30,10);
      } this.dayNightRenderable.mesh    = meshBuilder.end();
      dayNightRenderable.primitiveType  = GL30.GL_TRIANGLES;
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

  public void setSkyMapAsset(TextureAsset newSkyMapAsset) {
    if (skyMapAsset != null) {
      skyMapPixmap = null;
      skyMapTexture = null;
      skyMapAsset.release();
      skyMapAsset = null;
    }
    this.skyMapAsset = newSkyMapAsset;
    if (skyMapAsset != null)
      skyMapAsset.retain();
  }

  public void setStarsAlphaAsset(TextureAsset newStarsAlphaAsset) {
    if (starsAlphaAsset != null) {
      starsAlphaTexture = null;
      starsAlphaAsset.release();
      starsAlphaAsset = null;
    }
    this.starsAlphaAsset = newStarsAlphaAsset;
    if (starsAlphaAsset != null)
      starsAlphaAsset.retain();
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

  public Texture getSateliteTextureByHour() {
    if (ForgE.time.isDay()) {
      return getSunTexture();
    } else {
      return getMoonTexture();
    }
  }

  public TextureAsset getSkyMapAsset() {
    return skyMapAsset;
  }

  public TextureAsset getStarsAlphaAsset() {
    return starsAlphaAsset;
  }

  public Texture getSkyMapTexture() {
    if (skyMapTexture == null) {
      skyMapTexture = skyMapAsset.get();
      skyMapTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
      skyMapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    return skyMapTexture;
  }

  public Pixmap getSkyMapPixmap() {
    if (skyMapPixmap == null) {
      if (!getSkyMapTexture().getTextureData().isPrepared()) {
        skyMapTexture.getTextureData().prepare();
      }
      skyMapPixmap = skyMapTexture.getTextureData().consumePixmap();
    }
    return skyMapPixmap;
  }
}
