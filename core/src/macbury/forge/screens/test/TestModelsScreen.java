package macbury.forge.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import macbury.forge.ForgE;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 27.04.15.
 */
public class TestModelsScreen extends AbstractScreen {
  private Model model;

  @Override
  protected void initialize() {
    ModelLoader g3djLoader = new G3dModelLoader(new UBJsonReader());
    model = g3djLoader.loadModel(Gdx.files.internal("raw-models/test.g3db"));
  }

  @Override
  public void render(float delta) {
    ForgE.graphics.clearAll(Color.BLACK);
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
