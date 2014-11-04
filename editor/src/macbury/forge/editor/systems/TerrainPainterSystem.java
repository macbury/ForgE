package macbury.forge.editor.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import macbury.forge.editor.selection.AbstractSelection;
import macbury.forge.editor.selection.SelectionInterface;
import macbury.forge.editor.undo_redo.ChangeManager;
import macbury.forge.editor.undo_redo.actions.ApplyRangeBlock;
import macbury.forge.level.Level;
import macbury.forge.level.map.ChunkMap;

/**
 * Created by macbury on 02.11.14.
 */
public class TerrainPainterSystem extends IntervalSystem implements SelectionInterface {
  private static final String TAG = "TerrainPainterSystem";
  private final ChunkMap map;
  private final ChangeManager changeManager;

  public TerrainPainterSystem(Level level, ChangeManager changeManager) {
    super(0.1f);
    this.map           = level.terrainMap;
    this.changeManager = changeManager;
  }

  @Override
  protected void updateInterval() {

  }

  @Override
  public void onSelectionStart(AbstractSelection selection) {

  }

  @Override
  public void onSelectionChange(AbstractSelection selection) {

  }

  @Override
  public void onSelectionEnd(AbstractSelection selection) {
    //Gdx.app.log(TAG, selection.toString());
    //changeManager.addChangeable(new ApplyBlock(selection, map)).apply();
    changeManager.addChangeable(new ApplyRangeBlock(selection, map)).apply();
  }
}