package macbury.forge.editor.controllers;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import macbury.forge.ForgE;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.ShaderReloadListener;
import macbury.forge.shaders.utils.ShadersManager;
import macbury.forge.utils.FileForgeUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 * Created by macbury on 12.11.14.
 */
public class ShadersController implements DirectoryWatchJob.DirectoryWatchJobListener, ShaderReloadListener {
  private static final String TAG = "ShaderManager";
  private final JTextArea logArea;
  private final MainWindow mainWindow;
  private DefaultSingleCDockable dockable;

  private final static String VERT_TEMPLATE = "void main() {\n" +
      "  gl_Position       = u_projectionMatrix * u_worldTransform * a_position;\n" +
      "}";
  private final static String FRAG_TEMPLATE = "void main() {\n" +
      "  gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);\n" +
      "}";

  public ShadersController(DirectoryWatcher directoryWatcher, MainWindow mainWindow) {
    this.logArea = new JTextArea();
    directoryWatcher.addListener(ShadersManager.SHADERS_PATH, this);
    this.mainWindow = mainWindow;
  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.shaders.reload();
      }
    });
  }

  public Component buildLogs() {
    return new JScrollPane(logArea);
  }

  public void setDockable(DefaultSingleCDockable dockable) {
    this.dockable = dockable;
  }

  @Override
  public void onShadersReload(ShadersManager shaderManager) {
    logArea.setText("");
    dockable.setVisible(false);
  }

  @Override
  public void onShaderError(ShadersManager shaderManager, BaseShader program) {
    logArea.setText("");
    try {
      logArea.getDocument().insertString(logArea.getDocument().getEndPosition().getOffset(),program.getCurrentError().getFullLog()+"\n", null);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
    logArea.setCaretPosition(logArea.getDocument().getLength());
    dockable.setVisible(true);
    dockable.toFront();

  }

  public void newShader() {
    String shaderName = JOptionPane.showInputDialog(mainWindow, "Name your shader");
    if (shaderName != null && shaderName.length() > 3) {
      if (ForgE.shaders.get(shaderName) != null) {
        JOptionPane.showMessageDialog(mainWindow, "Shader exists!");
      } else {
        createShaderWithName(shaderName);
      }
    } else {
      JOptionPane.showMessageDialog(mainWindow, "Invalid name");
    }
  }

  private void createShaderWithName(String shaderName) {
    FileHandle fragFile = FileForgeUtils.absolute(ShadersManager.SHADERS_PATH + "/" + shaderName + ".frag.glsl");
    FileHandle vertFile = FileForgeUtils.absolute(ShadersManager.SHADERS_PATH + "/" + shaderName + ".vert.glsl");
    FileHandle jsonFile = FileForgeUtils.absolute(ShadersManager.SHADERS_PATH + "/" + shaderName + ".json");
    if (fragFile.exists() || vertFile.exists() || jsonFile.exists()) {
      JOptionPane.showMessageDialog(mainWindow, "Cannot create shader, there are files with similary name in shader dir!!!");
    } else {
      FileForgeUtils.writeString(fragFile, FRAG_TEMPLATE);
      FileForgeUtils.writeString(vertFile, VERT_TEMPLATE);
    }
  }
}
