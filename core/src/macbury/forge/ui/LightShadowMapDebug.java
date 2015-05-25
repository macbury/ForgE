package macbury.forge.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import macbury.forge.ForgE;
import macbury.forge.graphics.lighting.ShadowMap;

/**
 * Created by macbury on 22.05.15.
 */
public class LightShadowMapDebug extends Actor {

  private final TextureRegion region;
  private final ShadowMap shadowMap;

  public LightShadowMapDebug(ShadowMap shadowMap) {
    region = new TextureRegion();
    setWidth(Gdx.graphics.getWidth());
    setHeight(Gdx.graphics.getHeight());
    this.shadowMap = shadowMap;
    setZIndex(10);
  }



  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    Texture textureDescriptor = (Texture)shadowMap.getDepthTexture().texture;
    region.setTexture(textureDescriptor);
    region.setRegion(0, 0, textureDescriptor.getWidth(), textureDescriptor.getHeight());
    region.flip(false, true);

    batch.draw(region, getX(), getY(), getWidth(), getHeight());
  }
}
