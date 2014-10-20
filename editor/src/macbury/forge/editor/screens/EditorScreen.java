package macbury.forge.editor.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.ForgE;
import macbury.forge.editor.systems.EditorSystem;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 18.10.14.
 */
public class EditorScreen extends AbstractScreen {
  private CameraInputController cameraController;
  private Level level;

  @Override
  protected void initialize() {
    this.level              = new Level(LevelState.blank());
    level.camera.position.set(0,15,15);
    level.camera.lookAt(Vector3.Zero);
    level.camera.update(true);
    level.entities.addSystem(new EditorSystem());
    this.cameraController   = new CameraInputController(level.camera);

    Gdx.input.setInputProcessor(cameraController);

    Entity tileEntity = level.entities.createEntity();

    /*TerrainBuilder assembler = new TerrainBuilder();

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
      for (int x = 0; x < 5; x++) {
        for (int y = 0; y < 5; y++) {
          for (int z = 0; z < 5; z++) {
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

      Renderable renderableCompnent = level.entities.createComponent(Renderable.class);
      renderableCompnent.instance = assembler.getRenderable();
      tileEntity.add(renderableCompnent);
      tileEntity.add(level.entities.createComponent(Position.class));
      level.entities.addEntity(tileEntity);
    } assembler.end();*/
  }

  @Override
  public void render(float delta) {
    ForgE.graphics.clearAll(Color.BLACK);
    cameraController.update();
    level.render(delta);
  }

  @Override
  public void resize(int width, int height) {
    level.resize(width, height);
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
    level.dispose();
  }

  public Level getLevel() {
    return level;
  }
}
