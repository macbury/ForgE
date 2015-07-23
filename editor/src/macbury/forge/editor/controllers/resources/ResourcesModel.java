package macbury.forge.editor.controllers.resources;

import macbury.forge.ForgE;
import macbury.forge.shaders.utils.BaseShader;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

/**
 * Created by macbury on 23.07.15.
 */
public class ResourcesModel extends DefaultTreeModel {
  private GameFolderNode rootNode;

  public ResourcesModel() {
    super(null);
  }


  public void refresh() {
    if (rootNode == null)
      initialize();
    setRoot(rootNode);
  }

  private void initialize() {
    this.rootNode = new GameFolderNode(ForgE.db.title);
    rootNode.add(new GameShadersFolderNode());
  }

  public class GameFolderNode extends BaseGameFolderNode {
    public GameFolderNode(String string) {
      super(string);
    }
  }

  public class GameShadersFolderNode extends BaseGameFolderNode {
    public GameShadersFolderNode() {
      super("Shaders");
      for (BaseShader shader : ForgE.shaders.all()) {
        add(new GameShaderNode(shader.getName()));
      }
    }
  }

  public class GameShaderNode extends BaseGameFolderNode {
    public GameShaderNode(String name) {
      super(name);
    }
  }

  public class BaseGameFolderNode extends DefaultMutableTreeNode {
    private String name;
    public BaseGameFolderNode(String string) {
      super(string);
      this.name = string;
    }
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
  }
}
