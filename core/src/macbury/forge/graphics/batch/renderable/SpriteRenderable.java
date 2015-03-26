package macbury.forge.graphics.batch.renderable;

import com.badlogic.gdx.graphics.GL30;
import macbury.forge.graphics.batch.sprites.BaseSprite3D;

/**
 * Created by macbury on 26.03.15.
 */
public class SpriteRenderable extends BaseRenderable {
  public void build(BaseSprite3D sprite) {
    this.mesh             = sprite.getMesh();
    this.primitiveType    = GL30.GL_TRIANGLES;
    this.triangleCount    = 2;
    this.haveTransparency = sprite.haveTransparency();
    sprite.applyToMatrix(this.worldTransform);
  }
}
