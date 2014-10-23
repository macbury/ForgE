package macbury.forge.editor.views;

import javax.swing.*;

/**
 * Created by macbury on 19.10.14.
 */
public class MainMenu extends JMenuBar {
  public JCheckBoxMenuItem debugShowOctree;
  public JCheckBoxMenuItem debugBoundingBox;
  public JRadioButtonMenuItem debugWireframeItem;
  public JRadioButtonMenuItem debugTexturedItem;

  public MainMenu() {
    super();

    createProjectMenu();
    createDebugWindow();
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

    this.debugShowOctree  = new JCheckBoxMenuItem("Render octree partitions");
    this.debugBoundingBox = new JCheckBoxMenuItem("Render bounding boxes");
    debugMenu.add(debugShowOctree);
    debugMenu.add(debugBoundingBox);
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
