package macbury.forge.graphics.skybox;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.DayNightSkyRenderable;
import macbury.forge.graphics.batch.renderable.SunMonRenderable;
import macbury.forge.graphics.batch.sprites.Sprite3D;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.utils.QuadBuilder;

/**
 * Created by macbury on 04.08.15.
 */
public class DayNightSkybox extends Skybox {
  private TextureAsset sunAsset;
  private TextureAsset moonAsset;
  private Texture sunTexture;
  private Texture moonTexture;
  private DayNightSkyRenderable dayNightRenderable;
  private SunMonRenderable sunMonRenderable;
  private final static float SATELITE_MESH_SIZE = 0.5f;

  @Override
  public void update(float delta) {

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
    //renderables.add(getCubeRenderable());
    renderables.add(buildSunMoon());
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
      sunMonRenderable.mesh           = QuadBuilder.build(0.5f, region);
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
