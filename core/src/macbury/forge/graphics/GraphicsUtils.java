package macbury.forge.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by macbury on 15.10.14.
 */
public class GraphicsUtils {
  private float elapsedTime;

  /**
   * Clear GL20.GL_COLOR_BUFFER_BIT and GL20.GL_DEPTH_BUFFER_BIT with color
   * @param color - color to clear
   */
  public void clearAll(Color color) {
    Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
  }

  public void cullBackFace() {
    Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    Gdx.gl.glCullFace(GL20.GL_BACK);
  }

  public void depthBuffer(boolean enabled) {
    if (enabled) {
      Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
      //Gdx.gl.glDepthMask(enabled);
      Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
    } else {
      Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }
  }

  public void updateTime() {
    this.elapsedTime += Gdx.graphics.getDeltaTime();
  }

  public float getElapsedTime() {
    return elapsedTime;
  }
}
