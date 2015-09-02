class GameplayScreen < AbstractScreen
  TAG                    = "GameplayScreen"
  FAR_CAMERA             = 70;
  NEAR_CAMERA            = 0.01;
  FIELD_OF_VIEW          = 70;
  attr_accessor :level, :teleport

  def onInitialize
    #Gdx.app.log(TAG, "Level=" + self.level.to_s)
    #Gdx.app.log(TAG, "Teleport=" + self.teleport.to_s)
    Gdx.input.setCursorCatched(true)
    @cameraController = FirstPersonCameraController.new(self.level.camera);

    self.level.camera.far          = FAR_CAMERA
    self.level.camera.near         = NEAR_CAMERA
    self.level.camera.fieldOfView  = FIELD_OF_VIEW
    spawn_player
  end

  def spawn_player
    @playerEntity       = ForgE.entities.get("player").build(self.level.entities)
    @playerEntity.getComponent(PlayerComponent.java_class).camera = self.level.camera;
    self.level.terrainMap.localVoxelPositionToWorldPosition(self.teleport.voxelPosition, @playerEntity.getComponent(PositionComponent.java_class).vector);
    @playerEntity.getComponent(PositionComponent.java_class).vector.sub(-0.5);
    self.level.entities.addEntity(@playerEntity)
  end

  def spawn_entity
=begin
var playerEntity            = $entities.get(entityName).build(this.level.entities);
var playerPositionComponent = playerEntity.getComponent(PositionComponent);
var temp                    = new Vector3(this.level.camera.direction).scl(2);
temp.add(this.level.camera.position);

playerEntity.getComponent(PositionComponent).vector.set(temp);
temp.set(this.level.camera.direction).scl(15);
playerEntity.getComponent(RigidBodyComoponent).initialImpulse.set(temp);
this.level.entities.addEntity(playerEntity);
=end
  end

  def render(delta)
    ForgE.time.update();
    ForgE.assets.update();
    @cameraController.update(delta)
    self.level.render(delta)

    spawn_entity if Gdx.input.isKeyJustPressed(Input::Keys::E)
    Gdx.app.exit if Gdx.input.isKeyPressed(Input::Keys::ESCAPE)
  end

  def resize(width, height)

  end

  def show

  end

  def hide

  end

  def pause

  end

  def resume

  end

  def dispose
    @playerEntity = nil
    self.level.dispose
    self.teleport = nil
    self.level    = nil
  end
end
