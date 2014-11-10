package macbury.forge.editor.windows;

import javax.swing.*;

public class ProgressTaskDialog extends JWindow {
  private JPanel contentPane;
  private JProgressBar progressBar1;
  public ProgressTaskDialog() {
    setContentPane(contentPane);
    pack();
    setAlwaysOnTop(true);
  }

  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    progressBar1.setEnabled(true);
    progressBar1.setIndeterminate(true);
  }
}
