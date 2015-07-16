package macbury.forge.shaders.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by macbury on 16.07.15.
 */
public class UniformDefinition {
  private final String name;
  private Class type;
  private String customType;

  public UniformDefinition(String uniformName, Class uniformType) {
    this.name = uniformName;
    this.type = uniformType;
  }

  public UniformDefinition(String uniformName, String uniformTypeString) {
    this.name = uniformName;
    this.customType = uniformTypeString;
  }

  private String getTypeName() {
    if (customType != null) {
      return customType;
    } else if (type == Matrix4.class) {
      return "mat4";
    } else if (type == Float.class) {
      return "float";
    } else if (type == Matrix3.class) {
      return "mat3";
    } else if (type == Vector2.class) {
      return "vec2";
    } else if (type == Vector3.class || type == Color.class) {
      return "vec4";
    } else if (type == Texture.class) {
      return "sampler2D";
    }

    throw new GdxRuntimeException("Undefined type: " + type + " for " + name);
  }

  @Override
  public String toString() {
    return "uniform " + getTypeName() + " " + name + ";";
  }
}
