package macbury.forge.graphics.mesh;

import macbury.forge.graphics.builders.VoxelDef;

/**
 * Created by macbury on 16.10.14.
 */
public class VoxelsAssembler extends MeshAssembler {
  public void top(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert1.normalUp().color(voxelDef.material).ao(voxelDef.ao);

    if (voxelDef.shadeTopLeftCorner) {
      vert1.applyAoShade();
    }

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y  + voxelDef.size.y , voxelDef.position.z + voxelDef.size.z); // bottom left
    vert2.normalUp().color(voxelDef.material).ao(voxelDef.ao);
    if (voxelDef.shadeBottomLeftCorner) {
      vert2.applyAoShade();
    }

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y  + voxelDef.size.y, voxelDef.position.z); //top right
    vert3.normalUp().color(voxelDef.material).ao(voxelDef.ao);

    if (voxelDef.shadeTopRightCorner) {
      vert3.applyAoShade();
    }

    triangle(vert1, vert2, vert3);

    vert1 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y  + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //bottom right
    vert1.normalUp().color(voxelDef.material).ao(voxelDef.ao);

    if (voxelDef.shadeBottomRightCorner) {
      vert1.applyAoShade();
    }

    triangle(vert3, vert2, vert1);
  }

  public void bottom(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //top left
    vert1.normalBottom().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y , voxelDef.position.z + voxelDef.size.z); // bottom left
    vert2.normalBottom().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); //top right
    vert3.normalBottom().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert3, vert2, vert1);

    vert1 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); //bottom right
    vert1.normalBottom().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);
  }

  public void front(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); //bottom left
    vert1.normalFront().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalFront().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalFront().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top left
    vert4.normalFront().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert4, vert1, vert3);
  }

  public void back(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalBack().color(voxelDef.material).ao(voxelDef.ao);

    if (voxelDef.shadeBottomLeftCorner) {
      vert1.applyAoShade();
    }

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); // bottom right
    vert2.normalBack().color(voxelDef.material).ao(voxelDef.ao);

    if (voxelDef.shadeBottomRightCorner) {
      vert2.applyAoShade();
    }

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x + voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top right
    vert3.normalBack().color(voxelDef.material).ao(voxelDef.ao);

    if (voxelDef.shadeTopRightCorner) {
      vert3.applyAoShade();
    }

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalBack().color(voxelDef.material).ao(voxelDef.ao);
    if (voxelDef.shadeTopLeftCorner) {
      vert3.applyAoShade();
    }

    triangle(vert3, vert1, vert4);
  }

  public void left(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalLeft().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalLeft().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalLeft().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert1, vert2, vert3);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalLeft().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert4, vert1, vert3);
  }

  public void right(VoxelDef voxelDef) {
    MeshVertexInfo vert1 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y, voxelDef.position.z); //bottom left
    vert1.normalRight().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert2 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y, voxelDef.position.z + voxelDef.size.z); // bottom right
    vert2.normalRight().color(voxelDef.material).ao(voxelDef.ao);

    MeshVertexInfo vert3 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z + voxelDef.size.z); //top right
    vert3.normalRight().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert3, vert2, vert1);

    MeshVertexInfo vert4 = this.vertex(voxelDef.position.x+voxelDef.size.x, voxelDef.position.y + voxelDef.size.y, voxelDef.position.z); //top left
    vert4.normalRight().color(voxelDef.material).ao(voxelDef.ao);

    triangle(vert3, vert1, vert4);
  }
}
