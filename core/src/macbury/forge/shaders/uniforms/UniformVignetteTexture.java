package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.assets.assets.TextureAsset;
import macbury.forge.level.env.LevelEnv;
import macbury.forge.shaders.utils.BaseUniform;

/**
 * Created by macbury on 26.08.15.
 */
public class UniformVignetteTexture extends BaseUniform {
  private static final String UNIFORM_VIGNETTE_TEXTURE = "u_vignetteTexture";
  private final TextureAsset textureAsset;

  public UniformVignetteTexture() {
    this.textureAsset = ForgE.assets.getTexture("textures:vignette.png");
    textureAsset.retain();
  }

  @Override
  public void defineUniforms() {
    define(UNIFORM_VIGNETTE_TEXTURE, Texture.class);
  }

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    shader.setUniformi(UNIFORM_VIGNETTE_TEXTURE, context.textureBinder.bind(textureAsset.get()));
  }

  @Override
  public void dispose() {
    textureAsset.release();
  }
}
