import "scripts:libs/defer"
import "scripts:screens/main_menu_screen"
import "scripts:screens/game_play_screen"
import "scripts:screens/loading_level_screen"

if ForgE.db.startPosition
  loadingScreen          = LoadingLevelScreen.new
  loadingScreen.teleport = ForgE.db.startPosition
  ForgE.screens.set(loadingScreen)
else
  throw "Could not find start position!"
end
