package macbury.forge.editor.runnables;

import com.badlogic.gdx.Gdx;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.utils.FormatUtils;

import javax.swing.*;

/**
 * Created by macbury on 23.10.14.
 */
public class UpdateStatusBar implements Runnable {

  private final JLabel fpsLabel;
  private final JLabel statusMemoryLabel;
  private final JLabel statusRenderablesLabel;
  private ProjectController projectController;

  public UpdateStatusBar(ProjectController projectController, JLabel fpsLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel) {
    this.fpsLabel = fpsLabel;
    this.statusMemoryLabel = statusMemoryLabel;
    this.statusRenderablesLabel = statusRenderablesLabel;
    this.projectController = projectController;
  }


  @Override
  public void run() {
    if (projectController.editorScreen == null || projectController.editorScreen.level == null) {
      fpsLabel.setText("...");
      statusMemoryLabel.setText("...");
      statusRenderablesLabel.setText("...");
    } else {
      fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
      statusMemoryLabel.setText("Memory: " + FormatUtils.humanReadableByteCount(Gdx.app.getNativeHeap(), true) + "/" + FormatUtils.humanReadableByteCount(Gdx.app.getJavaHeap(), true));
      statusRenderablesLabel.setText("Renderables: " + String.valueOf(projectController.editorScreen.level.batch.renderablesPerFrame));
    }
  }
}
