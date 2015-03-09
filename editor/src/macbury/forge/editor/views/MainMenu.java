package macbury.forge.editor.views;

import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.parell.jobs.BuildBlocksTexture;
import macbury.forge.editor.screens.EditorScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 19.10.14.
 */
public class MainMenu extends JPopupMenu implements OnMapChangeListener {
  private final ProjectController controller;
  public JCheckBoxMenuItem debugRenderDynamicOctree;
  public JCheckBoxMenuItem debugBoundingBox;
  public JRadioButtonMenuItem debugWireframeItem;
  public JRadioButtonMenuItem debugTexturedItem;
  public JCheckBoxMenuItem debugRenderStaticOctree;
  private EditorScreen editor;
  private JMenu debugRenderMenu;
  private JRadioButtonMenuItem debugNormalsItem;
  private JRadioButtonMenuItem debugLightingItem;

  public MainMenu(ProjectController projectController) {
    super();

    this.controller = projectController;

    createProjectMenu();
    addSeparator();
    createDebugWindow();
    addSeparator();
    createPiplineMenu();
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
      switch (ForgE.config.renderDebug) {
        case Normals:
          debugNormalsItem.setSelected(true);
        break;
        case Lighting:
          debugLightingItem.setSelected(true);
        break;
        case Wireframe:
          debugWireframeItem.setSelected(true);
          break;
        default:
          debugTexturedItem.setSelected(true);
        break;
      }
    }
  }

  private void createPiplineMenu() {
    JMenu assetsMenu          = new JMenu("Assets");
    JMenuItem rebuild         = new JMenuItem("Rebuild blocks");

    rebuild.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.jobs.enqueue(new BuildBlocksTexture());
      }
    });

    assetsMenu.add(rebuild);
    add(assetsMenu);
  }

  private void createDebugWindow() {
    JMenu viewModeMenu      = new JMenu("View mode");

    ButtonGroup group = new ButtonGroup();

    this.debugWireframeItem  = new JRadioButtonMenuItem("Wireframe");
    this.debugTexturedItem   = new JRadioButtonMenuItem("Textured");
    this.debugNormalsItem    = new JRadioButtonMenuItem("Normals");
    this.debugLightingItem   = new JRadioButtonMenuItem("Lighting");

    group.add(debugWireframeItem);
    group.add(debugTexturedItem);
    group.add(debugLightingItem);
    group.add(debugNormalsItem);


    viewModeMenu.add(debugTexturedItem);
    viewModeMenu.add(debugWireframeItem);
    viewModeMenu.add(debugNormalsItem);
    viewModeMenu.add(debugLightingItem);

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
        setRenderingMode(Config.RenderDebug.Wireframe);
      }
    });

    debugNormalsItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setRenderingMode(Config.RenderDebug.Normals);
      }
    });

    debugLightingItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setRenderingMode(Config.RenderDebug.Lighting);
      }
    });

    debugTexturedItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setRenderingMode(Config.RenderDebug.Textured);
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
    JMenu projectMenu           = new JMenu("Project");
    JMenuItem newProjectItem    = new JMenuItem("New map");
    JMenuItem openProjectItem   = new JMenuItem("Open map");
    JMenuItem saveProjectItem   = new JMenuItem("Save map");

    newProjectItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.newMap();
      }
    });

    projectMenu.add(newProjectItem);
    projectMenu.add(openProjectItem);
    projectMenu.add(saveProjectItem);

    add(projectMenu);
  }

  private void setRenderingMode(Config.RenderDebug renderType) {
    ForgE.config.setRenderDebugTo(renderType);
  }

  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {
    setEditor(null);
  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    setEditor(screen);
  }
}
