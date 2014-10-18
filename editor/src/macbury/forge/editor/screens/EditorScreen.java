package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.TerrainTileRenderable;
import macbury.forge.graphics.builders.TerrainBuilder;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 18.10.14.
 */
public class EditorScreen extends AbstractScreen {
  private PerspectiveCamera camera;
  private CameraInputController cameraController;
  private TerrainTileRenderable terrainTile;
  public VoxelBatch batch;

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
      for (int x = 0; x < 10; x++) {
        for (int y = 0; y < 10; y++) {
          for (int z = 0; z < 10; z++) {
            Color color = pallette.get((int)Math.round((float)(pallette.size - 1) * Math.random()));
            position.set(x, y, z);
            assembler.top(position, size, color);
            assembler.bottom(position, size, color);

            assembler.front(position, size, color);
            assembler.back(position, size, color);

            assembler.left(position, size, color);
            assembler.right(position, size, color);
          }
        }
      }

      this.terrainTile = assembler.getRenderable();
    } assembler.end();

    this.batch = new VoxelBatch();
    Gdx.input.setInputProcessor(cameraController);
  }

  @Override
  public void render(float delta) {
    cameraController.update();
    camera.update();
    ForgE.graphics.clearAll(Color.BLACK);

    batch.begin(camera); {
      batch.add(terrainTile);
      batch.render();
    } batch.end();
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
