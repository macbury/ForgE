package macbury.forge.editor.views;

import javax.swing.*;

/**
 * Created by macbury on 19.10.14.
 */
public class MainMenu extends JMenuBar {

  public MainMenu() {
    super();

    createProjectMenu();
    createDebugWindow();
  }

  private void createDebugWindow() {
    JMenu debugMenu         = new JMenu("Debug");
    JMenu viewModeMenu         = new JMenu("View mode");

    JMenuItem wireframeItem  = new JMenuItem("Wireframe");
    JMenuItem texturedItem   = new JMenuItem("Textured");

    viewModeMenu.add(wireframeItem);
    viewModeMenu.add(texturedItem);

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
