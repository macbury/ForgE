class LoadingLevelScreen < AbstractScreen
  TAG = "LoadingLevelScreen"
  attr_accessor :teleport

  PROGRESS_INITIAL               = 0
  PROGRESS_LOAD_STATE            = 20
  PROGRESS_LOAD_GEOMETRY         = 40
  PROGRESS_BUILD_GEOMETRY        = 60
  PROGRESS_LOAD_SAVE             = 75
  PROGRESS_LOAD_ASSETS           = 90
  PROGRESS_DONE                  = 100

  ROTATION_SPEED                 = 80.0
  STANDARD_CUBE_ROTATION         = 60.0

  def onInitialize
    Gdx.app.log(TAG, self.teleport.to_s)

    @camera         = PerspectiveCamera.new(67, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    @boxTransMat    = Matrix4.new
    @shapeRenderer  = ShapeRenderer.new

    @camera.position.set(0, 8, 0)
    @camera.lookAt(Vector3::Zero)

    @indicatorRotation = 0.0

    @progress       = PROGRESS_INITIAL
    load_level_state_and_geometry
  end

  def load_level_state_and_geometry
    Defer.exec(Proc.new {
      Gdx.app.log(TAG, "Loading state and geometry")
      levelState       = ForgE.levels.load(self.teleport.mapId)
      @progress        = PROGRESS_LOAD_STATE
      geometryProvider = ForgE.levels.loadGeometry(levelState)
      @progress        = PROGRESS_LOAD_GEOMETRY
      [levelState, geometryProvider]
    }) do |levelState, geometryProvider|
      if geometryProvider
        Gdx.app.log(TAG, "Creating level")
        @level = Level.new(levelState, geometryProvider)
        build_geometry
      else
        throw "Could not load geometry for map: " + levelState.id
      end
    end
  end

  def build_geometry
    Gdx.app.log(TAG, "Building geometry")
    Defer.exec(Proc.new {
      @progress        = PROGRESS_BUILD_GEOMETRY
      while !@level.terrainEngine.rebuildInBackground(50)

      end
    }) do
      load_save
    end
  end

  def load_save
    @progress        = PROGRESS_LOAD_SAVE
    Gdx.app.log(TAG, "Loading entities etc...")
    Defer.exec(Proc.new {
      sleep 1
    }) do
      @progress        = PROGRESS_LOAD_ASSETS
      Gdx.app.log(TAG, "Loading assets")
      load_assets
    end

  end

  def load_assets
    if ForgE.assets.loadPendingInChunks
      ForgE.assets.unloadUnusedAssets
      loading_finished
    else
      Defer.main { load_assets }
    end
  end

  def loading_finished
    Gdx.app.log(TAG, "Loading finished")
    @progress                = PROGRESS_DONE
    gameplay_screen          = GameplayScreen.new
    gameplay_screen.level    = @level
    gameplay_screen.teleport = self.teleport
    ForgE.screens.set(gameplay_screen)
  end

  def render(delta)
    ForgE.graphics.clearAll(Color::BLACK)
    @camera.update
    @indicatorRotation += ROTATION_SPEED * delta;

    renderVoxelProgress
    renderVoxelBorder
  end

  def renderVoxelBorder
    @shapeRenderer.begin(ShapeRenderer::ShapeType::Line)
      @shapeRenderer.setProjectionMatrix(@camera.combined)
      @shapeRenderer.identity
      @shapeRenderer.setColor(1.0, 1.0, 1.0, 1.0)
      @shapeRenderer.translate(6, 0, 4)
      @shapeRenderer.rotate(0,0,1, STANDARD_CUBE_ROTATION)
      @shapeRenderer.rotate(0,0,1, @indicatorRotation)
      @shapeRenderer.box(-0.5,-0.5,-0.5, 1, 1, 1)
    @shapeRenderer.end
  end

  def renderVoxelProgress
    @shapeRenderer.begin(ShapeRenderer::ShapeType::Filled)
      @shapeRenderer.setProjectionMatrix(@camera.combined)
      @shapeRenderer.identity
      @shapeRenderer.setColor(1.0, 1.0, 1.0, 0.7)
      @shapeRenderer.translate(6, 0, 4)
      @shapeRenderer.rotate(0,0,1, STANDARD_CUBE_ROTATION)
      @shapeRenderer.rotate(0,0,1, @indicatorRotation)
      @shapeRenderer.box(-0.5,-0.5,-0.5, 1, 1, @progress / 100.0)
    @shapeRenderer.end
  end

  def resize(width, height)

  end

  def show

  end

  def hide
    dispose
  end

  def pause

  end

  def resume

  end

  def dispose
    Gdx.app.log(TAG, "Dispose...")
    self.teleport = nil
    @shapeRenderer.dispose
    @shapeRenderer = nil
    @level = nil
  end
  
end
