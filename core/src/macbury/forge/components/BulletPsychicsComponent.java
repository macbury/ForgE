package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.compression.lzma.Base;

/**
 * Created by macbury on 24.04.15.
 */
public abstract class BulletPsychicsComponent extends BaseComponent {
  protected btDiscreteDynamicsWorld world;
  protected static final Matrix4 tempMat = new Matrix4();
  public abstract void initBullet(PositionComponent positionComponent, btDiscreteDynamicsWorld world, Vector3 size, Entity entity);
  public abstract void disposeBullet();

  @Override
  public void reset() {
    disposeBullet();
  }
}
