package macbury.forge.editor.controllers.maps;

import com.badlogic.gdx.Gdx;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;

/**
 * Created by macbury on 13.03.15.
 */
public class MapNodeTreeMoveDragController extends DragSourceAdapter implements DragGestureListener, DropTargetListener {
  private static final String TAG = "MapNodeTreeMoveDragController";
  private final JTree tree;
  private final DragSource dragSource;
  private boolean isDragging = false;
  private MapTreeModel.BaseNode dropNode;
  private MapTreeModel.BaseNode currentDragNode;
  private Listener listener;
  public MapNodeTreeMoveDragController(DragSource dragSource, JTree mapTree, Listener listener) {
    this.tree = mapTree;
    this.dragSource = dragSource;
    this.listener   = listener;
  }

  @Override
  public void dragGestureRecognized(DragGestureEvent dge) {
    //ForgE.log(TAG, "drag started");
    dropNode                    = null;
    currentDragNode             = null;
    Point jap                   = dge.getDragOrigin(); // drag point
    int x                       = (int) jap.getX();
    int y                       = (int) jap.getY();
    TreePath tp                 = tree.getPathForLocation(x, y);
    if (tp == null || isDragging) {
      tree.getToolkit().beep();
    } else {
      currentDragNode = (MapTreeModel.BaseNode) tp.getLastPathComponent();
      if (currentDragNode.isMovable()) {
        dragSource.startDrag(dge, DragSource.DefaultMoveDrop, currentDragNode, this);
        isDragging = true;
      } else {
        tree.getToolkit().beep();
      }
    }
  }

  @Override
  public void dragDropEnd(DragSourceDropEvent dsde) {
    super.dragDropEnd(dsde);
    isDragging = false;
  }

  @Override
  public void dropActionChanged(DragSourceDragEvent e) {
    super.dropActionChanged(e);

  }

  @Override
  public void dragEnter(DragSourceDragEvent dsde) {
    super.dragEnter(dsde);
  }

  @Override
  public void dragOver(DragSourceDragEvent dsde) {
    super.dragOver(dsde);
    if (dropNode == null || !dropNode.canAcceptFolderOrMap()) {
      DragSourceContext context = dsde.getDragSourceContext();
      context.setCursor(DragSource.DefaultMoveNoDrop);
    } else {
      DragSourceContext context = dsde.getDragSourceContext();
      context.setCursor(DragSource.DefaultMoveDrop);
    }
  }

  @Override
  public void dragExit(DragSourceEvent dse) {
    super.dragExit(dse);
    dropNode = null;
  }

  @Override
  public void dragEnter(DropTargetDragEvent dtde) {
    dtde.acceptDrag(DnDConstants.ACTION_MOVE);
  }

  private void getDropNode(Point dragPoint) {
    TreePath path   = tree.getPathForLocation(dragPoint.x, dragPoint.y);
    if (path == null) {
      dropNode = null;
      //ForgE.log(TAG, "No node under cursor");
    } else {
      dropNode = (MapTreeModel.BaseNode)path.getLastPathComponent();
      //ForgE.log(TAG, "Node under cursor: " + dropNode.getName());
    }
  }

  @Override
  public void dragOver(DropTargetDragEvent dtde) {
    getDropNode(dtde.getLocation());
  }

  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {

  }

  @Override
  public void dragExit(DropTargetEvent dte) {

  }

  @Override
  public void drop(DropTargetDropEvent dtde) {
    //ForgE.log(TAG, "dropped");
    getDropNode(dtde.getLocation());

    if (dropNode == null) {
      tree.getToolkit().beep();
    } else {
      dtde.acceptDrop(DnDConstants.ACTION_MOVE);
      listener.onMapMoved(dropNode, currentDragNode);
    }

    dropNode = currentDragNode = null;
    dtde.dropComplete(true);
  }

  public interface Listener {
    public void onMapMoved(MapTreeModel.BaseNode target, MapTreeModel.BaseNode dragedNode);
  }
}
