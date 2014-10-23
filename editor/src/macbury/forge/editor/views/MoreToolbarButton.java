package macbury.forge.editor.views;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by macbury on 23.10.14.
 */
public class MoreToolbarButton extends JToggleButton implements PopupMenuListener, ItemListener {

  private final JPopupMenu menu;

  public MoreToolbarButton(JPopupMenu menu) {
    super();
    this.menu      = menu;
    ImageIcon icon = new ImageIcon(getClass().getResource("/icons/nav_icon.png"));
    setFocusable(false);
    setHorizontalTextPosition(SwingConstants.LEADING);
    setIcon(icon);
    menu.addPopupMenuListener(this);
    addItemListener(this);
    menu.setMinimumSize(new Dimension(320, 24));
  }

  @Override
  public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    setSelected(true);
  }

  @Override
  public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    setSelected(false);
  }

  @Override
  public void popupMenuCanceled(PopupMenuEvent e) {
    setSelected(false);
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    menu.show(this, 0, this.getHeight());
  }
}
