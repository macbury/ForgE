package macbury.forge.editor.controllers.maps;

import com.badlogic.gdx.Gdx;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.controllers.tools.ToolsController;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.selection.EventSelection;
import macbury.forge.editor.systems.SelectionSystem;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by macbury on 10.03.15.
 */
public class MapTreeController implements OnMapChangeListener, ForgEBootListener, MapNodeTreeMoveDragController.Listener, ToolsController.ToolControllerListener {
  private static final String TAG = "MapTreeController";
  private final JTree mapTree;
  private final ProjectController projectController;

  private final MapTreePopupHandlerController popupController;
  private final MapTreeModel mapTreeModel;
  private final DropTarget dropTarget;
  private LevelEditorScreen screen;
  private SelectionSystem selectionSystem;

  public MapTreeController(final JTree mapTree, ProjectController projectController) {
    this.popupController   = new MapTreePopupHandlerController(mapTree, projectController);
    this.mapTree           = mapTree;
    this.projectController = projectController;

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

    //DragSource dragSource = DragSource.getDefaultDragSource();
   // dragSource.createDefaultDragGestureRecognizer(mapTree, DnDConstants.ACTION_COPY_OR_MOVE, new MapNodeTreeMoveDragSource(mapTree));
    DragSource dragSource                          = new DragSource();
    MapNodeTreeMoveDragController dragSourceController = new MapNodeTreeMoveDragController(dragSource, mapTree, this);


    DragGestureRecognizer dgr                      = dragSource.createDefaultDragGestureRecognizer(mapTree, DnDConstants.ACTION_MOVE, dragSourceController);
    dropTarget                                     = new DropTarget(mapTree, dragSourceController);

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
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    screen = null;
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    reload();
    this.screen = screen;
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {
    reload();
  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {
    reload();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    mapTree.setVisible(true);
    reload();
    mapTree.setModel(mapTreeModel);
  }

  @Override
  public void onMapMoved(MapTreeModel.BaseNode target, MapTreeModel.BaseNode dragedNode) {
    projectController.moveMap(dragedNode.getPathFile(), target.getPathFile());
  }

  @Override
  public void onToolPaneUnSelected(SelectionSystem system) {

  }

  @Override
  public void onToolPaneSelected(ToolsController.ToolControllerListener selectedToolController, SelectionSystem system) {
    this.selectionSystem = system;
    selectionSystem.setSelection(new EventSelection());
  }
}
