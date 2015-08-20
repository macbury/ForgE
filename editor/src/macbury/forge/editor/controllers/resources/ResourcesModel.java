package macbury.forge.editor.controllers.resources;

import macbury.forge.ForgE;
import macbury.forge.shaders.utils.BaseShader;
import macbury.forge.shaders.utils.ShaderReloadListener;
import macbury.forge.shaders.utils.ShadersManager;

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
    rootNode.add(new GameScriptsFolderNode());
  }

  public class GameFolderNode extends BaseGameFolderNode {
    public GameFolderNode(String string) {
      super(string);
    }
  }

  public class GameShadersFolderNode extends BaseGameFolderNode implements ShaderReloadListener {
    public GameShadersFolderNode() {
      super("Shaders");
      ForgE.shaders.addOnShaderReloadListener(this);
      rebuildTree();
    }

    public void rebuildTree() {
      removeAllChildren();
      for (BaseShader shader : ForgE.shaders.all()) {
        add(new GameShaderNode(shader.getName()));
      }
    }

    @Override
    public void onShadersReload(ShadersManager shaderManager) {

    }

    @Override
    public void onShaderError(ShadersManager shaderManager, BaseShader program) {

    }
  }

  public class GameScriptsFolderNode extends BaseGameFolderNode {
    public GameScriptsFolderNode() {
      super("Scripts");

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
