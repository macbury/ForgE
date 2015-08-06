package macbury.forge.storage.serializers.level;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.mesh.MeshFactory;
import macbury.forge.graphics.mesh.MeshVertexInfo;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderableFactory;
import macbury.forge.shaders.attributes.SolidTerrainAttribute;
import macbury.forge.shaders.attributes.WaterAttribute;

/**
 * Created by macbury on 05.08.15.
 */
public class VoxelChunkRenderableFactorySerializer extends Serializer<VoxelChunkRenderableFactory> {
  @Override
  public void write(Kryo kryo, Output output, VoxelChunkRenderableFactory object) {
    output.writeInt(object.triangleCount);
    output.writeInt(object.primitiveType);

    output.writeBoolean(object.material.has(SolidTerrainAttribute.Type));
    output.writeBoolean(object.material.has(BlendingAttribute.Type));
    output.writeBoolean(object.material.has(WaterAttribute.Type));

    output.writeInt(object.attributes.length);
    for (MeshVertexInfo.AttributeType attribute : object.attributes) {
      kryo.writeObject(output, attribute);
    }
    output.writeInt(object.meshFactory.verties.length);
    output.writeInt(object.meshFactory.indices.length);

    output.writeFloats(object.meshFactory.verties);
    output.writeShorts(object.meshFactory.indices);
  }

  @Override
  public VoxelChunkRenderableFactory read(Kryo kryo, Input input, Class<VoxelChunkRenderableFactory> type) {
    VoxelChunkRenderableFactory factory = new VoxelChunkRenderableFactory();
    factory.triangleCount               = input.readInt();
    factory.primitiveType               = input.readInt();

    factory.material                    = new Material();
    if (input.readBoolean())
      factory.material.set(new SolidTerrainAttribute());

    if (input.readBoolean())
      factory.material.set(new BlendingAttribute());

    if (input.readBoolean())
      factory.material.set(new WaterAttribute());

    int attributeCount                  = input.readInt();
    factory.attributes                  = new MeshVertexInfo.AttributeType[attributeCount];
    for (int i = 0; i < attributeCount; i++) {
      factory.attributes[i] = kryo.readObject(input, MeshVertexInfo.AttributeType.class);
    }

    int vertCount                       = input.readInt();
    int indiCount                       = input.readInt();

    factory.meshFactory                 = new MeshFactory(input.readFloats(vertCount), input.readShorts(indiCount), factory.attributes);

    return factory;
  }
}
