package macbury.forge.editor.views;

import javax.swing.*;

/**
 * Created by macbury on 19.10.14.
 */
public class MainMenu extends JPopupMenu {
  public JCheckBoxMenuItem debugRenderDynamicOctree;
  public JCheckBoxMenuItem debugBoundingBox;
  public JRadioButtonMenuItem debugWireframeItem;
  public JRadioButtonMenuItem debugTexturedItem;
  public JCheckBoxMenuItem debugRenderStaticOctree;

  public MainMenu() {
    super();

    createProjectMenu();
    createDebugWindow();

    //add(Box.createRigidArea(new Dimension(320,28)));
  }

  private void createDebugWindow() {
    JMenu debugMenu         = new JMenu("Debug");
    JMenu viewModeMenu      = new JMenu("View mode");

    ButtonGroup group = new ButtonGroup();

    this.debugWireframeItem  = new JRadioButtonMenuItem("Wireframe");
    this.debugTexturedItem   = new JRadioButtonMenuItem("Textured");

    group.add(debugWireframeItem);
    group.add(debugTexturedItem);

    viewModeMenu.add(debugWireframeItem);
    viewModeMenu.add(debugTexturedItem);

    JMenu debugRenderMenu         = new JMenu("Render");

    this.debugRenderDynamicOctree = new JCheckBoxMenuItem("Render dynamic octree partitions");
    this.debugRenderStaticOctree  = new JCheckBoxMenuItem("Render static octree partitions");
    this.debugBoundingBox         = new JCheckBoxMenuItem("Render bounding boxes");

    debugRenderMenu.add(debugBoundingBox);
    debugRenderMenu.add(debugRenderDynamicOctree);
    debugRenderMenu.add(debugRenderStaticOctree);

    debugMenu.add(debugRenderMenu);
    debugMenu.add(viewModeMenu);
    add(debugMenu);
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
