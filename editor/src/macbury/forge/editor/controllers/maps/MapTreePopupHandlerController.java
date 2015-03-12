package macbury.forge.editor.controllers.maps;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by macbury on 11.03.15.
 */
public class MapTreePopupHandlerController {
  private final JPopupMenu mapsMenu;
  private final JPopupMenu folderMenu;
  private final JMenuItem newMapMenuItem;
  private final JMenuItem deleteMapMenuItem;
  private final JMenuItem createFolderMenuItem;
  private final JMenuItem deleteFolderMenuItem;
  private final JTree tree;

  public MapTreePopupHandlerController(JTree tree) {
    this.tree                 = tree;
    this.newMapMenuItem       = new JMenuItem("New map");
    this.deleteMapMenuItem    = new JMenuItem("Delete map");
    this.createFolderMenuItem = new JMenuItem("Create folder");
    this.deleteFolderMenuItem = new JMenuItem("Delete folder");

    this.mapsMenu = new JPopupMenu();
    mapsMenu.add(newMapMenuItem);
    mapsMenu.add(deleteMapMenuItem);

    this.folderMenu = new JPopupMenu();
    folderMenu.add(newMapMenuItem);
    folderMenu.add(createFolderMenuItem);
    folderMenu.add(deleteFolderMenuItem);
  }


  public void showPopupFor(MouseEvent e) {
    if ( SwingUtilities.isRightMouseButton ( e ) ) {
      TreePath path = tree.getPathForLocation ( e.getX (), e.getY () );
      Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
      if ( pathBounds != null && pathBounds.contains ( e.getX (), e.getY () ) ) {
        MapTreeModel.BaseNode selectedNode = (MapTreeModel.BaseNode)path.getLastPathComponent();
        if (MapTreeModel.ProjectNode.class.isInstance(selectedNode)){
          folderMenu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
        } else {
          mapsMenu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
        }

      }
    }
  }
}
