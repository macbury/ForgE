package macbury.forge.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import macbury.forge.components.BaseComponent;
import macbury.forge.systems.engine.LevelEntityEngine;

public class EntityBuilder {
  private String name;
  private Array<BaseComponent> components;

  public Entity build(LevelEntityEngine engine) {
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
