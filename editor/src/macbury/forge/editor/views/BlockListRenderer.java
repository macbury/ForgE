package macbury.forge.editor.views;

import macbury.forge.editor.controllers.BlocksController;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by macbury on 11.11.14.
 */
public class BlockListRenderer extends DefaultListCellRenderer {

  private static final int BORDER_SIZE = 1;

  public BlockListRenderer() {

  }

  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    BlocksController.BlockListItem item  = (BlocksController.BlockListItem) value;
    label.setText("");
    label.setToolTipText(item.block.name);
    label.setIcon(item.icon);

    if (isSelected || cellHasFocus) {
      label.setBorder(new BevelBorder(BORDER_SIZE, Color.black, Color.black));
    } else {
      label.setBorder(new EmptyBorder(BORDER_SIZE,BORDER_SIZE,BORDER_SIZE,BORDER_SIZE));
    }


    return label;
  }

}
