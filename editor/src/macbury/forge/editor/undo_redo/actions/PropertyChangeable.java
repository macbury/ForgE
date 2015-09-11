package macbury.forge.editor.undo_redo.actions;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.l2fprod.common.propertysheet.Property;
import macbury.forge.ForgE;
import macbury.forge.editor.controllers.tools.events.EventsController;
import macbury.forge.editor.controllers.tools.inspector.InspectorController;
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
  private final EventsController inspector;

  public PropertyChangeable(Object object, PropertyChangeEvent event, EventsController inspectorController) {
    this.object = object;
    this.event  = event;
    this.inspector = inspectorController;
    this.prop     = (Property) event.getSource();
    Kryo kryo = new Kryo();
    this.oldValue = kryo.copy(event.getOldValue());
    this.newValue = kryo.copy(event.getNewValue());
    ForgE.log(TAG, "New value: " + newValue);
    ForgE.log(TAG, "Old value: " + oldValue);
  }

  @Override
  public void revert() {
    inspector.stopListeningForPropertyChanges();
    try {
      ForgE.log(TAG, "Revert from " + newValue + " to " + oldValue + " for " + object);
      prop.setValue(oldValue);
      prop.writeToObject(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
    inspector.startListeningForPropertyChanges();
  }

  @Override
  public void apply() {
    inspector.stopListeningForPropertyChanges();
    try {
      ForgE.log(TAG, "Apply " + newValue + " from " + oldValue +  " for " + object);
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
    inspector.startListeningForPropertyChanges();
  }

  @Override
  public void dispose() {

  }
}
