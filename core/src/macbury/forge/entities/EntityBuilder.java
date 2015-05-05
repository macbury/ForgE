package macbury.forge.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import macbury.forge.components.BaseComponent;
import macbury.forge.systems.engine.EntitySystemsManager;

public class EntityBuilder {
  private String name;
  private String parent;
  private Array<BaseComponent> components;

  public Entity build(EntitySystemsManager engine) {
    Entity e =  engine.createEntity();
    for (BaseComponent componentTemplate : components) {
      BaseComponent newComponent = engine.createComponent(componentTemplate.getClass());
      newComponent.set(componentTemplate);
      e.add(newComponent);
    }
    return e;
  }

  public String getName() {
    return name;
  }
}
