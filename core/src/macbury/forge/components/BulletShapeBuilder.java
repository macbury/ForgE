package macbury.forge.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
* Created by macbury on 06.05.15.
*/
public class BulletShapeBuilder {

  public enum ShapeType {
    Box, Capsule
  }

  public ShapeType type;
  public Vector3 size;

  public boolean isValid() {
    return size != null && type != null;
  }

  public btCollisionShape build() {
    if (isValid()) {
      switch (type) {
        case Box:
          return createBox();
        case Capsule:
          return createCapsule();

        default:
          throw new GdxRuntimeException("Undefined shape type");

      }
    } else {
      throw new GdxRuntimeException("Invalid shape config");
    }
  }

  private btCollisionShape createCapsule() {
    return new btCapsuleShape(size.x, size.y);
  }

  private btCollisionShape createBox() {
    return new btBoxShape(size);
  }

}
