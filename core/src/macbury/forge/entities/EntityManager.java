package macbury.forge.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import macbury.forge.components.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

/**
 * Created by macbury on 19.03.15.
 */
public class EntityManager implements Disposable {
  private static final String TAG = "EntityManager";
  public HashMap<String, EntityBuilder> builders;
  private Json json;

  public EntityManager() {
    builders = new HashMap<String, EntityBuilder>();
    buildJsonReader();
    reload();
  }

  private void buildJsonReader() {
    this.json = new Json();
    json.addClassTag("position", PositionComponent.class);
    json.addClassTag("movement", MovementComponent.class);
    json.addClassTag("player", PlayerComponent.class);
    json.addClassTag("gravity", GravityComponent.class);
    json.addClassTag("collision", CollisionComponent.class);
  }

  private void reload() {
    builders.clear();
    Gdx.app.log(TAG, "Reloading...");
    FileHandle[] entityJsons = Gdx.files.internal("entities/").list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".json");
      }
    });

    for (FileHandle handle : entityJsons) {
      EntityBuilder builder = json.fromJson(EntityBuilder.class, handle.readString());
      builders.put(builder.getName(), builder);
      Gdx.app.log(TAG, "Loaded: " + builder.getName() + " from " + handle.path());
    }
  }

  @Override
  public void dispose() {

  }

  public EntityBuilder get(String entityId) {
    return builders.get(entityId);
  }

}
