package macbury.forge.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.shaders.utils.ShadersManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by macbury on 20.08.15.
 */
public class FileForgeUtils {

  private static final String TAG = "FileUtils";

  public static FileHandle absolute(String path) {
    return Gdx.files.external(Gdx.files.internal(path).file().getAbsolutePath());
  }

  public static void writeString(FileHandle file, String text) {
    ForgE.log(TAG, "Writing: " + file.path());
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter( new FileWriter( file.path() ));
      writer.write( text);
    }
    catch ( IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if ( writer != null)
          writer.close( );
      }
      catch ( IOException e) {
        e.printStackTrace();
      }
    }
  }
}
