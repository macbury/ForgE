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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by macbury on 10.03.15.
 */
public class MapTreeController implements OnMapChangeListener, ForgEBootListener {
  private static final String TAG = "MapTreeController";
  private final JTree mapTree;
  private final DefaultTreeCellRenderer treeCellRenderer;
  private final ProjectController projectController;
  private final JPopupMenu mapsMenu;
  private MapNode root;

  public MapTreeController(JTree mapTree, ProjectController projectController) {
    this.mapTree           = mapTree;
    this.projectController = projectController;
    mapTree.setDragEnabled(true);
    mapTree.setDropMode(DropMode.ON_OR_INSERT);
    mapTree.setVisible(false);

    this.mapsMenu = new JPopupMenu();
    mapsMenu.add (new JMenuItem ( "New map" ));
    mapsMenu.add (new JMenuItem ( "New folder" ));
    mapsMenu.add (new JMenuItem ( "Delete map" ));
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

    MouseListener ml = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        showPopupFor(e);
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          onMapSelected(e);
        }
      }
    };

    mapTree.addMouseListener(ml);
    mapTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
  }

  private void showPopupFor(MouseEvent e) {
    if ( SwingUtilities.isRightMouseButton ( e ) ) {
      TreePath path = mapTree.getPathForLocation ( e.getX (), e.getY () );
      Rectangle pathBounds = mapTree.getUI ().getPathBounds ( mapTree, path );
      if ( pathBounds != null && pathBounds.contains ( e.getX (), e.getY () ) ) {

        mapsMenu.show(mapTree, pathBounds.x, pathBounds.y + pathBounds.height);
      }
    }
  }

  private void onMapSelected(MouseEvent e) {
    MapNode selectedNode = (MapNode)mapTree.getLastSelectedPathComponent();
    if (selectedNode != null && selectedNode.getId() != -1) {
      if (projectController.getCurrentLevelState() == null || projectController.getCurrentLevelState().getId() != selectedNode.getId()) {
        projectController.openMap(ForgE.levels.getFileHandle(selectedNode.getId()));
      }
    }
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
