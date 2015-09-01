package macbury.forge.editor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import macbury.forge.editor.controllers.BlocksController;
import macbury.forge.screens.AbstractScreen;

/**
 * Created by macbury on 13.03.15.
 */
public class PreviewScreen extends AbstractScreen {
  private static final String TAG = "PreviewScreen";
  private RenderContext renderContext;
  private PerspectiveCamera camera;

  @Override
  protected void onInitialize() {
    this.renderContext       = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.camera              = new PerspectiveCamera(67, 64, 64);
  }

  @Override
  public void render(float delta) {
    camera.update();
    Gdx.app.exit();
    /*
    iterate over each block, assemble it, apply texture, render it, save it to preview, after all end dispose everything and return callback
     */
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
