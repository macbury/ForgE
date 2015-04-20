package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Gdx;
import com.l2fprod.common.propertysheet.Property;
import macbury.forge.editor.undo_redo.Changeable;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

/**
 * Created by macbury on 15.03.15.
 */
public class PropertyChangeable extends Changeable {
  private static final String TAG = "PropertyChangeable";
  private final Object object;
  private final PropertyChangeEvent event;
  private final Property prop;
  private final Object oldValue;
  private final Object newValue;

  public PropertyChangeable(Object object, PropertyChangeEvent event) {
    this.object = object;
    this.event  = event;

    this.prop     = (Property) event.getSource();
    this.oldValue = event.getOldValue();
    this.newValue = event.getNewValue();
  }

  @Override
  public void revert() {
    try {
      Gdx.app.log(TAG, "Revert " + oldValue + " for " + object);
      prop.setValue(oldValue);
      prop.writeToObject(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void apply() {
    try {
      Gdx.app.log(TAG, "Apply " + newValue + " for " + object);
      prop.setValue(newValue);
      prop.writeToObject(object);
    } catch (RuntimeException e) {
      e.printStackTrace();
      /*if (e.getCause() instanceof PropertyVetoException) {
        //UIManager.getLookAndFeel().provideErrorFeedback(sheet);
        prop.setValue(event.getOldValue());
      } else {
        throw e;
      }*/
    }
  }
}
