package macbury.forge.graphics.batch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.mesh.MeshTriangle;

import java.util.ArrayList;

/**
 * Created by macbury on 18.10.14.
 */
public class Wireframe {
  private final int size;
  private Vector3[] points;

  public Wireframe(ArrayList<MeshTriangle> triangleArrayList) {
    this.size   = triangleArrayList.size() * 3;
    this.points = new Vector3[size];
    for (int i = 0; i < size; i += 3) {
      MeshTriangle triangle = triangleArrayList.get(i/3);
      points[i]             = triangle.vert1.position.cpy();
      points[i+1]           = triangle.vert2.position.cpy();
      points[i+2]           = triangle.vert3.position.cpy();
    }
  }

  public void render(ShapeRenderer lineRenderer, Color color) {
    lineRenderer.setColor(color);
    int size = points.length;
    for(int i = 0; i < size; i+=3) {
      lineRenderer.line(points[i], points[i+1]);
      lineRenderer.line(points[i+1], points[i+2]);
      lineRenderer.line(points[i+2], points[i]);
    }
  }
}
