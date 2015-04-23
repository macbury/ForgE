package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.components.CollisionComponent;
import macbury.forge.components.MovementComponent;
import macbury.forge.components.PositionComponent;
import macbury.forge.utils.MyMath;

/**
 * Created by macbury on 20.04.15.
 */
public class PositionSystem extends IteratingSystem {
  private static final String TAG                 = "PositionSystem";
  private ComponentMapper<PositionComponent> pm   = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<MovementComponent> mm   = ComponentMapper.getFor(MovementComponent.class);
  private ComponentMapper<CollisionComponent> cm  = ComponentMapper.getFor(CollisionComponent.class);

  private final Vector3 tempA = new Vector3();
  private final Vector3 tempB = new Vector3();
  public PositionSystem() {
    super(Family.getFor(PositionComponent.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    /*
    PositionComponent pc  = pm.get(entity);
    MovementComponent mc  = mm.get(entity);
    CollisionComponent cc = cm.get(entity);
    tempA.setZero();

    if (mc != null) {
      tempA.add(mc.targetPosition);
    }

    if (cc != null && !cc.normal.isZero()) {
      MyMath.round(cc.normal);
      MyMath.neg(cc.normal, tempB);
      //Gdx.app.log(TAG, "normal=" + tempB);
      tempA.scl(tempB);
    }

    if (!tempA.isZero()) {
      pc.dirty = true;
      pc.vector.add(tempA);
    }

    pc.updateTransformMatrix();*/
  }
}
