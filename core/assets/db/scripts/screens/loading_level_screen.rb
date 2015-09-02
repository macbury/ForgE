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
  def onInitialize
    Gdx.app.log(TAG, self.teleport.to_s)
    @progress = PROGRESS_INITIAL
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
        Gdx.app.log(TAG, "Loaded 50 chunks")
      end
    }) do
      load_save
    end
  end

  def load_save
    @progress        = PROGRESS_LOAD_SAVE
    Gdx.app.log(TAG, "Loading entities etc...")
    Gdx.app.log(TAG, "Loading assets")
    load_assets
  end

  def load_assets
    @progress        = PROGRESS_LOAD_ASSETS
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
    ForgE.graphics.clearAll(Color::RED)
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
    self.teleport = nil
    @level = nil
  end
end
