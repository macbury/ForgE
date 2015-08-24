package macbury.forge.editor.controllers.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.level.Level;
import macbury.forge.level.LevelState;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by macbury on 11.03.15.
 */
public class MapTreeModel extends DefaultTreeModel implements TreeExpansionListener {
  private static final String TAG = "MapTreeModel";
  private final JTree tree;
  private ProjectNode rootProject;
  private HashMap<String, BaseNode> pathNodesMap;
  private Array<String> expandedNodes;

  public MapTreeModel(JTree tree) {
    super(null);
    this.pathNodesMap  = new HashMap<String, BaseNode>();
    this.tree          = tree;
    this.expandedNodes = new Array<String>();
    tree.addTreeExpansionListener(this);
  }

  public void refresh() {
    pathNodesMap.clear();
    if (rootProject == null) {
      this.rootProject = new ProjectNode(ForgE.db.title);
      setRoot(sort(rootProject));
    }

    rootProject.removeAllChildren();
    rootProject.setName(ForgE.db.title);

    buildTree(Gdx.files.internal(LevelState.MAP_STORAGE_DIR), rootProject);

    reload();

    for (String path : expandedNodes) {
      if (pathNodesMap.containsKey(path)) {
        BaseNode node     = pathNodesMap.get(path);
        TreePath toExpand = new TreePath(node.getPath());
        tree.expandPath(toExpand);
      }
    }
  }

  private void buildTree(FileHandle begin, BaseNode parent)  {
    FileHandle[] newHandles = begin.list(ForgE.levels.mapAndDirFileFilter);
    for (FileHandle f : newHandles) {
      if (f.isDirectory()) {
        FolderNode node = new FolderNode(f.nameWithoutExtension(), f);
        parent.add(node);
        buildTree(f, node);
        pushNode(node);
      } else {
        int levelId       = ForgE.levels.getLevelId(f);
        try {
          LevelState state  = ForgE.levels.loadBasicLevelStateInfo(levelId);
          MapNode node      = new MapNode(state.getName(), levelId, f);
          parent.add(node);
          pushNode(node);
        } catch (Exception e) {
          Gdx.app.log(TAG, "Skipping: " + levelId);
          e.printStackTrace();
        }

      }
    }
  }

  private void pushNode(BaseNode node) {
    pathNodesMap.put(node.getPathFile(), node);
  }

  private BaseNode sort(BaseNode root) {
    for (int i = 0; i < root.getChildCount() - 1; i++) {
      BaseNode node = (BaseNode) root.getChildAt(i);
      String nt     = node.getUserObject().toString();

      for (int j = i + 1; j <= root.getChildCount() - 1; j++) {
        BaseNode prevNode = (BaseNode) root.getChildAt(j);
        String np         = prevNode.getUserObject().toString();

        if (nt.compareToIgnoreCase(np) > 0) {
          root.insert(node, j);
          break;
        }
      }
      if (node.getChildCount() > 0) {
        node = sort(node);
      }
    }

    return root;
  }

  @Override
  public void treeExpanded(TreeExpansionEvent event) {
    BaseNode node = (BaseNode)event.getPath().getLastPathComponent();
    int nodeIndex = expandedNodes.indexOf(node.getPathFile(), true);
    if (nodeIndex == -1) {
      expandedNodes.add(node.getPathFile());
    }
  }

  @Override
  public void treeCollapsed(TreeExpansionEvent event) {
    BaseNode node = (BaseNode)event.getPath().getLastPathComponent();
    int nodeIndex = expandedNodes.indexOf(node.getPathFile(), true);
    if (nodeIndex != -1) {
      expandedNodes.removeIndex(nodeIndex);
    }
  }

  public abstract class BaseNode extends DefaultMutableTreeNode implements Transferable {
    private String name;
    protected final String path;
    private int id;
    public DataFlavor DEFAULT_MUTABLE_TREENODE_FLAVOR = new DataFlavor(
        BaseNode.class, "BaseNode");
    public DataFlavor flavors[] = {DEFAULT_MUTABLE_TREENODE_FLAVOR,
        DataFlavor.stringFlavor};
    public BaseNode(String name, int id, FileHandle handle) {
      super(name);
      this.name   = name;
      this.id     = id;
      if (handle == null) {
        this.path   = "";
      } else {
        this.path   = handle.file().getAbsolutePath();
      }
    }

    public abstract boolean isMovable();

    public abstract boolean canAcceptFolderOrMap();

    public abstract String getDirectory();

    public String getPathFile() {
      return path;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
      setUserObject(name);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
      return  flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
      boolean returnValue = false;
      for (int i = 0, n = flavors.length; i < n; i++) {
        if (flavor.equals(flavors[i])) {
          returnValue = true;
          break;
        }
      }
      return returnValue;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      return this;
    }
  }

  public class MapNode extends BaseNode {
    public MapNode(String name, int id, FileHandle handle) {
      super(name, id, handle);
    }

    @Override
    public boolean isMovable() {
      return true;
    }

    @Override
    public boolean canAcceptFolderOrMap() {
      return false;
    }

    @Override
    public String getDirectory() {
      return new File(path).getParent();
    }


  }

  public class FolderNode extends BaseNode {
    public FolderNode(String name, FileHandle handle) {
      super(name, -1, handle);
    }

    @Override
    public boolean isMovable() {
      return true;
    }

    @Override
    public boolean canAcceptFolderOrMap() {
      return true;
    }

    @Override
    public String getDirectory() {
      try {
        return new File(path).getCanonicalPath() + File.separator;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
  }

  public class ProjectNode extends BaseNode {
    public ProjectNode(String name) {
      super(name, -1, null);
    }

    @Override
    public boolean isMovable() {
      return false;
    }

    @Override
    public boolean canAcceptFolderOrMap() {
      return true;
    }

    @Override
    public String getDirectory() {
      try {
        return Gdx.files.internal(".").file().getCanonicalPath() + File.separator + LevelState.MAP_STORAGE_DIR;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    public String getPathFile() {
      return getDirectory();
    }
  }
}
