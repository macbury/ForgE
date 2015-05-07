package macbury.forge.desktop;

import javax.swing.*;

/**
 * Created by macbury on 07.05.15.
 */
public class SwingThemeHelper {
  public static void useGTK() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run () {
        Runtime.getRuntime().halt(0); // Because fuck you, Swing shutdown hooks.
      }
    });
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }

      // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }
}
