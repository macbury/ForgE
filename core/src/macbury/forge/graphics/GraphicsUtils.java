package macbury.forge.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by macbury on 15.10.14.
 */
public class GraphicsUtils {
  /**
   * Clear GL20.GL_COLOR_BUFFER_BIT and GL20.GL_DEPTH_BUFFER_BIT with color
   * @param color - color to clear
   */
  public void clearAll(Color color) {
    Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
  }

}
