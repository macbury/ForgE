package macbury.forge.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

/**
 * Created by macbury on 18.11.14.
 */
public class BlockShape implements Json.Serializable {
  private static final String KEY_PARTS     = "parts";
  private static final String KEY_VERTICIES = "verticies";
  private static final String KEY_NORMALS   = "normals";
  private static final String KEY_UVS       = "uvs";
  private static final String KEY_TRIANGLES = "triangles";
  public String name;
  private HashMap<Block.Side, BlockShapePart> parts;

  @Override
  public void write(Json json) {
    throw new GdxRuntimeException("You can only load this file for now :P");
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
    this.parts = new HashMap<Block.Side, BlockShapePart>();

    JsonValue partsValue = jsonData.get(KEY_PARTS);

    for(JsonValue partValue : partsValue) {
      try {
        Block.Side side     = Block.Side.valueOf(partValue.name());
        BlockShapePart part = new BlockShapePart();

        if (partValue.has(KEY_VERTICIES)) {
          for(JsonValue vertex : partValue.get(KEY_VERTICIES)) {
            float vertexArr[] = vertex.asFloatArray();
            if (vertexArr.length != 3) {
              throw new GdxRuntimeException("Vertex should have 3 values!!!");
            }
            part.verticies.add(new Vector3(vertexArr));
          }
        }

        if (partValue.has(KEY_NORMALS)) {
          for(JsonValue normalValue : partValue.get(KEY_NORMALS)) {
            float normal[] = normalValue.asFloatArray();
            if (normal.length != 3) {
              throw new GdxRuntimeException("Normal should have 3 values!!!");
            }
            part.normals.add(new Vector3(normal));
          }
        }

        if (partValue.has(KEY_UVS)) {
          for(JsonValue uvsValue : partValue.get(KEY_UVS)) {
            float uvs[] = uvsValue.asFloatArray();
            if (uvs.length != 2) {
              throw new GdxRuntimeException("Normal should have 2 values!!!");
            }
            part.uvs.add(new Vector2(uvs[0], uvs[1]));
          }
        }

        if (partValue.has(KEY_TRIANGLES)) {
          for(JsonValue triangleValue : partValue.get(KEY_TRIANGLES)) {
            int triangles[] = triangleValue.asIntArray();
            if (triangles.length != 3) {
              throw new GdxRuntimeException("Triangle should have 3 values!!!");
            }
            part.triangles.add(new BlockShapeTriangle(triangles));
          }
        }

        parts.put(side, part);
      } catch (IllegalArgumentException e) {
        throw new GdxRuntimeException(e);
      }
    }
  }

  public BlockShapePart get(Block.Side side) {
    return parts.get(side);
  }
}
