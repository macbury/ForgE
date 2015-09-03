package macbury.forge.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;
import macbury.forge.ForgE;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.level.env.LevelEnv;
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
  private ModelBatch modelBatch;
  private Environment environment;
  private BoundingBox bounds = new BoundingBox();
  private Vector3 tmpV1 = new Vector3();
  private Vector3 tmpV2 = new Vector3();

  @Override
  protected void onInitialize() {
    this.levelEnv          = new LevelEnv();
    this.camera            = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.near = 0.1f;
    camera.position.set(5, 5, 5);
    camera.lookAt(0.1f, 0.1f, 0.1f);

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
    environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f));


    this.modelBatch        = new ModelBatch();
    //this.voxelBatch        = new VoxelBatch(new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED)));
    ModelLoader g3djLoader = new G3dModelLoader(new UBJsonReader());
    model                  = g3djLoader.loadModel(ForgE.files.internal("raw/models/test.g3db"));

    modelInstance          = new ModelInstance(model);

    modelInstance.calculateBoundingBox(bounds);
    camera.position.set(1, 1, 1).nor().scl(bounds.getDimensions(tmpV1).len() * 1.75f + bounds.getCenter(tmpV2).len());
    camera.up.set(0, 1, 0);
    camera.lookAt(0, 0, 0);
    camera.far = 50f + bounds.getDimensions(tmpV1).len() * 3.0f;
    camera.update(true);
  }

  @Override
  public void render(float delta) {
    camera.update();
    ForgE.graphics.clearAll(Color.BLACK);
    modelInstance.transform.rotate(Vector3.Y, 23 * delta);
    voxelBatch.begin(camera); {
      voxelBatch.add(modelInstance);

      voxelBatch.render(levelEnv);
    } voxelBatch.end();

    /*modelBatch.begin(camera); {
      modelBatch.render(modelInstance, environment);
    } modelBatch.end();*/
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
