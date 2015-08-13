package macbury.forge.graphics.skybox;

import com.badlogic.gdx.Gdx;
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
  private TextureAsset sateliteLightingAsset;
  private TextureAsset starsAlphaAsset;
  private Texture skyMapTexture;
  private Texture starsAlphaTexture;
  private Texture sunTexture;
  private Texture moonTexture;
  private DayNightSkyRenderable dayNightRenderable;
  private SunMonRenderable sunMonRenderable;
  private Matrix4 tempMat             = new Matrix4();
  private Vector3 tempPosition        = new Vector3();
  private Vector3 tempDirection       = new Vector3();
  private Vector3 tempCamPosition     = new Vector3();
  private Quaternion sateliteRotation = new Quaternion();
  private Color fogColor              = new Color();
  private Color ambientColor          = new Color();
  private Color sunColor              = new Color();
  @Override
  public void update(float delta, GameCamera camera) {
    updateFogColor();
    updateLighting();
  }

  private void updateLighting() {
    Pixmap lightingPixmap = sateliteLightingAsset.getPixmap();
    int maxW            = lightingPixmap.getWidth() - 1;
    int maxH            = lightingPixmap.getHeight() - 1;
    int mapPosA         = MathUtils.clamp(Math.round(maxW * ForgE.time.getProgress()), 0, maxW);
    sunColor.set(lightingPixmap.getPixel(mapPosA, maxH));
    //sunColor.set(Color.WHITE);
  }

  private void updateFogColor() {
    Pixmap skyMapPixmap = skyMapAsset.getPixmap();
    int maxW            = skyMapPixmap.getWidth() - 1;
    int maxH            = skyMapPixmap.getHeight() - 1;
    int mapPosA         = MathUtils.clamp(Math.round(maxW * ForgE.time.getProgress()), 0, maxW);

    fogColor.set(skyMapPixmap.getPixel(mapPosA, maxH));
    ambientColor.set(skyMapPixmap.getPixel(mapPosA, maxH - 2));

  }

  private void updateSatelites(GameCamera camera) {
    tempCamPosition.set(camera.position);
    tempMat.idt();
    tempMat.translate(tempCamPosition);
    tempMat.rotate(Vector3.X, ForgE.time.getSateliteRotation());
    tempMat.translate(0, 0, DISTANCE_TO_SUN);
    tempMat.getTranslation(tempPosition);

    CameraUtils.lookAt(tempCamPosition, tempPosition, Vector3.Y, sateliteRotation);

    if (sunMonRenderable != null) {
      sunMonRenderable.worldTransform.setToTranslation(tempPosition);
      sunMonRenderable.worldTransform.scl(SUN_SIZE);
      sunMonRenderable.worldTransform.rotate(sateliteRotation);
    }
  }

  @Override
  public void render(VoxelBatch batch, LevelEnv env, GameCamera camera) {
    super.render(batch, env, camera);
    updateSatelites(camera);
    renderSatelites(camera, env, batch);
  }

  private void renderSatelites(GameCamera camera, LevelEnv env, VoxelBatch batch) {
    //tempDirection.set(tempCamPosition).sub(tempPosition).nor();
    env.mainLight.direction.set(0,0,-1).rotate(Vector3.X, MathUtils.clamp(ForgE.time.getSateliteRotation(), -170, -10));

    env.mainLight.color.set(sunColor);
    env.ambientLight.set(ambientColor);
    env.fogColor.set(fogColor);
    env.skyColor.set(fogColor);
    batch.add(buildSunMoon());
    batch.render(env);
  }

  @Override
  public void dispose() {
    setSunAsset(null);
    setMoonAsset(null);
    setSkyMapAsset(null);
    setSateliteLightingAsset(null);
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


  public TextureAsset getSateliteLightingAsset() {
    return sateliteLightingAsset;
  }

  public void setSateliteLightingAsset(TextureAsset newSateliteLighting) {
    if (sateliteLightingAsset != null) {
      sateliteLightingAsset.release();
      sateliteLightingAsset = null;
    }
    this.sateliteLightingAsset = newSateliteLighting;
    if (sateliteLightingAsset != null) {
      sateliteLightingAsset.retain();
    }
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

}
