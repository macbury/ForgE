package macbury.forge.editor.windows;

import javax.swing.*;

public class ProgressTaskDialog extends JFrame {
  private JPanel contentPane;
  private JProgressBar progressBar1;
  public ProgressTaskDialog() {
    setContentPane(contentPane);
    setTitle("Working...");
    pack();
    setAlwaysOnTop(true);
  }
}
