package macbury.forge.editor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.blocks.AirBlock;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlocksProvider;
import macbury.forge.editor.controllers.blocks.BlockPreviews;
import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.BuildBlocksTexture;
import macbury.forge.editor.reloader.DirectoryWatchJob;
import macbury.forge.editor.reloader.DirectoryWatcher;
import macbury.forge.editor.screens.LevelEditorScreen;
import macbury.forge.editor.views.BlockListRenderer;
import macbury.forge.editor.views.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by macbury on 11.11.14.
 */
public class BlocksController implements OnMapChangeListener, DirectoryWatchJob.DirectoryWatchJobListener, ForgEBootListener, ListSelectionListener, BlockPreviews.Listener {
  private static final int TILE_SIZE = 32;
  private static final String TAG = "BlocksController";
  private final BlockListRenderer blockListRenderer;
  private final JList blockList;
  private final JobManager jobs;
  private final ImagePanel panelPrimaryBlock;
  private final ImagePanel panelSecondaryBlock;
  private DefaultListModel blocksModel;
  private int currentPrimarySelectedBlock = 0;
  private int currentSecondarySelectedBlock = 0;
  private ProjectController controller;

  public BlocksController(JList blockList, DirectoryWatcher directoryWatcher, JobManager jobs, ImagePanel panelPrimaryBlock, ImagePanel panelSecondaryBlock) {
    this.panelPrimaryBlock   = panelPrimaryBlock;
    this.panelSecondaryBlock = panelSecondaryBlock;
    this.blockList           = blockList;
    this.blockListRenderer   = new BlockListRenderer();
    this.jobs                = jobs;

    blockList.setEnabled(false);
    blockList.setFixedCellWidth(TILE_SIZE);
    blockList.setFixedCellHeight(TILE_SIZE);
    blockList.setValueIsAdjusting(true);
    blockList.setVisibleRowCount(0);
    blockList.setCellRenderer(blockListRenderer);
    blockList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    blockList.setBorder(new EmptyBorder(1, 1, 1, 1));

    directoryWatcher.addListener(BlocksProvider.BLOCKS_PATH, this);
    directoryWatcher.addListener(BlocksProvider.BLOCKS_SHAPE_PATH, this);
    blockList.addListSelectionListener(this);
    blockList.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if ( SwingUtilities.isRightMouseButton(e) ) {
          JList list = (JList)e.getSource();
          int row = list.locationToIndex(e.getPoint());
          setSecondarySelectedIndex(row);
        }
      }
    });
  }

  private void setSecondarySelectedIndex(int row) {
    currentSecondarySelectedBlock = row;
    if (currentSecondarySelectedBlock != -1) {
      BlockListItem item = (BlockListItem) blocksModel.get(currentSecondarySelectedBlock);
      panelSecondaryBlock.setIcon(item.image);
    }

  }

  private void reload() {
    this.blocksModel = new DefaultListModel();

    for (Block block : ForgE.blocks.list()) {
      if (!AirBlock.class.isInstance(block)) {
        BlockListItem item = new BlockListItem();
        item.block         = block;

        item.loadIcon(BlockPreviews.blockPreview(block).path());
        blocksModel.addElement(item);
      }
    }

    blockList.setModel(blocksModel);
  }

  public Block getCurrentPrimaryBlock() {
    BlockListItem item = (BlockListItem) blocksModel.get(currentPrimarySelectedBlock);
    return item.block;
  }

  public Block getCurrentSecondaryBlock() {
    BlockListItem item = (BlockListItem) blocksModel.get(currentSecondarySelectedBlock);
    return item.block;
  }

  @Override
  public void onCloseMap(ProjectController controller, LevelEditorScreen screen) {
    resetSelectedItem();
    blockList.setEnabled(false);
  }

  @Override
  public void onNewMap(ProjectController controller, LevelEditorScreen screen) {
    this.controller = controller;
    blockList.setEnabled(true);
    reload();
    resetSelectedItem();
  }

  @Override
  public void onProjectStructureChange(ProjectController controller) {
    reload();
  }

  @Override
  public void onMapSaved(ProjectController projectController, LevelEditorScreen levelEditorScreen) {

  }

  private void resetSelectedItem() {
    blockList.setSelectedIndex(0);
    setSecondarySelectedIndex(1);
  }

  @Override
  public void onFileInDirectoryChange(FileHandle handle) {
    if (handle.extension().contains(BlocksProvider.BLOCK_EXT) || handle.extension().contains(BlocksProvider.SHAPE_EXT)) {
      ForgE.log(TAG, "Change in: " + handle.name());
      rebuildTileset();
    }
  }

  public void rebuildTileset() {
    BuildBlocksTexture job = new BuildBlocksTexture();
    job.setCallback(this, "onRebuildTextures");
    jobs.enqueue(job);
  }

  public void onRebuildTextures(Boolean success, BuildBlocksTexture buildBlocksTexture) {
    ForgE.log(TAG, "Rebuilded all textures");
    if (controller != null) {
      controller.rebuildChunks();
      controller.clearUndoRedo();
    }

    rebuildPreviews(true);

  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    if (!ForgE.blocks.getTextureAtlasFile().exists()) {
      rebuildTileset();
    } else {
      rebuildPreviews(false);
    }

  }

  private void rebuildPreviews(boolean removeOld) {
    BlockPreviews bp = new BlockPreviews(this, removeOld);
  }


  @Override
  public void valueChanged(ListSelectionEvent e) {
    currentPrimarySelectedBlock = blockList.getSelectedIndex();
    if (currentPrimarySelectedBlock != -1) {
      BlockListItem item = (BlockListItem) blocksModel.get(currentPrimarySelectedBlock);
      panelPrimaryBlock.setIcon(item.image);
    }
  }

  @Override
  public void onGeneratePreviewsCompleted() {
    reload();
    resetSelectedItem();
  }

  public class BlockListItem {
    public Block block;
    public Icon icon;
    public Image image;

    public void loadIcon(String iconPath) {
      BufferedImage img = null;
      try {
        img = ImageIO.read(new File(iconPath));
        this.image = img.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_FAST);

        this.icon  = new ImageIcon(image);
      } catch (IOException e) {
        e.printStackTrace();
      }


    }
  }


}
