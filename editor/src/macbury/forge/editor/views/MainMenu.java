package macbury.forge.editor.views;

import com.badlogic.gdx.Gdx;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.*;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.LevelEditorScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by macbury on 19.10.14.
 */
public class MainMenu extends JPopupMenu implements OnMapChangeListener {
  private final ProjectController controller;
  private final BlocksController blocksController;
  private final DockFramesController dockFrameController;
  private final ShadersController shadersController;
  private final PostProcessingController postProcessingController;
  private final UiController uiController;
  public JCheckBoxMenuItem debugRenderDynamicOctree;
  public JCheckBoxMenuItem debugBoundingBox;
  public JRadioButtonMenuItem debugWireframeItem;
  public JRadioButtonMenuItem debugTexturedItem;
  public JCheckBoxMenuItem debugRenderStaticOctree;
  private LevelEditorScreen editor;
  private JMenu debugRenderMenu;
  private JRadioButtonMenuItem debugNormalsItem;
  private JRadioButtonMenuItem debugLightingItem;

  public MainMenu(ProjectController projectController, BlocksController blocksController, DockFramesController dockFrameController, ShadersController shadersController, PostProcessingController postProcessingController, UiController uiController) {
    super();

    this.controller = projectController;
    this.blocksController = blocksController;
    this.dockFrameController = dockFrameController;
    this.shadersController   = shadersController;
    this.postProcessingController = postProcessingController;
    this.uiController             = uiController;
    //add(Box.createRigidArea(new Dimension(320,28)));
  }

  public void createAllMenus() {
    createCreateMenu();
    createMapMenu();
    createDebugWindow();
    createPiplineMenu();
    createExportFboMenu();
    addSeparator();
    add(dockFrameController.menu.getMenu());
  }

  private void createCreateMenu() {
    JMenu createMenu           = new JMenu("Create");
    JMenuItem mapItem    = new JMenuItem("New map");
    mapItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.newMap();
      }
    });
    createMenu.add(mapItem);

    JMenuItem shaderItem    = new JMenuItem("New shader");
    shaderItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        shadersController.newShader();
      }
    });
    createMenu.add(shaderItem);

    add(createMenu);
  }

  private void createExportFboMenu() {
    JMenu fboMenu          = new JMenu("Export FBO");
    postProcessingController.setMenu(fboMenu);

    add(fboMenu);
  }

  /**
   * Set Editor screen and refresh menu
   * @param levelEditorScreen
   */
  public void setEditor(LevelEditorScreen levelEditorScreen) {
    this.editor = levelEditorScreen;
    refresh();
  }

  public void refresh() {
    debugRenderDynamicOctree.setState(ForgE.config.getBool(Config.Key.RenderDynamicOctree));
    debugBoundingBox.setState(ForgE.config.getBool(Config.Key.RenderBoundingBox));
    debugRenderStaticOctree.setState(ForgE.config.getBool(Config.Key.RenderStaticOctree));

    if (editor == null) {
      debugRenderMenu.setVisible(false);
    } else {
      debugRenderMenu.setVisible(true);
      switch (ForgE.config.getRenderDebug()) {
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
    JMenuItem rebuildBlocks         = new JMenuItem("Rebuild blocks");

    rebuildBlocks.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        blocksController.rebuildTileset();
      }
    });

    assetsMenu.add(rebuildBlocks);

    JMenuItem rebuildUi         = new JMenuItem("Rebuild ui");

    rebuildUi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        uiController.rebuild();
      }
    });

    assetsMenu.add(rebuildUi);
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
        ForgE.config.putBool(Config.Key.RenderDynamicOctree, debugRenderDynamicOctree.getState());
      }
    });

    debugRenderStaticOctree.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.putBool(Config.Key.RenderStaticOctree, debugRenderStaticOctree.getState());
      }
    });

    debugBoundingBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.putBool(Config.Key.RenderBoundingBox, debugBoundingBox.getState());
      }
    });
  }


  private void createMapMenu() {
    JMenuItem closeMapItem    = new JMenuItem("Close map");
    closeMapItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.closeAndSaveChangesMap();
      }
    });
    add(closeMapItem);
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
    ForgE.config.putRenderDebug(renderType);
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    setEditor(null);
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    setEditor(screen);
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {

  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }
}
