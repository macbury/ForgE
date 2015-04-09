package macbury.forge.editor.controllers.tools.inspector.properties;

import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.l2fprod.common.model.DefaultBeanInfoResolver;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

public class DefaultBeanBinder implements PropertyChangeListener {

  private Object object = null;
  private BeanInfo info = null;
  private PropertySheetPanel sheet = null;
  private PropertyChangeListener listener;

  public DefaultBeanBinder(Object object, PropertySheetPanel sheet) {
    this(object, sheet, new DefaultBeanInfoResolver().getBeanInfo(object));
  }

  public DefaultBeanBinder(Object object, PropertySheetPanel sheet, BeanInfo info) {
    if (info == null) {
      throw new IllegalArgumentException(String.format("Cannot find %s for %s", BeanInfo.class.getSimpleName(), object.getClass()));
    }

    this.object = object;
    this.sheet = sheet;
    this.info = info;

    bind();
  }

  public void bind() {
    sheet.setProperties(info.getPropertyDescriptors());
    sheet.readFromObject(object);
    sheet.addPropertySheetChangeListener(this);
  }

  public void unbind() {
    sheet.removePropertyChangeListener(this);
    sheet.setProperties(new Property[0]);
    listener = null;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (listener != null) {
      listener.onPropertyChange(this, event, object);
    }
  }

  public void setListener(PropertyChangeListener listener) {
    this.listener = listener;
  }

  public PropertyChangeListener getListener() {
    return listener;
  }

  public interface PropertyChangeListener {
    public void onPropertyChange(DefaultBeanBinder binder, PropertyChangeEvent event, Object object);
  }
}