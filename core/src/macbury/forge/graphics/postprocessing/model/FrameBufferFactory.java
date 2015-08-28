package macbury.forge.graphics.postprocessing.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import macbury.forge.ForgE;

/**
 * Created by macbury on 28.08.15.
 */
public class FrameBufferFactory {
  private static final String TAG = "FrameBufferFactory";
  public String name;
  public int width  = 512;
  public int height = 512;
  public Pixmap.Format format;
  public Texture.TextureWrap wrap;
  public Texture.TextureFilter filter;
  public boolean depth;

  public FrameBuffer build() {
    Gdx.app.log(TAG, "Building: " + name);
    return ForgE.fb.create(name, format, width, height, depth, wrap, filter);
  }
}
