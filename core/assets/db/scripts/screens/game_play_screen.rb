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

    @gameplay_view                 = GameplayView.new

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
    boxEntity         = ForgE.entities.get("crate").build(self.level.entities)
    positionComponent = boxEntity.getComponent(PositionComponent.java_class)
    temp              = Vector3.new(@level.camera.direction).scl(2)
    temp.add(self.level.camera.position)
    positionComponent.vector.set(temp)
    temp.set(self.level.camera.direction).scl(15)
    boxEntity.getComponent(RigidBodyComoponent.java_class).initialImpulse.set(temp)
    self.level.entities.addEntity(boxEntity)
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
    ForgE.ui.addActor(@gameplay_view)
  end

  def hide
    @gameplay_view.remove
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
