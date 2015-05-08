package macbury.forge.editor.controllers;

import bibliothek.gui.dock.common.*;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.windows.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by macbury on 07.05.15.
 */
public class DockFramesController implements MainToolbarController.EditorModeListener {
  private final CControl control;
  public final RootMenuPiece menu;
  private final DefaultSingleCDockable mapEditorDockable;
  private final DefaultSingleCDockable terrainToolsDockable;
  private final DefaultSingleCDockable inspectorDockable;
  private final DefaultSingleCDockable resourcesDockable;
  private final DefaultSingleCDockable objectsDockable;

  public DockFramesController(MainWindow mainWindow) {
    control = new CControl( mainWindow );
    control.setTheme(ThemeMap.KEY_ECLIPSE_THEME);
    mainWindow.mainContentPane.add(control.getContentArea(), BorderLayout.CENTER);

    this.mapEditorDockable = new DefaultSingleCDockable( "Map", "Map", mainWindow.openGlContainer );
    mapEditorDockable.setExtendedMode(ExtendedMode.NORMALIZED);
    mapEditorDockable.setSticky(false);
    mapEditorDockable.setCloseable(false);
    mapEditorDockable.setMaximizable(false);
    mapEditorDockable.setStackable(false);
    mapEditorDockable.setMinimizable(false);
    mapEditorDockable.setExternalizable(false);

    this.terrainToolsDockable   = createDockablePanel("Terrain", mainWindow.terrainPanel, true);


    DefaultSingleCDockable mapTreeDockable   = createDockablePanel("Maps", mainWindow.mapTreeScroll, true);
    mapTreeDockable.setLocation( CLocation.base().minimalEast() );
    this.inspectorDockable = createDockablePanel("Properties", mainWindow.inspectorContainerPanel, true);

    this.resourcesDockable = createDockable( "Resources", Color.RED );
    this.objectsDockable   = createDockable( "Objects", Color.RED );
    CGrid grid = new CGrid( control );

    grid.add( 11, 1, 1, 2, mapTreeDockable);
    grid.add( 11, 0, 1, 2, terrainToolsDockable );

    grid.add( 0, 0, 2, 1, resourcesDockable );
    grid.add( 0, 1, 2, 1, objectsDockable );
    grid.add( 0, 2, 2, 1, inspectorDockable);

    grid.add( 2, 0, 9, 3, mapEditorDockable);
    control.getContentArea().deploy( grid );

    this.menu = new RootMenuPiece( "Panels", false );
    menu.add( new SingleCDockableListMenuPiece( control ));
  }

  public DefaultSingleCDockable createDockablePanel( String title, Component panel, boolean closeable) {
    DefaultSingleCDockable dockable = new DefaultSingleCDockable( title, title, panel );
    dockable.setCloseable( closeable );
    return dockable;
  }

  public DefaultSingleCDockable createDockable( String title, Color color ) {
    JPanel panel = new JPanel();
    panel.setOpaque( false );
    panel.setBackground( color );
    DefaultSingleCDockable dockable = new DefaultSingleCDockable( title, title, panel );
    dockable.setCloseable( true );
    return dockable;
  }

  @Override
  public void onEditorModeChange(MainToolbarController.EditorMode editorMode) {
    boolean objectsDock = false;
    boolean terrainDock = false;

    switch (editorMode) {
      case Terrain:
        objectsDock = false;
        terrainDock = true;
        break;
      case Objects:
        objectsDock = true;
        terrainDock = false;
        break;
      case None:
        objectsDock = false;
        terrainDock = false;
        break;
      default: throw new GdxRuntimeException("No support for: " + editorMode);
    }

    terrainToolsDockable.setVisible(terrainDock);
    inspectorDockable.setVisible(objectsDock);
    objectsDockable.setVisible(objectsDock);
    resourcesDockable.setVisible(objectsDock);
  }
}
