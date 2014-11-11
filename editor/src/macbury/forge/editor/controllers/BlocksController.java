package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import macbury.forge.ForgE;
import macbury.forge.blocks.AirBlock;
import macbury.forge.blocks.Block;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.editor.views.BlockListRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by macbury on 11.11.14.
 */
public class BlocksController implements OnMapChangeListener {
  private static final int TILE_SIZE = 32;
  private final BlockListRenderer blockListRenderer;
  private final JList blockList;
  private DefaultListModel blocksModel;
  private int currentSelectedBlock = 0;

  public BlocksController(JList blockList) {
    this.blockList         = blockList;
    this.blockListRenderer = new BlockListRenderer();
    blockList.setFixedCellWidth(TILE_SIZE);
    blockList.setFixedCellHeight(TILE_SIZE);
    blockList.setValueIsAdjusting(true);
    blockList.setVisibleRowCount(0);
    blockList.setCellRenderer(blockListRenderer);
    blockList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    blockList.setBorder(new EmptyBorder(1,1, 1, 1));
  }

  private void reload() {
    this.blocksModel = new DefaultListModel();

    for (Block block : ForgE.blocks.list()) {
      if (!AirBlock.class.isInstance(block)) {
        BlockListItem item = new BlockListItem();
        item.block         = block;

        item.loadIcon(Gdx.files.internal("textures/blocks/"+block.getIconName() + ".png").path());
        blocksModel.addElement(item);
      }
    }

    blockList.setModel(blocksModel);
    blockList.setSelectedIndex(currentSelectedBlock);
  }

  @Override
  public void onCloseMap(ProjectController controller, EditorScreen screen) {

  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    reload();
  }

  public class BlockListItem {
    public Block block;
    public Icon icon;
    public void loadIcon(String iconPath) {
      BufferedImage img = null;
      try {
        img = ImageIO.read(new File(iconPath));
      } catch (IOException e) {
        e.printStackTrace();
      }

      Image dimg = img.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_FAST);

      this.icon  = new ImageIcon(dimg);
    }
  }
}
