package macbury.forge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.ForgE;
import macbury.forge.graphics.postprocessing.PostProcessingManager;
import macbury.forge.level.LevelState;
import macbury.forge.scripts.ScriptManager;
import macbury.forge.ui.UIManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by macbury on 28.08.15.
 */
public class FileManager implements Disposable {

  private static final String TAG = "FileManager";
  private HashMap<String, String> pathMappings;
  private boolean classpathFileStore = false;
  public FileManager() {
    pathMappings  = new HashMap<String, String>();

    configureMappings();
    checkMappings();
  }

  private void checkMappings() {
    classpathFileStore = Gdx.files.classpath("graphics").exists();
    Gdx.app.log(TAG, "Initialized...");
  }

  private void configureMappings() {
    putMapping("textures", "graphics/textures/");
    putMapping("skybox", "graphics/textures/skybox/");
    putMapping("sounds", "audio/sounds/");
    putMapping("maps", LevelState.MAP_STORAGE_DIR);
    putMapping("scripts", ScriptManager.SCRIPTS_DIR);
    putMapping("postprocessing", PostProcessingManager.STORAGE_DIR);
    putMapping("ui", UIManager.STORE_PATH);
  }

  private String applyMapping(String path) {
    for(String mapping : pathMappings.keySet()) {
      if (path.startsWith(mapping)) {
        return path.replace(mapping, pathMappings.get(mapping));
      }
    }
    return path;
  }

  private void putMapping(String key, String path) {
    pathMappings.put(key + ":", path);
    Gdx.app.log(TAG, "Mapping: " + key + " is " + path);
  }

  public FileHandle internal(String path) {
    path = applyMapping(path);
    if (classpathFileStore) {
      return Gdx.files.classpath(path);
    } else {
      return Gdx.files.internal(path);
    }
  }

  public Array<FileHandle> listRecursive(String path) {
    Array<FileHandle> handles = new Array<FileHandle>();
    getHandles(internal(path), handles);
    return handles;
  }

  private void getHandles(FileHandle begin, Array<FileHandle> handles)  {
    //Gdx.app.log(TAG, "begin: " + begin.path());
    Array<FileHandle> newHandles = ForgE.files.list(begin.path());
    for (FileHandle f : newHandles) {
      if ((f.isDirectory() || f.extension().length() == 0) ) {
        //Gdx.app.log(TAG, "in: " + f.path());
        if (!f.path().equals(begin.path())) {
          getHandles(f, handles);
        }
      } else {
        handles.add(f);
      }
    }
  }

  public Array<FileHandle> list(String path) {
    return list(path, null);
  }

  public Array<FileHandle> list(String path, FilenameFilter filenameFilter) {
    path = applyMapping(path);
    if (!path.endsWith("/"))
      path += "/";
    if (classpathFileStore) {
      try {
        String[] rawPaths = getResourceListing(ForgE.class, path);
        Array<FileHandle> handles = new Array<FileHandle>();
        for (int i = 0; i < rawPaths.length; i++) {
          FileHandle fileHandle = internal(path+rawPaths[i]);
          if (filenameFilter == null) {
            handles.add(fileHandle);
          } else if (filenameFilter.accept(fileHandle.file(), fileHandle.name())) {
            handles.add(fileHandle);
          }
        }
        return handles;
      } catch (URISyntaxException e) {
        e.printStackTrace();
        return null;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    } else {
      if (filenameFilter == null) {
        return new Array<FileHandle>(Gdx.files.internal(path).list());
      } else {
        return new Array<FileHandle>(Gdx.files.internal(path).list(filenameFilter));
      }

    }
  }

  private String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
    URL dirURL = clazz.getClassLoader().getResource(path);
    if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
      return new File(dirURL.toURI()).list();
    }

    if (dirURL == null) {
        /*
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
      String me = clazz.getName().replace(".", "/")+".class";
      dirURL = clazz.getClassLoader().getResource(me);
    }

    if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
      String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
      JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
      Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
      Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
      while(entries.hasMoreElements()) {
        String name = entries.nextElement().getName();
        if (name.startsWith(path)) { //filter according to the path
          String entry = name.substring(path.length());
          int checkSubdir = entry.indexOf("/");
          if (checkSubdir >= 0) {
            // if it is a subdirectory, we just return the directory name
            entry = entry.substring(0, checkSubdir);
          }
          result.add(entry);
        }
      }
      return result.toArray(new String[result.size()]);
    }

    throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  }

  @Override
  public void dispose() {

  }
}
