package macbury.forge.graphics.builders;

import com.badlogic.ashley.core.Entity;
import macbury.forge.graphics.batch.renderable.TerrainChunkRenderable;

/**
 * Created by macbury on 19.10.14.
 */
public class TerrainChunk extends Chunk {
  public Entity                 entity;
  public TerrainChunkRenderable renderable;
}
