package macbury.forge.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;


/**
 * Created by macbury on 27.10.14.
 */
public class CursorComponent extends Component {
  private static final float BOUNDING_PADDING = 0.005F;
  public BoundingBox cursorBox = new BoundingBox();
  public Color color           = new Color(Color.BLACK);

  public void set(BoundingBox boundingBox) {
    cursorBox.set(boundingBox);
    cursorBox.min.sub(BOUNDING_PADDING, BOUNDING_PADDING, BOUNDING_PADDING);
    cursorBox.max.add(BOUNDING_PADDING, BOUNDING_PADDING, BOUNDING_PADDING);
  }
}
