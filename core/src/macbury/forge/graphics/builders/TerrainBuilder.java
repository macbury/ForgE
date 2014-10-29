package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.graphics.VoxelMaterial;
import macbury.forge.graphics.VoxelMap;
import macbury.forge.graphics.batch.renderable.VoxelFaceRenderable;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.level.map.ChunkMap;
import macbury.forge.shaders.utils.RenderableBaseShader;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 16.10.14.
 */
public class TerrainBuilder extends VoxelsAssembler {

  public enum Face {
    Left(Vector3i.LEFT), Right(Vector3i.RIGHT), Top(Vector3i.TOP), Bottom(Vector3i.BOTTOM), Front(Vector3i.FRONT), Back(Vector3i.BACK);
    public final Vector3i direction;

    Face(Vector3i direction) {
      this.direction = direction;
    }
  }
  private static final String TAG = "TerrainBuilder";
  private static final String TERRAIN_SHADER = "terrain";
  private final VoxelMap map;
  public final TerrainCursor cursor;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  private Array<Face> facesToBuild = new Array<Face>();

  public TerrainBuilder(VoxelMap voxelMap) {
    super();
    this.map       = voxelMap;
    this.cursor    = new TerrainCursor();
  }

  private void buildFace(Vector3i checkTileInDirection, Face face) {
    for (int y = cursor.start.y; y < cursor.end.y; y++) {
      for (int x = cursor.start.x; x < cursor.end.x; x++) {
        for (int z = cursor.start.z; z < cursor.end.z; z++) {
          if (map.isSolid(x, y, z)) {
            cursor.size.x = Math.max(x, cursor.size.x);
            cursor.size.y = Math.max(y, cursor.size.y);
            cursor.size.z = Math.max(z, cursor.size.z);

            tempB.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.tileSize);
            tempA.set(x,y,z).scl(map.tileSize).sub(tempB);

            if (map.isEmptyNotOutOfBounds(x + checkTileInDirection.x,y + checkTileInDirection.y, z + checkTileInDirection.z)) {
              VoxelMaterial material = map.getMaterialForPosition(x, y, z);
              switch (face) {
                case Top:
                  top(tempA, map.tileSize, material);
                break;

                case Bottom:
                  bottom(tempA, map.tileSize, material);
                break;

                case Front:
                  front(tempA, map.tileSize, material);
                break;

                case Back:
                  back(tempA, map.tileSize, material);
                break;

                case Left:
                  left(tempA, map.tileSize, material);
                break;

                case Right:
                  right(tempA, map.tileSize, material);
                break;
              }

            }
          }
        }
      }
    }
    tempA.set(cursor.start.x, cursor.start.y, cursor.start.z).scl(map.tileSize);
    cursor.size.scl(map.tileSize).sub(tempA);
  }

  public void facesForChunk(Chunk chunk) {
    facesForPart(chunk.start, chunk.end, chunk.size);
  }

  private void facesForPart(Vector3i start, Vector3i end, Vector3 outSize) {
    outSize.setZero();
    Gdx.app.log(TAG, "Building faces for: " + start.toString() + " - " + end.toString());
    for (int x = start.x; x < end.x; x++) {
      for (int y = start.y; y < end.y; y++) {
        for (int z = start.z; z < end.z; z++) {
          if (map.isSolid(x, y, z)) {
            outSize.x = Math.max(x, outSize.x);
            outSize.y = Math.max(y, outSize.y);
            outSize.z = Math.max(z, outSize.z);

            tempA.set(x,y,z);
            Gdx.app.log(TAG, tempA.toString());
            VoxelMaterial material = map.getMaterialForPosition(x, y, z);

            if (map.isEmpty(x,y+1,z)) {
              top(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x,y-1,z)) {
              bottom(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x-1,y,z)) {
              left(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x+1,y,z)) {
              right(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x,y,z+1)) {
              front(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }

            if (map.isEmpty(x,y,z-1)) {
              back(tempA, ChunkMap.TERRAIN_TILE_SIZE, material);
            }
          }
        }
      }
    }
    outSize.sub(start.x, start.y, start.z).add(1,1,1);
  }

  public void facesForMap() {
    facesForPart(new Vector3i(0,0,0), new Vector3i(map.getWidth(), map.getHeight(), map.getDepth()), new Vector3());
  }

  @Override
  public void begin() {
    super.begin();
    facesToBuild.clear();
    facesToBuild.addAll(Face.values());
  }

  public boolean next() {
    return facesToBuild.size > 0;
  }

  public VoxelFaceRenderable buildFaceForChunk(Chunk chunk) {
    Face face = facesToBuild.pop();
    buildFace(face.direction, face);

    if (isEmpty()) {
      return null;
    } else {
      VoxelFaceRenderable renderable = getRenderable();
      renderable.worldTransform.idt();
      renderable.worldTransform.translate(chunk.worldPosition);
      renderable.direction.set(face.direction.x, face.direction.y, face.direction.z);
      renderable.mesh.calculateBoundingBox(renderable.boundingBox);
      renderable.boundingBox.min.add(chunk.worldPosition);
      renderable.boundingBox.max.add(chunk.worldPosition);
      return renderable;
    }
  }

  private VoxelFaceRenderable getRenderable() {
    VoxelFaceRenderable renderable   = new VoxelFaceRenderable();
    renderable.primitiveType         = GL30.GL_TRIANGLES;
    renderable.shader                = (RenderableBaseShader) ForgE.shaders.get(TERRAIN_SHADER);

    if (ForgE.config.generateWireframe)
      renderable.wireframe           = this.wireframe();
    renderable.mesh = this.mesh(MeshVertexInfo.AttributeType.Position, MeshVertexInfo.AttributeType.Normal, MeshVertexInfo.AttributeType.Color);

    return renderable;
  }

}
