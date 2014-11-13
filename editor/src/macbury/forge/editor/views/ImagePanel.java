package macbury.forge.editor.views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by macbury on 13.11.14.
 */
public class ImagePanel extends JPanel {

  private Image icon;

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (icon != null) {
      Graphics2D g2d = (Graphics2D) g;
      int x = (this.getWidth() - icon.getWidth(null)) / 2;
      int y = (this.getHeight() - icon.getHeight(null)) / 2;
      g2d.drawImage(icon, x, y, null);
    }
  }

  public Image getIcon() {
    return icon;
  }

  public void setIcon(Image icon) {
    this.icon = icon;
    repaint();
  }
}
