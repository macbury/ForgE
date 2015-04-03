var TAG = "main";

function Start($game, $log, $db) {
  $log.info(TAG, "Initialized game!!!!");
  $game.teleport($db.playerStartPosition);
}