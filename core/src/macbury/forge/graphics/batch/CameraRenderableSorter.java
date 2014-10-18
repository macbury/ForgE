package macbury.forge.graphics.batch;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import macbury.forge.graphics.batch.renderable.BaseRenderable;

import java.util.Comparator;

/**
 * Created by macbury on 18.10.14.
 */
public class CameraRenderableSorter implements Comparator<BaseRenderable> {
  private Camera camera;
  private final Vector3 tmpV1 = new Vector3();
  private final Vector3 tmpV2 = new Vector3();

  public void sort (final Camera camera, final Array<BaseRenderable> renderables) {
    this.camera = camera;
    renderables.sort(this);
  }

  public int compare (final BaseRenderable o1, final BaseRenderable o2) {
    o1.worldTransform.getTranslation(tmpV1);
    o2.worldTransform.getTranslation(tmpV2);
    final float dst = (int)(1000f * camera.position.dst2(tmpV1)) - (int)(1000f * camera.position.dst2(tmpV2));
    final int result = dst < 0 ? -1 : (dst > 0 ? 1 : 0);
    return result;
  }
}
