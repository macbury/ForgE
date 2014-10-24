package macbury.forge.editor.views;

import macbury.forge.ForgE;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.graphics.batch.VoxelBatch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 19.10.14.
 */
public class MainMenu extends JPopupMenu {
  public JCheckBoxMenuItem debugRenderDynamicOctree;
  public JCheckBoxMenuItem debugBoundingBox;
  public JRadioButtonMenuItem debugWireframeItem;
  public JRadioButtonMenuItem debugTexturedItem;
  public JCheckBoxMenuItem debugRenderStaticOctree;
  private EditorScreen editor;
  private JMenu debugRenderMenu;

  public MainMenu() {
    super();

    createProjectMenu();
    addSeparator();
    createDebugWindow();

    //add(Box.createRigidArea(new Dimension(320,28)));
  }

  /**
   * Set Editor screen and refresh menu
   * @param editorScreen
   */
  public void setEditor(EditorScreen editorScreen) {
    this.editor = editorScreen;
    refresh();
  }

  public void refresh() {
    debugRenderDynamicOctree.setState(ForgE.config.renderDynamicOctree);
    debugBoundingBox.setState(ForgE.config.renderBoundingBox);
    debugRenderStaticOctree.setState(ForgE.config.renderStaticOctree);

    if (editor == null) {
      debugRenderMenu.setVisible(false);
    } else {
      debugRenderMenu.setVisible(true);
      if (editor.level.batch.getType() == VoxelBatch.RenderType.Normal) {
        debugTexturedItem.setSelected(true);
      } else {
        debugWireframeItem.setSelected(false);
      }
    }
  }

  private void createDebugWindow() {
    JMenu viewModeMenu      = new JMenu("View mode");

    ButtonGroup group = new ButtonGroup();

    this.debugWireframeItem  = new JRadioButtonMenuItem("Wireframe");
    this.debugTexturedItem   = new JRadioButtonMenuItem("Textured");

    group.add(debugWireframeItem);
    group.add(debugTexturedItem);

    viewModeMenu.add(debugWireframeItem);
    viewModeMenu.add(debugTexturedItem);

    this.debugRenderMenu         = new JMenu("Render");

    this.debugRenderDynamicOctree = new JCheckBoxMenuItem("Render dynamic octree partitions");
    this.debugRenderStaticOctree  = new JCheckBoxMenuItem("Render static octree partitions");
    this.debugBoundingBox         = new JCheckBoxMenuItem("Render bounding boxes");

    debugRenderMenu.add(debugBoundingBox);
    debugRenderMenu.add(debugRenderDynamicOctree);
    debugRenderMenu.add(debugRenderStaticOctree);

    add(debugRenderMenu);
    add(viewModeMenu);

    debugWireframeItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editor.level.batch.setType(VoxelBatch.RenderType.Wireframe);
      }
    });

    debugTexturedItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editor.level.batch.setType(VoxelBatch.RenderType.Normal);
      }
    });

    debugRenderDynamicOctree.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderDynamicOctree = debugRenderDynamicOctree.getState();
      }
    });

    debugRenderStaticOctree.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderStaticOctree = debugRenderStaticOctree.getState();
      }
    });

    debugBoundingBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderBoundingBox = debugBoundingBox.getState();
      }
    });
  }

  private void createProjectMenu() {
    JMenu projectMenu         = new JMenu("Project");
    JMenuItem newProjectItem  = new JMenuItem("New");
    JMenuItem openProjectItem = new JMenuItem("Open");

    projectMenu.add(newProjectItem);
    projectMenu.add(openProjectItem);

    add(projectMenu);
  }
}
