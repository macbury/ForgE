package macbury.forge.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.level.LevelEnv;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 27.04.15.
 */
public class TestModelsScreen extends AbstractScreen {
  private Model model;
  private VoxelBatch voxelBatch;
  private ModelInstance modelInstance;
  private PerspectiveCamera camera;
  private LevelEnv levelEnv;

  @Override
  protected void initialize() {
    this.levelEnv          = new LevelEnv();
    this.camera            = new PerspectiveCamera();

    camera.position.set(10,10,10);
    camera.lookAt(Vector3.Zero);

    this.voxelBatch        = new VoxelBatch(new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED)));
    ModelLoader g3djLoader = new G3dModelLoader(new UBJsonReader());
    model                  = g3djLoader.loadModel(Gdx.files.internal("raw-models/test.g3db"));

    modelInstance          = new ModelInstance(model);
  }

  @Override
  public void render(float delta) {
    camera.update();
    ForgE.graphics.clearAll(Color.BLACK);
    voxelBatch.begin(camera); {
      voxelBatch.add(modelInstance);

      voxelBatch.render(levelEnv);
    } voxelBatch.end();
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
    model.dispose();
  }
}
