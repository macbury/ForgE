package macbury.forge.editor.controllers;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.ShaderReloadListener;
import macbury.forge.shaders.utils.ShadersManager;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 * Created by macbury on 12.11.14.
 */
public class ShadersController implements DirectoryWatchJob.DirectoryWatchJobListener, ShaderReloadListener {
  private final JTextArea logArea;
  private DefaultSingleCDockable dockable;

  public ShadersController(DirectoryWatcher directoryWatcher) {
    this.logArea = new JTextArea();
    directoryWatcher.addListener(ShadersManager.SHADERS_PATH, this);
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
}
