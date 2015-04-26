package macbury.forge.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import macbury.forge.components.CharacterComponent;
import macbury.forge.components.PositionComponent;

/**
 * Created by macbury on 26.04.15.
 * updates information in position component from bullet character
 */
public class CharacterSystem extends IteratingSystem {
  private final Matrix4 tempMat = new Matrix4();
  private final Vector3 tempVec = new Vector3();
  private ComponentMapper<PositionComponent> pm     = ComponentMapper.getFor(PositionComponent.class);
  private ComponentMapper<CharacterComponent> chm   = ComponentMapper.getFor(CharacterComponent.class);

  public CharacterSystem() {
    super(Family.getFor(CharacterComponent.class, PositionComponent.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent positionComponent   = pm.get(entity);
    CharacterComponent characterComponent = chm.get(entity);
    characterComponent.ghostObject.getWorldTransform(tempMat);
    tempMat.getTranslation(tempVec);
    positionComponent.setVector(tempVec);
  }
}
