package macbury.forge.editor.controllers.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import icons.Utils;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.level.LevelManager;
import macbury.forge.level.LevelState;
import org.lwjgl.util.glu.Project;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by macbury on 10.03.15.
 */
public class MapTreeController implements OnMapChangeListener, ForgEBootListener {
  private static final String TAG = "MapTreeController";
  private final JTree mapTree;
  private final ProjectController projectController;

  private final MapTreePopupHandlerController popupController;
  private final MapTreeModel mapTreeModel;

  public MapTreeController(JTree mapTree, ProjectController projectController) {
    this.popupController   = new MapTreePopupHandlerController(mapTree, projectController);
    this.mapTree           = mapTree;
    this.projectController = projectController;

    mapTree.setDragEnabled(true);
    mapTree.setDropMode(DropMode.ON_OR_INSERT);
    mapTree.setVisible(false);

    this.mapTree.setCellRenderer(new MapTreeCellRenderer());

    MouseListener ml = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        popupController.showPopupFor(e);
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          onMapSelected(e);
        }
      }
    };

    this.mapTreeModel = new MapTreeModel(mapTree);

    mapTree.addMouseListener(ml);
    mapTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
  }

  private void onMapSelected(MouseEvent e) {
    MapTreeModel.BaseNode selectedNode = (MapTreeModel.BaseNode)mapTree.getLastSelectedPathComponent();
    if (selectedNode != null && selectedNode.getId() != -1) {
      if (projectController.getCurrentLevelState() == null || projectController.getCurrentLevelState().getId() != selectedNode.getId()) {
        projectController.openMap(ForgE.levels.getFileHandle(selectedNode.getId()));
      }
    }
  }

  private void reload() {
    ForgE.levels.reload();
    Gdx.app.log(TAG, "Reloading tree nodes");
    mapTreeModel.refresh();
    Gdx.app.log(TAG, "Done");
  }


  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {

  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    reload();
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {
    reload();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    mapTree.setVisible(true);
    reload();
    mapTree.setModel(mapTreeModel);
  }

}
