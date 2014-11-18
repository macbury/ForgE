package macbury.forge.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
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
  private final Vector3 mapSize          = new Vector3();
  private final String  UNIFORM_MAP_SIZE = "u_mapSize";
  @Override
  public boolean canRender(BaseRenderable instance) {
    return TerrainChunkRenderable.class.isInstance(instance);
  }

  @Override
  public void afterBegin() {
    setUniformSkyColor();
    setUniformSun();
    setUniformEyePosition();

    shader.setUniformf(UNIFORM_MAP_SIZE, env.terrainMap.getWidth(), env.terrainMap.getDepth());
    GLTexture terrainTexture = ForgE.blocks.getTerrainTexture();
    if (terrainTexture != null) {
      setUniformDiffuseTexture(terrainTexture);
    }

    context.setDepthTest(GL20.GL_LEQUAL);
  }

  @Override
  public void beforeRender(VoxelFaceRenderable renderable) {
    tempNormalMatrix.set(renderable.worldTransform).inv().transpose();
    shader.setUniformMatrix(UNIFORM_WORLD_TRANSFORM, renderable.worldTransform);
    shader.setUniformMatrix(UNIFORM_NORMAL_MATRIX, tempNormalMatrix);
    if (renderable.haveTransparency) {
      context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      context.setCullFace(GL30.GL_NONE);
    } else {
      context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      context.setCullFace(GL30.GL_BACK);
    }
  }

}
