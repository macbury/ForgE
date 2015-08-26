package macbury.forge.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import macbury.forge.ForgE;
import macbury.forge.graphics.fbo.Fbo;
import macbury.forge.graphics.fbo.FrameBufferManager;

/**
 * Created by macbury on 19.05.15.
 */
public class FullScreenFrameBufferResult extends Actor {

  private final TextureRegion region;
  private final String fboName;

  public FullScreenFrameBufferResult(String fboName) {
    region = new TextureRegion();
    this.fboName = fboName;
    setWidth(Gdx.graphics.getWidth());
    setHeight(Gdx.graphics.getHeight());
    setZIndex(0);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    Texture tex = ForgE.fb.get(fboName).getColorBufferTexture();
    region.setTexture(tex);
    region.setRegion(0, 0, tex.getWidth(), tex.getHeight());
    region.flip(false, true);

    batch.draw(region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }
}
