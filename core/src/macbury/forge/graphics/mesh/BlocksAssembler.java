package macbury.forge.graphics.mesh;

/**
 * Created by macbury on 16.10.14.
 */
public class BlocksAssembler extends MeshAssembler {

  public void topFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2) {
    MeshVertexInfo vert1 = this.vertex(x, y, z); //top left
    vert1.uv(u, v2);
    vert1.normal(0,1,0);

    MeshVertexInfo vert2 = this.vertex(x, y, z + height); // bottom left
    vert2.uv(u, v);
    vert2.normal(0,1,0);

    MeshVertexInfo vert3 = this.vertex(x + width, y, z); //top right
    vert3.uv(u2,v2);
    vert3.normal(0,1,0);

    triangle(vert1, vert2, vert3);

    vert1 = this.vertex(x + width, y, z + height); //bottom right
    vert1.normal(0,1,0);
    vert1.uv(u2,v);

    triangle(vert3, vert2, vert1);
  }

}
