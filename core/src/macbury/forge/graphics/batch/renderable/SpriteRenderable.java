package macbury.forge.graphics.batch.renderable;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.graphics.batch.sprites.BaseSprite3D;

/**
 * Created by macbury on 26.03.15.
 */
public class SpriteRenderable extends BaseRenderable {
  public Texture texture;

  public void build(BaseSprite3D sprite) {
    this.mesh             = sprite.getMesh();
    this.primitiveType    = GL30.GL_TRIANGLES;
    this.triangleCount    = 2;
    //this.haveTransparency = sprite.haveTransparency();
    this.texture          = sprite.getTexture();
    sprite.applyToMatrix(this.worldTransform);
  }
}
