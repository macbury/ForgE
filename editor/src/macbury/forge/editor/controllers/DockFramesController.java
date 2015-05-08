package macbury.forge.editor.controllers;

import bibliothek.gui.dock.common.*;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.windows.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by macbury on 07.05.15.
 */
public class DockFramesController {
  private final CControl control;
  public final RootMenuPiece menu;
  private final DefaultSingleCDockable mapEditorDockable;
  private final DefaultSingleCDockable terrainToolsDockable;
  private final DefaultSingleCDockable inspectorDockable;

  public DockFramesController(MainWindow mainWindow) {
    control = new CControl( mainWindow );
    control.setTheme(ThemeMap.KEY_ECLIPSE_THEME);
    mainWindow.mainContentPane.add(control.getContentArea(), BorderLayout.CENTER);

    this.mapEditorDockable = new DefaultSingleCDockable( "Map", "Map", mainWindow.openGlContainer );
    mapEditorDockable.setExtendedMode(ExtendedMode.NORMALIZED);
    mapEditorDockable.setSticky(false);
    mapEditorDockable.setCloseable(false);
    mapEditorDockable.setMaximizable(false);
    mapEditorDockable.setMinimizable(false);
    mapEditorDockable.setExternalizable(false);

    this.terrainToolsDockable   = createDockablePanel("Terrain", mainWindow.terrainPanel, true);
    terrainToolsDockable.setLocation( CLocation.base().minimalSouth() );

    DefaultSingleCDockable mapTreeDockable   = createDockablePanel("Maps", mainWindow.mapTreeScroll, true);
    this.inspectorDockable = createDockablePanel("Properties", mainWindow.inspectorContainerPanel, true);
    
    CGrid grid = new CGrid( control );

    grid.add( 11, 1, 1, 2, mapTreeDockable);
    grid.add( 11, 0, 1, 2, terrainToolsDockable );

    grid.add( 0, 0, 2, 1, createDockable( "Resources", Color.RED ) );
    grid.add( 0, 1, 2, 1, createDockable( "Objects", Color.RED ) );
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

}
