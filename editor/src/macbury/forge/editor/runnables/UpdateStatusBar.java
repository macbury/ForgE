package macbury.forge.editor.runnables;

import com.badlogic.gdx.Gdx;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.utils.FormatUtils;

import javax.swing.*;

/**
 * Created by macbury on 23.10.14.
 */
public class UpdateStatusBar {

  private final JLabel fpsLabel;
  private final JLabel statusMemoryLabel;
  private final JLabel statusRenderablesLabel;
  private final JLabel mapCursorPositionLabel;
  private final JLabel statusTriangleCountLabel;
  private ProjectController projectController;
  private float accumulator;
  public UpdateStatusBar(ProjectController projectController, JLabel fpsLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel, JLabel mapCursorPositionLabel, JLabel statusTriangleCountLabel) {
    this.fpsLabel = fpsLabel;
    this.statusMemoryLabel = statusMemoryLabel;
    this.mapCursorPositionLabel = mapCursorPositionLabel;
    this.statusRenderablesLabel = statusRenderablesLabel;
    this.projectController = projectController;
    this.statusTriangleCountLabel = statusTriangleCountLabel;
    this.update();
  }

  public void update() {
    accumulator += Gdx.graphics.getDeltaTime();

    if (accumulator >= 0.25f){
      accumulator = 0f;
      if (projectController.levelEditorScreen == null || projectController.levelEditorScreen.level == null) {
        fpsLabel.setText("...");
        statusMemoryLabel.setText("...");
        statusRenderablesLabel.setText("...");
        mapCursorPositionLabel.setText("...");
        statusTriangleCountLabel.setText("...");
      } else {
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        statusMemoryLabel.setText("Memory: " + FormatUtils.humanReadableByteCount(Gdx.app.getNativeHeap(), true) + "/" + FormatUtils.humanReadableByteCount(Gdx.app.getJavaHeap(), true));
        statusRenderablesLabel.setText("Renderables: " + String.valueOf(projectController.levelEditorScreen.level.batch.renderablesPerFrame));
        statusTriangleCountLabel.setText("Triangles: " + String.valueOf(Math.round(projectController.levelEditorScreen.level.batch.trianglesPerFrame)));

        mapCursorPositionLabel.setText(projectController.levelEditorScreen.selectionSystem.voxelCursor.replace.toString());
      }
    }

  }

}
