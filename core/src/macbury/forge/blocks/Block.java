package macbury.forge.blocks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.utils.Vector3i;

import java.util.HashMap;

/**
 * Created by macbury on 11.11.14.
 */
public class Block {
  /*
  TODO: merge side with terrain builder.face :P
   */
  public enum Side {
    all(Vector3i.ZERO, new Quaternion(), new Quaternion()),
    top(Vector3i.TOP, new Quaternion(Vector3.Z, -180), new Quaternion()),
    left(Vector3i.LEFT,  new Quaternion(Vector3.Z, -90), new Quaternion(Vector3.Y, -90)),
    right(Vector3i.RIGHT, new Quaternion(Vector3.Z, 90), new Quaternion(Vector3.Y, 90)),
    back(Vector3i.BACK, new Quaternion(Vector3.X, 90), new Quaternion(Vector3.Y, 180)),
    front(Vector3i.FRONT, new Quaternion(Vector3.X, -90), new Quaternion()),
    bottom(Vector3i.BOTTOM, new Quaternion(), new Quaternion()),
    side(Vector3i.ZERO, new Quaternion(), new Quaternion());

    public final Vector3i direction;
    public final Quaternion rotationAllSides;
    public final Quaternion rotationHorizontal;

    Side(Vector3i direction, Quaternion rotationAllSides, Quaternion rotationHorizontal) {
      this.direction          = new Vector3i(direction);
      this.rotationAllSides   = new Quaternion(rotationAllSides);
      this.rotationHorizontal = new Quaternion(rotationHorizontal);
    }
  }

  public enum Rotation {
    none, horizontal, alignToSurface;

    public Side faceToSide(Side alginTo) {
      if (this == none || alginTo == Side.all) {
        return alginTo;
      } else {
        return Side.left;
      }
    }
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
   * Name of the BlockShape object
   */
  public String shape = "cube";
  public BlockShape blockShape;

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
   * How block should algint itself to other blocks
   */
  public Rotation rotation = Rotation.none;
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
