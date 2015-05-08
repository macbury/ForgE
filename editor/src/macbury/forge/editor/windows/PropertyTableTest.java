package macbury.forge.editor.windows;


import bibliothek.gui.dock.common.*;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import macbury.forge.desktop.SwingThemeHelper;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import javax.swing.*;

public class PropertyTableTest extends JFrame {

  public static void main( String[] args ){
    SwingThemeHelper.useGTK();
    JFrame frame = new JFrame( "Demo" );
    CControl control = new CControl( frame );
    control.setTheme(ThemeMap.KEY_ECLIPSE_THEME);
    control.handleResizeRequests();
    frame.add( new JPanel().add(control.getContentArea()) );

    CGrid grid = new CGrid( control );
    grid.add( 0, 1, 1, 3, createDockable( "Maps", Color.RED ) );
    grid.add( 0, 0, 1, 3, createDockable( "Terrain", Color.RED ) );
    grid.add( 0, 0, 1, 3, createDockable( "Objects", Color.RED ) );
    grid.add( 0, 1, 1, 3, createDockable( "Resources", Color.RED ) );
    grid.add( 0, 2, 2, 4, createDockable( "Properties", Color.GREEN ) );
    SingleCDockable mapDockable = createDockable("Map", Color.BLUE);
    mapDockable.setExtendedMode(ExtendedMode.MAXIMIZED);
    grid.add( 1, 0, 7, 10, mapDockable);
    control.getContentArea().deploy( grid );

    SingleCDockable black = createDockable( "Logs", Color.BLACK );
    control.addDockable( black );
    black.setLocation( CLocation.base().minimalSouth() );
    black.setVisible( true );


    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setBounds( 20, 20, 800, 600 );
    frame.setVisible( true );
    frame.setResizable(true);

    RootMenuPiece menu = new RootMenuPiece( "Windows", false );
    menu.add( new SingleCDockableListMenuPiece( control ));
    JMenuBar menuBar = new JMenuBar();
    menuBar.add( menu.getMenu() );
    frame.setJMenuBar( menuBar );
  }

  public static SingleCDockable createDockable( String title, Color color ) {
    JPanel panel = new JPanel();
    panel.setOpaque( false );
    panel.setBackground( color );
    DefaultSingleCDockable dockable = new DefaultSingleCDockable( title, title, panel );
    dockable.setCloseable( true );
    return dockable;
  }
}
