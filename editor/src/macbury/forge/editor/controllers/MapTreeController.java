package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import icons.Utils;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.level.LevelManager;
import macbury.forge.level.LevelState;
import org.lwjgl.util.glu.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * Created by macbury on 10.03.15.
 */
public class MapTreeController implements OnMapChangeListener, ForgEBootListener {
  private static final String TAG = "MapTreeController";
  private final JTree mapTree;
  private final DefaultTreeCellRenderer treeCellRenderer;
  private MapNode root;

  public MapTreeController(JTree mapTree, ProjectController projectController) {
    this.mapTree = mapTree;
    mapTree.setVisible(false);
    this.treeCellRenderer = new DefaultTreeCellRenderer() {
      private Icon projectIcon = Utils.getIcon("node_root");
      private Icon folderIcon  = Utils.getIcon("node_folder");
      private Icon mapIcon     = Utils.getIcon("node_map");
      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
        if (ProjectNode.class.isInstance(value)) {
          setIcon(projectIcon);
        } else if (FolderNode.class.isInstance(value)) {
          setIcon(folderIcon);
        } else {
          setIcon(mapIcon);
        }

        return c;
      }
    };
    this.mapTree.setCellRenderer(treeCellRenderer);
  }


  private void reload() {
    ForgE.levels.reload();
    Gdx.app.log(TAG, "Reloading tree nodes");
    root = new ProjectNode(ForgE.db.title);
    buildTree(Gdx.files.internal(LevelState.MAP_STORAGE_DIR), root);
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    mapTree.setModel(treeModel);
    Gdx.app.log(TAG, "Done");
  }

  private void buildTree(FileHandle begin, MapNode parent)  {
    FileHandle[] newHandles = begin.list(ForgE.levels.mapAndDirFileFilter);
    for (FileHandle f : newHandles) {
      if (f.isDirectory()) {
        FolderNode node = new FolderNode(f.nameWithoutExtension());
        parent.add(node);
        buildTree(f, node);
      } else {
        int levelId       = ForgE.levels.getLevelId(f);
        LevelState state  = ForgE.levels.loadBasicLevelStateInfo(levelId);
        MapNode node      = new MapNode(state.getName(), levelId);
        parent.add(node);
      }
    }
  }

  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {

  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    reload();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    mapTree.setVisible(true);
    reload();
  }

  public class MapNode extends DefaultMutableTreeNode {
    private int id;

    public MapNode(String name, int id) {
      super(name);
      this.id     = id;
    }

    public int getId() {
      return id;
    }

  }

  public class FolderNode extends MapNode {
    public FolderNode(String name) {
      super(name, -1);
    }
  }

  public class ProjectNode extends MapNode {
    public ProjectNode(String name) {
      super(name, -1);
    }
  }
}
