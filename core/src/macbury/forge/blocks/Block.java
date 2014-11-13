package macbury.forge.blocks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

/**
 * Created by macbury on 11.11.14.
 */
public class Block {

  public enum Side {
    all, top, left, right, back, front, bottom, side
  }
  /**
   * Unique block id
   */
  public byte id = 0;
  /**
   * Debug name
   */
  public String name = "";
  /**
   * Is block solid and collidable
   */
  public boolean solid = false;
  /**
   * Should apply ao shading
   */
  public boolean shadeAO = false;
  /**
   * Should apply perlin ao
   */
  public boolean envAO = false;
  /**
   * Should render transparent with alpha support
   */
  public boolean transparent = false;
  /**
   * Should render backFace
   */
  public boolean backFace = false;
  /**
   * Textures names for block
   */
  private HashMap<String, String> textures;

  private HashMap<Side, TextureAtlas.AtlasRegion> uvs;

  public void createUVMapping(TextureAtlas textureAtlas) {
    uvs = new HashMap<Side, TextureAtlas.AtlasRegion>();
    for (String side : textures.keySet()) {
      String sideName                 = textures.get(side);
      TextureAtlas.AtlasRegion region = textureAtlas.findRegion(sideName);

      Side currentSide = Side.valueOf(side);
      switch (currentSide) {
        case all:
          uvs.put(Side.front, region);
          uvs.put(Side.top, region);
          uvs.put(Side.bottom, region);
          uvs.put(Side.back, region);
          uvs.put(Side.left, region);
          uvs.put(Side.right, region);
        break;

        case side:
          uvs.put(Side.front, region);
          uvs.put(Side.back, region);
          uvs.put(Side.left, region);
          uvs.put(Side.right, region);
        break;
      }

      uvs.put(currentSide, region);
    }
  }

  public String getIconName() {
    if (textures.containsKey(Side.all.toString())) {
      return textures.get(Side.all.toString());
    } else if (textures.containsKey(Side.top.toString())) {
      return textures.get(Side.top.toString());
    } else if (textures.containsKey(Side.side.toString())) {
      return textures.get(Side.side.toString());
    }
    throw new GdxRuntimeException("No default side found!");
  }

  public boolean isAir() {
    return AirBlock.class.isInstance(this);
  }

  public TextureAtlas.AtlasRegion getRegionForSide(Side side) {
    return uvs.get(side);
  }

  @Override
  public String toString() {
    return "Block " + name + " with id " + id;
  }
}
