package macbury.forge.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 18.11.14.
 */
public class BlockShapePart {
  public Array<Vector3> verticies            = new Array<Vector3>();
  public Array<Vector3> normals              = new Array<Vector3>();
  public Array<Vector2> uvs                  = new Array<Vector2>();
  public Array<BlockShapeTriangle> triangles = new Array<BlockShapeTriangle>();
  public float[]                   waviness  = null;
}
