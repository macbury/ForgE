package macbury.forge.editor.controllers.resources;

import icons.Utils;
import macbury.forge.editor.controllers.maps.MapTreeModel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by macbury on 23.07.15.
 */
public class ResourceTreeCellRenderer extends DefaultTreeCellRenderer {
  private Icon projectIcon = Utils.getIcon("node_root");
  private Icon defaultIcon = Utils.getIcon("node_map");
  private Icon folderIcon  = Utils.getIcon("node_folder");
  private Icon shaderIcon  = Utils.getIcon("shaders");
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
    Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
    if (ResourcesModel.GameFolderNode.class.isInstance(value)) {
      setIcon(projectIcon);
    } else if (ResourcesModel.GameShadersFolderNode.class.isInstance(value)) {
      setIcon(folderIcon);
    } else if (ResourcesModel.GameShaderNode.class.isInstance(value)) {
      setIcon(shaderIcon);
    } else {
      setIcon(defaultIcon);
    }
    return c;
  }
}
