package macbury.forge.utils;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;

/**
 * Created by macbury on 25.04.15.
 */
public class BulletUtils {
  public static btConvexHullShape createConvexHullShape(Mesh mesh, boolean optimize) {
    final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
    if (!optimize) return shape;
    // now optimize the shape
    final btShapeHull hull = new btShapeHull(shape);
    hull.buildHull(shape.getMargin());
    final btConvexHullShape result = new btConvexHullShape(hull);
    // delete the temporary shape
    shape.dispose();
    hull.dispose();
    return result;
  }
}
