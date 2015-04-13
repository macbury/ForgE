
function onStart($game) {
  $log.info(TAG, "Initialized game!!!!");
  $game.teleport($db.playerStartPosition);
}