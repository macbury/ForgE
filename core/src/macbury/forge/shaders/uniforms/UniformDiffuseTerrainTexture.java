package macbury.forge.shaders.uniforms;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import macbury.forge.ForgE;
import macbury.forge.level.LevelEnv;

/**
 * Created by macbury on 13.03.15.
 */
public class UniformDiffuseTerrainTexture extends BaseUniform {
  public final String UNIFORM_DIFFUSE_TEXTURE = "u_diffuseTexture";

  @Override
  public void bind(ShaderProgram shader, LevelEnv env, RenderContext context, Camera camera) {
    GLTexture terrainTexture = ForgE.blocks.getTerrainTexture();
    if (terrainTexture != null) {
      shader.setUniformi(UNIFORM_DIFFUSE_TEXTURE, context.textureBinder.bind(terrainTexture));
    }
  }

  @Override
  public void dispose() {

  }
}
