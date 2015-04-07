package macbury.forge.voxel;

import macbury.forge.ForgE;
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

  /**
   * Totaly need to change this, Shit storm ahead
   * @return
   */
  public Block getBlock() {
    return ForgE.blocks.find(blockId);
  }

  public Voxel() {

  }

  public Voxel(Voxel otherVoxel) {
    this.blockId = otherVoxel.blockId;
    this.alginTo = otherVoxel.alginTo;
  }

  public boolean isAir() {
    return getBlock().isAir();
  }

  @Override
  public boolean equals(Object obj) {
    Voxel b = (Voxel)obj;
    return blockId == b.blockId;
  }

  public boolean isScalable() {
    return getBlock().blockShape.scalable;
  }
}
