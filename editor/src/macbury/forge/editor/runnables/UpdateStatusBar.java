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
  private final JLabel mapCursorPositionLabel;
  private final JLabel statusTriangleCountLabel;
  private ProjectController projectController;

  public UpdateStatusBar(ProjectController projectController, JLabel fpsLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel, JLabel mapCursorPositionLabel, JLabel statusTriangleCountLabel) {
    this.fpsLabel = fpsLabel;
    this.statusMemoryLabel = statusMemoryLabel;
    this.mapCursorPositionLabel = mapCursorPositionLabel;
    this.statusRenderablesLabel = statusRenderablesLabel;
    this.projectController = projectController;
    this.statusTriangleCountLabel = statusTriangleCountLabel;
  }


  @Override
  public void run() {
    if (projectController.editorScreen == null || projectController.editorScreen.level == null) {
      fpsLabel.setText("...");
      statusMemoryLabel.setText("...");
      statusRenderablesLabel.setText("...");
      mapCursorPositionLabel.setText("...");
      statusTriangleCountLabel.setText("...");
    } else {
      fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
      statusMemoryLabel.setText("Memory: " + FormatUtils.humanReadableByteCount(Gdx.app.getNativeHeap(), true) + "/" + FormatUtils.humanReadableByteCount(Gdx.app.getJavaHeap(), true));
      statusRenderablesLabel.setText("Renderables: " + String.valueOf(projectController.editorScreen.level.batch.renderablesPerFrame));
      statusTriangleCountLabel.setText("Triangles: " + String.valueOf(projectController.editorScreen.level.batch.trianglesPerFrame));
      mapCursorPositionLabel.setText(projectController.editorScreen.editorSystem.intersectionPoint.toString());
    }
  }
}
