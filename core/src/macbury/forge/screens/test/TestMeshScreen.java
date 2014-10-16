package macbury.forge.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 15.10.14.
 */
public class TestMeshScreen extends AbstractScreen {

  private Mesh triangleTest;
  private ShaderProgram shader;
  private PerspectiveCamera camera;

  @Override
  protected void initialize() {
    this.camera             = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(0,5,5);
    camera.lookAt(Vector3.Zero);
    TerrainBuilder assembler = new TerrainBuilder();
    assembler.begin(); {
      assembler.topFace(0,0,0,1,1,1,0,0,0,0);
      assembler.topFace(0,0,1,1,1,1,0,0,0,0);
      assembler.topFace(1,0,1,1,1,1,0,0,0,0);
      this.triangleTest = assembler.mesh(MeshVertexInfo.AttributeType.Position);
    } assembler.end();

    this.shader       = ForgE.shaders.get("mesh_test");
  }

  @Override
  public void render(float delta) {
    camera.update();
    ForgE.graphics.clearAll(Color.BLACK);
    shader.begin(); {
      shader.setUniformMatrix("u_projViewTrans", camera.combined);
      triangleTest.render(shader, GL30.GL_TRIANGLES);
    } shader.end();
  }

  @Override
  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }

}
