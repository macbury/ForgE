package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import macbury.forge.graphics.batch.Wireframe;

import java.util.ArrayList;

/**
 * Created by macbury on 30.08.14.
 */
public class MeshAssembler implements Disposable {

  private final Pool<MeshVertexInfo> meshVertexPool = Pools.get(MeshVertexInfo.class);
  private final Pool<MeshTriangle> trianglesPool    = Pools.get(MeshTriangle.class);

  protected ArrayList<MeshVertexInfo> vertexArrayList;
  protected ArrayList<MeshTriangle> triangleArrayList;
  private short currentIndex = 0;

  public MeshAssembler() {
    vertexArrayList    = new ArrayList<MeshVertexInfo>();
    triangleArrayList  = new ArrayList<MeshTriangle>();
  }

  public void begin() {
    clear();
  }

  public MeshVertexInfo vertex(float x, float y, float z) {
    MeshVertexInfo vert = meshVertexPool.obtain();
    vert.index          = currentIndex;
    currentIndex        += 1;
    vert.set(x,y,z);
    vertexArrayList.add(vert);
    return vert;
  }

  public MeshVertexInfo vertex(Vector3 vec) {
    return vertex(vec.x, vec.y, vec.z);
  }

  public MeshVertexInfo vertex() {
    return vertex(Vector3.Zero);
  }

  public MeshTriangle triangle(MeshVertexInfo vertex1, MeshVertexInfo vertex2, MeshVertexInfo vertex3) {
    MeshTriangle triangle = trianglesPool.obtain();
    triangle.set(vertex1, vertex2, vertex3);
    triangleArrayList.add(triangle);
    return triangle;
  }

  public MeshFactory meshFactory(MeshVertexInfo.AttributeType... attributtes) {
    VertexAttribute meshAttributtes[] = new VertexAttribute[attributtes.length];
    int vertiesArraySize = 0;
    int cursor           = 0;
    boolean usingPosition = false;
    boolean usingNormal   = false;
    boolean usingTexture  = false;
    boolean usingColor    = false;
    boolean usingMaterial = false;
    boolean usingTextureFullCords = false;
    for (MeshVertexInfo.AttributeType attr : attributtes) {
      vertiesArraySize        += attr.size();
      meshAttributtes[cursor] = attr.attribute();
      cursor++;
      switch (attr) {
        case Position:
          usingPosition = true;
          break;
        case Normal:
          usingNormal = true;
          break;
        case TextureCord:
          usingTexture = true;
          break;
        case Color:
          usingColor = true;
          break;

        case TextureFullCords:
          usingTextureFullCords = true;
          break;

        case Material:
          usingMaterial = true;
          break;
      }
    }

    float verties[] = new float[this.vertexArrayList.size() * vertiesArraySize];
    short indices[] = new short[this.triangleArrayList.size() * 3];

    cursor = 0;

    for (MeshTriangle triangle : this.triangleArrayList) {
      indices[cursor++] = triangle.vert1.index;
      indices[cursor++] = triangle.vert2.index;
      indices[cursor++] = triangle.vert3.index;
    }

    cursor = 0;

    for (MeshVertexInfo vertex : this.vertexArrayList) {
      if (usingPosition) {
        verties[cursor++] = vertex.position.x;
        verties[cursor++] = vertex.position.y;
        verties[cursor++] = vertex.position.z;
      }

      if (usingNormal) {
        verties[cursor++] = vertex.normal.x;
        verties[cursor++] = vertex.normal.y;
        verties[cursor++] = vertex.normal.z;
      }

      if (usingTexture) {
        verties[cursor++] = vertex.uv.x;
        verties[cursor++] = vertex.uv.y;
      }

      if (usingColor) {
        verties[cursor++] = vertex.color();
      }

      if (usingMaterial) {
        verties[cursor++] = vertex.material();
      }

      if (usingTextureFullCords) {
        verties[cursor++] = vertex.textureFullCords[0];
        verties[cursor++] = vertex.textureFullCords[1];
        verties[cursor++] = vertex.textureFullCords[2];
        verties[cursor++] = vertex.textureFullCords[3];
      }
    }

    MeshFactory tempMeshData = new MeshFactory(verties, indices, meshAttributtes);

    clear();
    return tempMeshData;
  }

  public Mesh mesh(MeshVertexInfo.AttributeType... attributtes) {
    MeshFactory meshFactory = meshFactory(attributtes);
    Mesh mesh               = meshFactory.get();
    meshFactory.dispose();
    return mesh;
  }

  public Wireframe wireframe() {
    return new Wireframe(triangleArrayList);
  }

  public void end() {
    clear();
  }

  private void clear() {
    currentIndex = 0;

    for(MeshVertexInfo vert : vertexArrayList)
      meshVertexPool.free(vert);
    for(MeshTriangle triangle : triangleArrayList)
      trianglesPool.free(triangle);

    vertexArrayList.clear();
    triangleArrayList.clear();
  }


  @Override
  public void dispose() {
    clear();
    meshVertexPool.clear();
    meshVertexPool.clear();
  }

  public boolean isEmpty() {
    return triangleArrayList.size() == 0;
  }

  public boolean haveGeometry() {
    return !isEmpty();
  }

  public int getTriangleCount() {
    return this.triangleArrayList.size();
  }

  public class MeshFactory implements Disposable {
    private final VertexAttribute[] attributes;
    private float verties[];
    private short indices[];

    public MeshFactory(float[] verties, short[] indices, VertexAttribute[] meshAttributtes) {
      this.verties    = verties;
      this.indices    = indices;
      this.attributes = meshAttributtes;
    }

    public Mesh get() {
      Mesh mesh = new Mesh(true, verties.length, indices.length, attributes);
      mesh.setVertices(verties);
      mesh.setIndices(indices);
      mesh.setAutoBind(false);
      return mesh;
    }

    @Override
    public void dispose() {
      verties = null;
      indices = null;
    }
  }
}
