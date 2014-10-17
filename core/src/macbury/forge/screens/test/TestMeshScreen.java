package macbury.forge.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
  private CameraInputController cameraController;

  @Override
  protected void initialize() {
    this.camera             = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.cameraController   = new CameraInputController(camera);
    camera.position.set(0,10,10);
    camera.lookAt(Vector3.Zero);
    TerrainBuilder assembler = new TerrainBuilder();

    Vector3 position = new Vector3();
    Vector3 size     = new Vector3(1,1,1);

    Color green1 = new Color(14f / 255f, 123f / 255f, 34f / 255f, 1);
    Color green2 = new Color(44f / 255f, 159f / 255f, 93f / 255f, 1);
    Color green3 = new Color(82f / 255f, 198f / 255f, 152f / 255f, 1);

    Array<Color> pallette = new Array<Color>();
    pallette.add(green1);
    pallette.add(green2);
    pallette.add(green3);

    assembler.begin(); {
      for (int i = 0; i < pallette.size; i++) {
        Color color = pallette.get(i);
        position.add(1, 0,0);
        assembler.top(position, size, color);
        assembler.bottom(position, size, color);

        assembler.front(position, size, color);
        assembler.back(position, size, color);

        assembler.left(position, size, color);
        assembler.right(position, size, color);
      }

      this.triangleTest = assembler.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Color);
    } assembler.end();

    this.shader       = ForgE.shaders.get("mesh_test");

    Gdx.input.setInputProcessor(cameraController);
  }

  @Override
  public void render(float delta) {
    cameraController.update();
    camera.update();
    ForgE.graphics.clearAll(Color.BLACK);
    ForgE.graphics.depthBuffer(true);
    ForgE.graphics.cullBackFace();
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
