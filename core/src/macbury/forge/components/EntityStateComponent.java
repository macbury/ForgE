package macbury.forge.components;

/**
 * Created by macbury on 23.03.15.
 */
public class EntityStateComponent extends BaseComponent {
  public enum State {
    Airborne, Grounded
  }
  public State state = State.Airborne;
  @Override
  public void set(BaseComponent otherComponent) {
    state = State.Airborne;
  }

  @Override
  public void reset() {

  }
}
