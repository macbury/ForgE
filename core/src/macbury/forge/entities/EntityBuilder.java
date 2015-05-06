package macbury.forge.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.components.BaseComponent;
import macbury.forge.systems.engine.EntitySystemsManager;

public class EntityBuilder {
  private String name;
  private String parent;
  private Array<BaseComponent> components;
  private EntityManager manager;

  public Entity build(EntitySystemsManager engine) {
    Entity template = null;
    if (haveParent()) {
      template = getParent(engine);
      if (template == null) {
        throw new GdxRuntimeException("Could not find parent " + parent);
      }
    } else {
      template = engine.createEntity();
    }
    for (BaseComponent componentTemplate : components) {
      BaseComponent currentComponent = template.getComponent(componentTemplate.getClass());
      if (currentComponent == null) {
        currentComponent = engine.createComponent(componentTemplate.getClass());
        template.add(currentComponent);
      }
      currentComponent.set(componentTemplate);
    }
    return template;
  }

  private boolean haveParent() {
    return parent != null;
  }

  private Entity getParent(EntitySystemsManager engine) {
    return manager.get(parent).build(engine);
  }

  public String getName() {
    return name;
  }

  public void setManager(EntityManager entityManager) {
    manager = entityManager;
  }
}
