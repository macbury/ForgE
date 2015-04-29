
function main($args, $game, $log, $db) {
  $log.info(TAG, "Initialized game!!!!");

  if ($args['testLevel'] != null) {
    $game.teleport($db.playerStartPosition);
  }
}