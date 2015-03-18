package macbury.forge.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PlayerComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.level.Level;

/**
 * Created by macbury on 16.03.15.
 */
public class GameplayScreen extends AbstractScreen {
  private static final float FAR_CAMERA = 45;
  private static final float NEAR_CAMERA = 0.01f;
  private final Level level;
  private FirstPersonCameraController cameraController;

  public GameplayScreen(Level level) {
    this.level = level;
  }

  @Override
  protected void initialize() {
    Gdx.input.setCursorCatched(true);
    this.cameraController = new FirstPersonCameraController(level.camera);
    level.camera.position.set(50, 3, 50);
    level.camera.far  = FAR_CAMERA;
    level.camera.near = NEAR_CAMERA;
    //level.camera.lookAt(22,0,22);
    //Gdx.input.setInputProcessor(cameraController);


    Entity e          = level.entities.createEntity();
    PositionComponent position = level.entities.createComponent(PositionComponent.class);
    position.vector.set(50,3,50);
    position.size.set(1,1,1);
    MovementComponent movement = level.entities.createComponent(MovementComponent.class);
    movement.speed    = 15.4f;
    PlayerComponent player = level.entities.createComponent(PlayerComponent.class);
    player.camera = level.camera;

    e.add(player);
    e.add(movement);
    e.add(position);
    level.entities.addEntity(e);
  }

  @Override
  public void render(float delta) {
    cameraController.update(delta);
    level.render(delta);

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      Gdx.input.setCursorCatched(false);
    }
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
}
