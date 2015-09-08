module Menu
  class MainMenuScreen < AbstractScreen
    BUTTON_WIDTH        = 250
    MENU_RIGHT_PADDING  = 75
    MENU_BOTTOM_PADDING = 80
    BUTTON_HEIGHT       = 70
    BUTTON_PADDING      = 5


    def initialize

    end

    def onInitialize
      @container      = ForgE.ui.table
      @selectSound    = ForgE.assets.getSound("sounds:select.wav")
      @selectSound.retain
      createHeader
      createMenu
      createFooter
      bindEvents
    end

    def render(delta)
      ForgE.assets.update()
    end

    def resize(width, height)
    end

    def show
      ForgE.ui.addActor(@container)
    end

    def hide
      @container.remove if @container
    end

    def pause

    end

    def resume

    end

    def dispose
      @container.remove
      @selectSound.release
      @startGameButton.dispose
      @loadGameButton.dispose
      @settingsGameButton.dispose
      @exitGameButton.dispose
      @container = nil
    end

    private
      def bindEvents
        @startGameButton.onClick do
          Gdx.app.log("Clicked", "start game button")
          @selectSound.play
          loadingScreen          = Loading::LevelScreen.new
          loadingScreen.teleport = ForgE.db.startPosition
          ForgE.screens.set(loadingScreen)
        end

        @loadGameButton.onClick do
          Gdx.app.log("Clicked", "load game button")
          @selectSound.play
        end

        @settingsGameButton.onClick do
          Gdx.app.log("Clicked", "settings game button")
          @selectSound.play
        end

        @exitGameButton.onClick do
          Gdx.app.exit
        end
      end

      def createHeader
        @container.row
        @container.add.expandX.expandY
      end

      def createMenu
        @startGameButton     = UI::Button.new("Start game")
        @loadGameButton      = UI::Button.new("Load game")
        @settingsGameButton  = UI::Button.new("Settings")
        @exitGameButton      = UI::Button.new("Exit")

        [@startGameButton, @loadGameButton, @settingsGameButton, @exitGameButton].each do |menu_button|
          @container.row
          @container.add.expandX
          @container.add(menu_button).width(BUTTON_WIDTH).padRight(MENU_RIGHT_PADDING).height(BUTTON_HEIGHT).padBottom(BUTTON_PADDING)
        end
      end

      def createFooter
        @container.row
        @container.add.expandX.height(MENU_BOTTOM_PADDING)
      end
  end
end
