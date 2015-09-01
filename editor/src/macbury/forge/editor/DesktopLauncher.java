package macbury.forge.editor;

import com.badlogic.gdx.math.Vector3;
import com.ezware.dialog.task.CommandLink;
import com.ezware.dialog.task.TaskDialogs;
import macbury.forge.desktop.SwingThemeHelper;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.utils.Vector3i;
import org.jruby.embed.ScriptingContainer;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Created by macbury on 15.10.14.
 */
public class DesktopLauncher {
  public static void main (String[] arg) {
    SwingThemeHelper.useGTK();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainWindow mainWindow = new MainWindow();
      }
    });


  }
}
