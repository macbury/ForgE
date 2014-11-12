package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.math.Matrix3;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.renderable.BaseRenderable;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.shaders.utils.RenderableBaseShader;

/**
 * Created by macbury on 18.10.14.
 */
public class TerrainShader extends RenderableBaseShader<VoxelFaceRenderable> {
  private final Matrix3 tempNormalMatrix = new Matrix3();
  @Override
  public boolean canRender(BaseRenderable instance) {
    return TerrainChunkRenderable.class.isInstance(instance);
  }

  @Override
  public void afterBegin() {
    context.setCullFace(GL30.GL_BACK);
    context.setDepthTest(GL20.GL_LEQUAL);

    setUniformSkyColor();
    setUniformSun();
    setUniformEyePosition();
    GLTexture terrainTexture = ForgE.blocks.getTerrainTexture();
    if (terrainTexture != null) {
      setUniformDiffuseTexture(terrainTexture);
    }

  }

  @Override
  public void beforeRender(VoxelFaceRenderable renderable) {
    tempNormalMatrix.set(renderable.worldTransform).inv().transpose();
    shader.setUniformMatrix(UNIFORM_WORLD_TRANSFORM, renderable.worldTransform);
    shader.setUniformMatrix(UNIFORM_NORMAL_MATRIX, tempNormalMatrix);
  }
}
