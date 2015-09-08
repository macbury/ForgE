import "scripts:libs/defer"
import "scripts:libs/ui"
import "scripts:screens/menu/main_menu_screen"
import "scripts:screens/game_play_screen"

import "scripts:screens/loading/base_screen"
import "scripts:screens/loading/level_screen"

if ForgE.db.startPosition
  #loadingScreen          = LoadingLevelScreen.new
  #loadingScreen.teleport = ForgE.db.startPosition
  #ForgE.screens.set(loadingScreen)
  ForgE.screens.set(Menu::MainMenuScreen.new)
else
  throw "Could not find start position!"
end
