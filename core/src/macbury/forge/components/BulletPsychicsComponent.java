package macbury.forge.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.compression.lzma.Base;

/**
 * Created by macbury on 24.04.15.
 */
public abstract class BulletPsychicsComponent extends BaseComponent {
  protected btDiscreteDynamicsWorld world;
  public abstract void initBullet(Matrix4 transform, btDiscreteDynamicsWorld world, Vector3 size);
  public abstract void disposeBullet();

  @Override
  public void reset() {
    disposeBullet();
  }
}
