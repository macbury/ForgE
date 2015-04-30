package macbury.forge.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import macbury.forge.ForgE;
import macbury.forge.components.*;
import macbury.forge.db.models.Teleport;
import macbury.forge.level.Level;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 16.03.15.
 */
public class GameplayScreen extends AbstractScreen {
  private static final float FAR_CAMERA = 100;
  private static final float NEAR_CAMERA = 0.01f;
  private final Level level;
  private final Teleport teleport;
  private FirstPersonCameraController cameraController;

  public GameplayScreen(Teleport teleport, Level level) {
    this.level    = level;
    this.teleport = teleport;
  }

  @Override
  protected void initialize() {
    Gdx.input.setCursorCatched(true);
    this.cameraController     = new FirstPersonCameraController(level.camera);
    level.camera.far          = FAR_CAMERA;
    level.camera.near         = NEAR_CAMERA;
    level.camera.fieldOfView  = 70;
    Entity playerEntity       = ForgE.entities.get("player").build(level.entities);
    playerEntity.getComponent(PlayerComponent.class).camera = level.camera;
    level.terrainMap.localVoxelPositionToWorldPosition(teleport.voxelPosition, playerEntity.getComponent(PositionComponent.class).vector);
    playerEntity.getComponent(PositionComponent.class).vector.sub(-0.5f);
    level.entities.addEntity(playerEntity);

    level.entities.addEntity(ForgE.entities.get("teapot").build(level.entities));

  }

  @Override
  public void render(float delta) {
    ForgE.assets.loadPending();
    cameraController.update(delta);
    level.render(delta);

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      Gdx.app.exit();
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
