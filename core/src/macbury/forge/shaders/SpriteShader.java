package macbury.forge.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.SpriteRenderable;
import macbury.forge.level.LevelEnv;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 27.03.15.
 */
public class SpriteShader extends RenderableBaseShader<SpriteRenderable> {
  public final String UNIFORM_SPRITE_TEXTURE = "u_spriteTexture";
  @Override
  public boolean canRender(BaseRenderable instance) {
    return false;
  }

  @Override
  public void begin(Camera camera, RenderContext context, LevelEnv env) {
    super.begin(camera, context, env);
    context.setDepthMask(true);
    context.setDepthTest(GL30.GL_LEQUAL);
  }

  @Override
  public void beforeRender(SpriteRenderable renderable) {
    shader.setUniformi(UNIFORM_SPRITE_TEXTURE, context.textureBinder.bind(renderable.texture));
    shader.setUniformMatrix(UNIFORM_WORLD_TRANSFORM, renderable.worldTransform);

    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    context.setCullFace(GL30.GL_BACK);
  }

  @Override
  public void afterBegin() {

  }
}
