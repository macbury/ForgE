package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Mesh;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

/**
 * Created by macbury on 20.10.14.
 */
public abstract class RenderableBaseShader<T extends BaseRenderable> extends BaseShader {
  protected Mesh currentMesh;
  public final String UNIFORM_WORLD_TRANSFORM = "u_worldTransform";
  public final String UNIFORM_DIFFUSE_TEXTURE = "u_normalMatrix";
  public final String UNIFORM_NORMAL_MATRIX   = "u_diffuseTexture";

  public final String UNIFORM_SKY_COLOR              = "u_skyColor";
  public final String UNIFORM_AMBIENT_LIGHT          = "u_ambientLight";
  public final String UNIFORM_MAIN_LIGHT_COLOR       = "u_mainLight.color";
  public final String UNIFORM_MAIN_LIGHT_DIRECTION   = "u_mainLight.direction";

  public abstract boolean canRender (BaseRenderable instance);

  /**
   * Setup local uniforms for renderable
   * @param renderable
   */
  public abstract void beforeRender(final T renderable);

  public void render(final T renderable) {
    beforeRender(renderable);

    if (currentMesh != renderable.mesh) {
      if (currentMesh != null) {
        currentMesh.unbind(shader);
      }
      currentMesh = renderable.mesh;
      currentMesh.bind(shader);
    }
    currentMesh.render(shader, renderable.primitiveType, 0, currentMesh.getMaxIndices() > 0 ? currentMesh.getMaxIndices() : currentMesh.getMaxVertices(), false);
  }

  /**
   * Set UNIFORM_DIFFUSE_TEXTURE
   */
  public void setUniformDiffuseTexture(GLTexture texture) {
    shader.setUniformi(UNIFORM_DIFFUSE_TEXTURE, context.textureBinder.bind(texture));
  }

  /**
   * Set UNIFORM_EYE_POSITION to camera.position
   */
  public void setUniformEyePosition() {
    shader.setUniformf(UNIFORM_EYE_POSITION, camera.position.x, camera.position.y, camera.position.z, 1.1881f / (camera.far * camera.far));
  }

  /**
   * Set UNIFORM_AMBIENT_LIGHT, UNIFORM_MAIN_LIGHT_COLOR and UNIFORM_MAIN_LIGHT_DIRECTION from LevelEnv
   */
  public void setUniformSun() {
    shader.setUniformf(UNIFORM_AMBIENT_LIGHT, env.ambientLight);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_COLOR, env.mainLight.color);
    shader.setUniformf(UNIFORM_MAIN_LIGHT_DIRECTION, env.mainLight.direction);
  }

  /**
   * Set UNIFORM_SKY_COLOR from LevelEnv.skyColor
   */
  public void setUniformSkyColor() {
    shader.setUniformf(UNIFORM_SKY_COLOR, env.skyColor);
  }

  @Override
  public void end() {
    super.end();
    if (currentMesh != null) {
      currentMesh.unbind(shader);
    }
    currentMesh = null;
  }
}
