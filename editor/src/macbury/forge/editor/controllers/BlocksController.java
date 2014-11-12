package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.blocks.AirBlock;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.BuildBlocksTexture;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
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
public class BlocksController implements OnMapChangeListener, DirectoryWatchJob.DirectoryWatchJobListener, ForgEBootListener {
  private static final int TILE_SIZE = 32;
  private static final String TAG = "BlocksController";
  private final BlockListRenderer blockListRenderer;
  private final JList blockList;
  private final JobManager jobs;
  private DefaultListModel blocksModel;
  private int currentSelectedBlock = 0;
  private ProjectController controller;

  public BlocksController(JList blockList, DirectoryWatcher directoryWatcher, JobManager jobs) {
    this.blockList         = blockList;
    this.blockListRenderer = new BlockListRenderer();
    this.jobs              = jobs;
    blockList.setFixedCellWidth(TILE_SIZE);
    blockList.setFixedCellHeight(TILE_SIZE);
    blockList.setValueIsAdjusting(true);
    blockList.setVisibleRowCount(0);
    blockList.setCellRenderer(blockListRenderer);
    blockList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    blockList.setBorder(new EmptyBorder(1, 1, 1, 1));

    directoryWatcher.addListener(BlocksProvider.BLOCKS_PATH, this);
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
    this.controller = controller;
    reload();
  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    if (handle.extension().equalsIgnoreCase(BlocksProvider.BLOCK_EXT)) {
      Gdx.app.log(TAG, "Change in: " + handle.name());
      rebuildTileset();
    }
  }

  private void rebuildTileset() {
    BuildBlocksTexture job = new BuildBlocksTexture();
    job.setCallback(this, "onRebuildTextures");
    jobs.enqueue(job);
  }

  public void onRebuildTextures(Boolean success, BuildBlocksTexture buildBlocksTexture) {
    reload();
    if (controller != null)
      controller.rebuildChunks();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    if (!ForgE.blocks.getTextureAtlasFile().exists()) {
      rebuildTileset();
    }
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
