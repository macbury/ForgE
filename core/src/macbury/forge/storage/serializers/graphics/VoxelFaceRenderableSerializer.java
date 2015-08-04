package macbury.forge.storage.serializers.graphics;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.mesh.MeshFactory;
import macbury.forge.graphics.mesh.MeshVertexInfo;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by macbury on 21.03.15.
 */
public class VoxelFaceRenderableSerializer extends Serializer<VoxelChunkRenderable> {
  @Override
  public void write(Kryo kryo, Output output, VoxelChunkRenderable face) {
    face.buildMeshFromFactory();
    kryo.writeObjectOrNull(output, face.material, Material.class);
    kryo.writeObject(output, face.direction);
    kryo.writeObject(output, face.boundingBox);
    output.writeInt(face.primitiveType);
    output.writeInt(face.triangleCount);
    kryo.writeObject(output, face.worldTransform);

    int max              = face.mesh.getNumVertices() * face.mesh.getVertexSize() / 4;
    FloatBuffer vertBuff = face.mesh.getVerticesBuffer();
    output.writeInt(max);
    int oldPosition      = vertBuff.position();
    for (int pos = 0; pos < max; pos++) {
      vertBuff.position(pos);
      output.writeFloat(vertBuff.get());
    }
    vertBuff.position(oldPosition);

    ShortBuffer indiBuff = face.mesh.getIndicesBuffer();
    oldPosition          = indiBuff.position();
    output.writeInt(face.mesh.getNumIndices());
    for (int pos = 0; pos < face.mesh.getNumIndices(); pos++) {
      indiBuff.position(pos);
      output.writeShort(indiBuff.get());
    }

    indiBuff.position(oldPosition);

  }

  @Override
  public VoxelChunkRenderable read(Kryo kryo, Input input, Class<VoxelChunkRenderable> type) {
    VoxelChunkRenderable renderable = new VoxelChunkRenderable();
    renderable.material             = kryo.readObjectOrNull(input, Material.class);
    renderable.direction            = kryo.readObject(input, Vector3.class);
    renderable.boundingBox          = kryo.readObject(input, BoundingBox.class);
    renderable.primitiveType        = input.readInt();
    renderable.triangleCount        = input.readInt();
    renderable.worldTransform.set(kryo.readObject(input, Matrix4.class));

    int maxVerticies                = input.readInt();
    float[] vertBuff                = new float[maxVerticies];

    for (int pos = 0; pos < maxVerticies; pos++) {
      vertBuff[pos] = input.readFloat();
    }

    int numIndicies         = input.readInt();
    short[] indiBuff        = new short[numIndicies];

    for (int pos = 0; pos < numIndicies; pos++) {
      indiBuff[pos] = input.readShort();
    }

    renderable.meshFactory = new MeshFactory(vertBuff, indiBuff, MeshVertexInfo.voxelAttributes());
    return renderable;
  }
}
