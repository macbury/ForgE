package macbury.forge.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

/**
 * Created by macbury on 13.03.15.
 */
public class ScreenshotFactory {

  private static int counter = 1;
  public static void saveScreenshot(FileHandle fh, int width, int height){
    try{
      Pixmap pixmap = getScreenshot(0, 0, width, height, true);
      PixmapIO.writePNG(fh, pixmap);
      pixmap.dispose();
    }catch (Exception e){
    }
  }

  private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
    final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

    if (yDown) {
      // Flip the pixmap upside down
      ByteBuffer pixels = pixmap.getPixels();
      int numBytes = w * h * 4;
      byte[] lines = new byte[numBytes];
      int numBytesPerLine = w * 4;
      for (int i = 0; i < h; i++) {
        pixels.position((h - i - 1) * numBytesPerLine);
        pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
      }
      pixels.clear();
      pixels.put(lines);
    }

    return pixmap;
  }
}