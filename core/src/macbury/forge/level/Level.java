package macbury.forge.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.graphics.batch.VoxelBatch;
import macbury.forge.graphics.batch.renderable.TerrainTileRenderable;
import macbury.forge.graphics.builders.TerrainBuilder;

/**
 * Created by macbury on 18.10.14.
 */
public class Level implements Disposable {
  public final Engine                engine;
  public final PerspectiveCamera     camera;
  public final VoxelBatch            batch;

  private TerrainTileRenderable terrainTile;

  public Level() {
    this.batch              = new VoxelBatch();
    this.camera             = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.engine             = new Engine();
    TerrainBuilder assembler = new TerrainBuilder();

    Vector3 position = new Vector3();
    Vector3 size     = new Vector3(1,1,1);

    Color green1 = new Color(14f / 255f, 123f / 255f, 34f / 255f, 1);
    Color green2 = new Color(44f / 255f, 159f / 255f, 93f / 255f, 1);
    Color green3 = new Color(82f / 255f, 198f / 255f, 152f / 255f, 1);

    Array<Color> pallette = new Array<Color>();
    pallette.add(green1);
    pallette.add(green2);
    pallette.add(green3);

    assembler.begin(); {
      for (int x = 0; x < 5; x++) {
        for (int y = 0; y < 5; y++) {
          for (int z = 0; z < 5; z++) {
            Color color = pallette.get((int)Math.round((float)(pallette.size - 1) * Math.random()));
            position.set(x, y, z);
            assembler.top(position, size, color);
            assembler.bottom(position, size, color);

            assembler.front(position, size, color);
            assembler.back(position, size, color);

            assembler.left(position, size, color);
            assembler.right(position, size, color);
          }
        }
      }

      this.terrainTile = assembler.getRenderable();
    } assembler.end();
  }

  public void resize(int width, int height) {
    camera.viewportWidth  = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  public void update(float delta) {
    camera.update();
  }

  public void render() {
    batch.begin(camera); {
      batch.add(terrainTile);
      batch.render();
    } batch.end();
  }


  @Override
  public void dispose() {
    batch.dispose();
  }

  public void setRenderType(VoxelBatch.RenderType renderType) {
    batch.setType(renderType);
  }
}
