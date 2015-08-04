package macbury.forge.storage.serializers.level;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import macbury.forge.graphics.batch.renderable.VoxelChunkRenderable;
import macbury.forge.terrain.geometry.DynamicGeometryProvider;
import macbury.forge.terrain.geometry.GeometryCache;
import macbury.forge.terrain.geometry.TerrainGeometryProvider;

/**
 * Created by macbury on 04.08.15.
 */
public class TerrainGeometryProviderSerializer extends Serializer<TerrainGeometryProvider> {
  @Override
  public void write(Kryo kryo, Output output, TerrainGeometryProvider object) {
    kryo.writeClass(output, object.getClass());
    output.writeInt(object.caches.size);
    for (GeometryCache geometryCache : object.caches) {
      kryo.writeObject(output, geometryCache.position);
      output.writeInt(geometryCache.renderables.size);

      for (VoxelChunkRenderable renderable : geometryCache.renderables) {
        kryo.writeObject(output, renderable);
      }
    }
  }

  @Override
  public TerrainGeometryProvider read(Kryo kryo, Input input, Class<TerrainGeometryProvider> type) {
    return null;
  }
}
