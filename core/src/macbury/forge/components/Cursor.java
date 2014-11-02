package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;


/**
 * Created by macbury on 27.10.14.
 */
public class Cursor extends Component {
  public BoundingBox cursorBox = new BoundingBox();
  public Color color           = new Color(Color.BLACK);
}
