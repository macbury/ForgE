package macbury.forge.editor.selection;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 26.10.14.
 */
public abstract class AbstractSelection {
  public Vector3    startPosition;
  public Vector3    endPostion;

  public SelectType selectType = SelectType.Block;

}
