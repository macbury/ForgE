package macbury.forge.editor.controllers.maps;

import com.badlogic.gdx.Gdx;
import macbury.forge.editor.controllers.ProjectController;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Created by macbury on 11.03.15.
 */
public class MapTreePopupHandlerController implements ActionListener {
  private static final String TAG = "MapTreePopupHandlerController";
  private final ProjectController projectController;
  private JPopupMenu mapsMenu;
  private final JMenuItem newMapMenuItem;
  private final JMenuItem deleteMapMenuItem;
  private final JMenuItem createFolderMenuItem;
  private final JMenuItem deleteFolderMenuItem;
  private final JTree tree;
  private MapTreeModel.BaseNode selectedNode;

  public MapTreePopupHandlerController(JTree tree, ProjectController projectController) {
    this.projectController    = projectController;
    this.tree                 = tree;
    this.newMapMenuItem       = buildMenuItem("New map");
    this.deleteMapMenuItem    = buildMenuItem("Delete map");
    this.createFolderMenuItem = buildMenuItem("Create folder");
    this.deleteFolderMenuItem = buildMenuItem("Delete folder");
  }

  private JMenuItem buildMenuItem(String title) {
    JMenuItem item = new JMenuItem(title);
    item.addActionListener(this);
    return item;
  }

  public void showPopupFor(MouseEvent e) {
    if (SwingUtilities.isRightMouseButton(e)) {
      TreePath path = tree.getPathForLocation( e.getX (), e.getY () );
      Rectangle pathBounds = tree.getUI().getPathBounds( tree, path );
      if (pathBounds != null && pathBounds.contains( e.getX(), e.getY() )) {
        setSelectedNode((MapTreeModel.BaseNode) path.getLastPathComponent());
        mapsMenu.show(tree, e.getX(), e.getY());
      }
    }
  }

  private void setSelectedNode(MapTreeModel.BaseNode node) {
    selectedNode    = node;

    //Gdx.app.log(TAG, selectedNode.getDirectory());
    tree.addSelectionPath(new TreePath(node.getPath()));
    buildMenuForNode(selectedNode);
  }

  private void buildMenuForNode(MapTreeModel.BaseNode node) {
    this.mapsMenu = new JPopupMenu();

    if (MapTreeModel.ProjectNode.class.isInstance(node)) {
      mapsMenu.add(newMapMenuItem);
      mapsMenu.add(createFolderMenuItem);
    } else if (MapTreeModel.FolderNode.class.isInstance(node)) {
      mapsMenu.add(newMapMenuItem);
      mapsMenu.add(createFolderMenuItem);
      mapsMenu.addSeparator();
      mapsMenu.add(deleteFolderMenuItem);
    } else if (MapTreeModel.MapNode.class.isInstance(node)) {
      mapsMenu.add(newMapMenuItem);
      mapsMenu.addSeparator();
      mapsMenu.add(deleteMapMenuItem);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == newMapMenuItem) {
      projectController.newMap(selectedNode.getDirectory());
    } else if (e.getSource() == deleteFolderMenuItem) {
      projectController.deleteFolder(selectedNode.getPathFile());
    } else if (e.getSource() == deleteMapMenuItem) {
      projectController.deleteMap(selectedNode.getId());
    } else if (e.getSource() == createFolderMenuItem) {
      projectController.createFolder(selectedNode.getDirectory());
    }
  }
}
