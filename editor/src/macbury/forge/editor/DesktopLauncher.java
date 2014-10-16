package macbury.forge.editor;

import javax.swing.*;

/**
 * Created by macbury on 15.10.14.
 */
public class DesktopLauncher {
  public static void main (String[] arg) {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run () {
      Runtime.getRuntime().halt(0); // Because fuck you, Swing shutdown hooks.
      }
    });
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainWindow mainWindow = new MainWindow();
      }
    });
  }
}
