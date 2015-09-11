package macbury.forge.editor.controllers.resources;

import com.badlogic.gdx.Gdx;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.shaders.utils.BaseShader;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by macbury on 23.07.15.
 */
public class ResourcesController extends MouseAdapter {
  private static final String TAG = "ResourcesController";
  private final JTree tree;
  private ResourcesModel model;

  public ResourcesController() {
    this.model = new ResourcesModel();
    tree       = new JTree(model);
    tree.setCellRenderer(new ResourceTreeCellRenderer());
    tree.setRootVisible(false);
    tree.addMouseListener(this);
  }

  public Component buildTree() {
    return new JScrollPane(tree);
  }

  public void refresh() {
    model.refresh();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    int selRow = tree.getRowForLocation(e.getX(), e.getY());
    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
    if(selRow != -1) {
      if(e.getClickCount() == 2) {
        onResourceDbClick(selRow, selPath);
      }
    }
  }

  private void onResourceDbClick(int selRow, TreePath selPath) {
    Object object = selPath.getLastPathComponent();
    if (ResourcesModel.GameShaderNode.class.isInstance(object)) {
      openShaderToEdit((ResourcesModel.GameShaderNode)object);
    } else {
      ForgE.log(TAG, "Double click on: " + object.toString() + " at index =" + selRow);
    }

  }

  private void openShaderToEdit(ResourcesModel.GameShaderNode node) {
    BaseShader shader = ForgE.shaders.get(node.getName());
    try {
      Runtime.getRuntime().exec(ForgE.config.getString(Config.Key.Editor) + " " + shader.getJsonFile().getAbsolutePath());
      Runtime.getRuntime().exec(ForgE.config.getString(Config.Key.Editor) + " " + shader.getFragmentFile().path());
      Runtime.getRuntime().exec(ForgE.config.getString(Config.Key.Editor) + " " + shader.getVertexFile().path());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
