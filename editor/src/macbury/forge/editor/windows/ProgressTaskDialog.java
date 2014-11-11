package macbury.forge.editor.windows;

import javax.swing.*;

public class ProgressTaskDialog extends JWindow {
  private JPanel contentPane;
  private JProgressBar progressBar1;
  public ProgressTaskDialog() {
    setContentPane(contentPane);
    setAlwaysOnTop(true);
    pack();
  }

  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    progressBar1.setEnabled(true);
    progressBar1.setIndeterminate(true);
  }
}
