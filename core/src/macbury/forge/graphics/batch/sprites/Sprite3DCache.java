package macbury.forge.graphics.batch.sprites;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 27.05.14.
 */
public class Sprite3DCache implements Disposable {
  public Mesh mesh;
  public TextureRegion region;

  // 3(x,y,z) + 2(u,v)
  private static final int VERTEX_SIZE = 3 + 2;
  public static final int SIZE = 4 * VERTEX_SIZE;
  protected float[] vertices = new float[SIZE];

  public static final int X1 = 0;
  public static final int Y1 = 1;
  public static final int Z1 = 2;
  public static final int U1 = 3;
  public static final int V1 = 4;

  public static final int X2 = 5;
  public static final int Y2 = 6;
  public static final int Z2 = 7;
  public static final int U2 = 8;
  public static final int V2 = 9;

  public static final int X3 = 10;
  public static final int Y3 = 11;
  public static final int Z3 = 12;
  public static final int U3 = 13;
  public static final int V3 = 14;

  public static final int X4 = 15;
  public static final int Y4 = 16;
  public static final int Z4 = 17;
  public static final int U4 = 18;
  public static final int V4 = 19;

  public Sprite3DCache(TextureRegion region) {
    this.region   = region;

    updateVerticies();
    buildMesh();
  }

  private void buildMesh() {
    mesh = new Mesh(true, SIZE, 6, VertexAttribute.Position(), VertexAttribute.TexCoords(0));
    mesh.setVertices(vertices);

    short[] indices = new short[6];
    int v = 0;
    for (int i = 0; i < indices.length; i += 6, v += 4) {
      indices[i] = (short)(v);
      indices[i + 1] = (short)(v + 2);
      indices[i + 2] = (short)(v + 1);
      indices[i + 3] = (short)(v + 1);
      indices[i + 4] = (short)(v + 2);
      indices[i + 5] = (short)(v + 3);
    }

    mesh.setIndices(indices);
  }

  private void updateVerticies() {
    float left   = -0.5f;
    float right  = 0.5f;
    float top    = 0.5f;
    float bottom = -0.5f;

    // left top
    vertices[X1] = left;
    vertices[Y1] = top;
    vertices[Z1] = 0;
    // right top
    vertices[X2] = right;
    vertices[Y2] = top;
    vertices[Z2] = 0;
    // left bot
    vertices[X3] = left;
    vertices[Y3] = bottom;
    vertices[Z3] = 0;
    // right bot
    vertices[X4] = right;
    vertices[Y4] = bottom;
    vertices[Z4] = 0;

    // left top
    vertices[U1] = region.getU();
    vertices[V1] = region.getV();
    // right top
    vertices[U2] = region.getU2();
    vertices[V2] = region.getV();
    // left bot
    vertices[U3] = region.getU();
    vertices[V3] = region.getV2();
    // right bot
    vertices[U4] = region.getU2();
    vertices[V4] = region.getV2();
  }

  @Override
  public void dispose() {
    mesh.dispose();
    region = null;
  }

  public boolean is(TextureRegion region) {
    return region.getTexture() == this.region.getTexture() && region.getU() == this.region.getU() && region.getV() == this.region.getV() && region.getU2() == this.region.getU2() && region.getV2() == this.region.getV2();
  }
}