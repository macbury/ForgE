package macbury.forge.storage.serializers.level;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.graphics.builders.ChunkPartCollider;
import macbury.forge.graphics.renderable.VoxelChunkRenderableFactory;
import macbury.forge.terrain.geometry.DynamicGeometryProvider;
import macbury.forge.terrain.geometry.FileGeometryProvider;
import macbury.forge.terrain.geometry.GeometryCache;
import macbury.forge.terrain.geometry.TerrainGeometryProvider;
import macbury.forge.utils.Vector3i;

/**
 * Created by macbury on 04.08.15.
 */
public class TerrainGeometryProviderSerializer extends Serializer<TerrainGeometryProvider> {
  @Override
  public void write(Kryo kryo, Output output, TerrainGeometryProvider object) {
    output.writeInt(object.caches.size);
    for (GeometryCache geometryCache : object.caches) {
      kryo.writeObjectOrNull(output, geometryCache.position, Vector3i.class);
      output.writeInt(geometryCache.factories.size);

      for (VoxelChunkRenderableFactory voxelChunkRenderableFactory : geometryCache.factories) {
        kryo.writeObject(output, voxelChunkRenderableFactory);
      }

      output.writeInt(geometryCache.colliders.size);

      for (ChunkPartCollider collider : geometryCache.colliders) {
        kryo.writeObject(output, collider);
      }
    }


  }

  @Override
  public TerrainGeometryProvider read(Kryo kryo, Input input, Class<TerrainGeometryProvider> type) {
    FileGeometryProvider provider = new FileGeometryProvider();
    int cacheCount                = input.readInt();

    for (int i = 0; i < cacheCount; i++) {
      GeometryCache geometryCache = new GeometryCache();
      Vector3i position           = kryo.readObjectOrNull(input, Vector3i.class);
      geometryCache.position.set(position);
      provider.caches.add(geometryCache);


      int factoryCount            = input.readInt();
      for (int j = 0; j < factoryCount; j++) {
        VoxelChunkRenderableFactory factory = kryo.readObject(input, VoxelChunkRenderableFactory.class);
        geometryCache.factories.add(factory);
      }

      int colliderCount           = input.readInt();
      for (int j = 0; j < colliderCount; j++) {
        ChunkPartCollider collider = kryo.readObject(input, ChunkPartCollider.class);
        geometryCache.colliders.add(collider);
      }
    }
    return provider;
  }
}
