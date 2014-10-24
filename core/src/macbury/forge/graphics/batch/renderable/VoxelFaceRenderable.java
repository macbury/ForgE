package macbury.forge.graphics.batch.renderable;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 23.10.14.
 */
public class VoxelFaceRenderable extends BaseRenderable implements Disposable {
  public Vector3 direction = new Vector3();

  @Override
  public void dispose() {
    if (mesh != null) {
      mesh.dispose();
    }
    mesh = null;
  }
}
