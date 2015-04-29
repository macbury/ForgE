
function main($game, $log, $db) {
  $log.info(TAG, "Initialized game!!!!");
  $game.teleport($db.playerStartPosition);
}