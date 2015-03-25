package macbury.forge.editor;

import com.ezware.dialog.task.CommandLink;
import com.ezware.dialog.task.TaskDialogs;
import macbury.forge.editor.windows.MainWindow;

import javax.swing.*;
import java.math.BigDecimal;

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

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainWindow mainWindow = new MainWindow();
      }
    });
  }
}
