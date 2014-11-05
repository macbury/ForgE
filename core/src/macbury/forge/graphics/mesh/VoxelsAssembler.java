package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.graphics.builders.VoxelDef;

/**
 * Created by macbury on 16.10.14.
 */
public class VoxelsAssembler extends MeshAssembler {
  public void top(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert1.normalUp().color(voxelDef.material).ao(voxelDef.ao);
    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y  + voxelDef.size.y , voxelDef.position.z + voxelDef.size.z); // bottom left
    vert2.normalUp().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y  + voxelDef.size.y, voxelDef.position.z); //top right
    vert3.normalUp().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);

    vert1 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y  + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //bottom right
    vert1.normalUp().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert3, vert2, vert1);
  }

  public void bottom(Vector3 position, Vector3 size, Color color) {
    MeshVertexInfo vert1 = this.vertex(position.x, position.y, position.z); //top left
    vert1.normalBottom().color(color);

    MeshVertexInfo vert2 = this.vertex(position.x, position.y , position.z + size.z); // bottom left
    vert2.normalBottom().color(color);

    MeshVertexInfo vert3 = this.vertex(position.x + size.x, position.y, position.z); //top right
    vert3.normalBottom().color(color);

    triangle(vert3, vert2, vert1);

    vert1 = this.vertex(position.x + size.x, position.y, position.z + size.z); //bottom right
    vert1.normalBottom().color(color);

    triangle(vert1, vert2, vert3);
  }

  public void front(Vector3 position, Vector3 size, Color color) {
    MeshVertexInfo vert1 = this.vertex(position.x, position.y, position.z + size.z); //bottom left
    vert1.normalFront().color(color);

    MeshVertexInfo vert2 = this.vertex(position.x + size.x, position.y, position.z + size.z); // bottom right
    vert2.normalFront().color(color);

    MeshVertexInfo vert3 = this.vertex(position.x + size.x, position.y + size.y, position.z + size.z); //top right
    vert3.normalFront().color(color);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(position.x, position.y + size.y, position.z + size.z); //top left
    vert4.normalFront().color(color);

    triangle(vert4, vert1, vert3);
  }

  public void back(Vector3 position, Vector3 size, Color color, float ao) {
    MeshVertexInfo vert1 = this.vertex(position.x, position.y, position.z); //bottom left
    vert1.normalBack().color(color).ao(ao).baseAo();

    MeshVertexInfo vert2 = this.vertex(position.x + size.x, position.y, position.z); // bottom right
    vert2.normalBack().color(color).ao(ao).baseAo();

    MeshVertexInfo vert3 = this.vertex(position.x + size.x, position.y + size.y, position.z); //top right
    vert3.normalBack().color(color).ao(ao);

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(position.x, position.y + size.y, position.z); //top left
    vert4.normalBack().color(color).ao(ao);

    triangle(vert3, vert1, vert4);
  }

  public void left(Vector3 position, Vector3 size, Color color) {
    MeshVertexInfo vert1 = this.vertex(position.x, position.y, position.z); //bottom left
    vert1.normalLeft().color(color);

    MeshVertexInfo vert2 = this.vertex(position.x, position.y, position.z + size.z); // bottom right
    vert2.normalLeft().color(color);

    MeshVertexInfo vert3 = this.vertex(position.x, position.y + size.y, position.z + size.z); //top right
    vert3.normalLeft().color(color);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(position.x, position.y + size.y, position.z); //top left
    vert4.normalLeft().color(color);

    triangle(vert4, vert1, vert3);
  }

  public void right(Vector3 position, Vector3 size, Color color) {
    MeshVertexInfo vert1 = this.vertex(position.x+size.x, position.y, position.z); //bottom left
    vert1.normalRight().color(color);

    MeshVertexInfo vert2 = this.vertex(position.x+size.x, position.y, position.z + size.z); // bottom right
    vert2.normalRight().color(color);

    MeshVertexInfo vert3 = this.vertex(position.x+size.x, position.y + size.y, position.z + size.z); //top right
    vert3.normalRight().color(color);

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(position.x+size.x, position.y + size.y, position.z); //top left
    vert4.normalRight().color(color);

    triangle(vert3, vert1, vert4);
  }
}
