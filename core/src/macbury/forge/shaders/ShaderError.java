package macbury.forge.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by macbury on 23.07.15.
 */
public class ShaderError {
  private static final String TAG = "ShaderError";
  private final String log;
  private final String fragmentSrc;
  private final String vertexSrc;

  public ShaderError(ShaderProgram newShaderProgram, String fragmentSrc, String vertexSrc) {
    this.log         = newShaderProgram.getLog();
    this.fragmentSrc = fragmentSrc;
    this.vertexSrc   = vertexSrc;
  }

  public void print() {
    Gdx.app.error(TAG, "Error while compiling shader:");
    Gdx.app.error(TAG, log);
    Gdx.app.error(TAG, "Fragment SRC === ");
    int i = 0;
    for (String line : fragmentSrc.split("\n")) {
      Gdx.app.error(TAG, (++i) + " | " + line);
    }
    i = 0;
    Gdx.app.error(TAG, "Vertex SRC ===");

    for (String line : vertexSrc.split("\n")) {
      Gdx.app.error(TAG, (++i) + " | " + line);
    }
  }

  public String getFullLog() {
    String output = "Error while compiling shader: \n"
      + log + "\n";
    output += "Fragment SRC === \n";
    int i = 0;
    for (String line : fragmentSrc.split("\n")) {
      output += (++i) + " | " + line + "\n";
    }
    i = 0;
    output +=  "Vertex SRC ===\n";

    for (String line : vertexSrc.split("\n")) {
      output += (++i) + " | " + line + "\n";
    }

    return output;
  }
}
