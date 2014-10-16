package macbury.forge.screens;

import com.badlogic.gdx.graphics.Color;
import macbury.forge.ForgE;
import macbury.forge.graphics.mesh.MeshAssembler;
import macbury.forge.graphics.mesh.MeshVertexInfo;

/**
 * Created by macbury on 15.10.14.
 */
public class TestMeshScreen extends AbstractScreen {

  @Override
  protected void initialize() {
    MeshAssembler assembler = new MeshAssembler();
    assembler.begin(); {
      MeshVertexInfo v1 = assembler.vertex(1,1,1);
      MeshVertexInfo v2 = assembler.vertex(0,0,0);
      MeshVertexInfo v3 = assembler.vertex(1,0,0);
      assembler.triangle(v1, v2, v3);
    } assembler.end();
  }

  @Override
  public void render(float delta) {
    ForgE.graphics.clearAll(Color.BLUE);
  }

  @Override
  public void resize(int width, int height) {

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
