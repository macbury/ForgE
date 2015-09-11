module Loading
  class LevelScreen < BaseScreen

    def loading_finished
      ForgE.log(TAG, "Loading finished")
      @progress                = PROGRESS_DONE
      gameplay_screen          = GameplayScreen.new
      gameplay_screen.level    = @level
      gameplay_screen.teleport = self.teleport
      ForgE.screens.set(gameplay_screen)
    end

    def get_map_id
      self.teleport.mapId
    end

  end
end
