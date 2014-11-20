package macbury.forge.voxel;

import macbury.forge.blocks.Block;

/**
 * Created by macbury on 19.11.14.
 */
public class Voxel {
  /**
   * Block id for identification
   */
  public byte blockId;

  /**
   * Should terrain builder algin this block to specified side of other block;
   */
  public Block.Side alginTo;
}
