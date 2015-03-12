package macbury.forge.editor.controllers.maps;

import icons.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by macbury on 11.03.15.
 */
public class MapTreeCellRenderer extends DefaultTreeCellRenderer {
  private Icon projectIcon = Utils.getIcon("node_root");
  private Icon folderIcon  = Utils.getIcon("node_folder");
  private Icon mapIcon     = Utils.getIcon("node_map");
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
    Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
    if (MapTreeModel.ProjectNode.class.isInstance(value)) {
      setIcon(projectIcon);
    } else if (MapTreeModel.FolderNode.class.isInstance(value)) {
      setIcon(folderIcon);
    } else {
      setIcon(mapIcon);
    }
    return c;
  }
}
