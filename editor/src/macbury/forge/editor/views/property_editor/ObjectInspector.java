package macbury.forge.editor.views.property_editor;

import com.badlogic.gdx.utils.Array;
import macbury.forge.editor.views.property_editor.editors.AbstractPropertyValue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Created by macbury on 10.04.15.
 */
public class ObjectInspector {
  private final PropertyDescriptor[] properties;
  private Array<AbstractPropertyValue> valueEditors;

  public ObjectInspector(PropertyTableForObject propertyTable, Object toInspect) throws IntrospectionException {
    BeanInfo beaninfo = Introspector.getBeanInfo(toInspect.getClass());
    valueEditors      = new Array<AbstractPropertyValue>();
    properties        = beaninfo.getPropertyDescriptors();
    for (PropertyDescriptor propertyDescriptor : properties) {
      if (!propertyDescriptor.getDisplayName().equalsIgnoreCase("class")) {
        Class<? extends AbstractPropertyValue> editorClass = propertyTable.getEditorClassFor(propertyDescriptor.getPropertyType());
        try {
          AbstractPropertyValue editorInstance               = editorClass.newInstance();
          editorInstance.setTitle(propertyDescriptor.getName());
          valueEditors.add(editorInstance);
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }


    }
  }

  public void insertInto(Array<AbstractPropertyValue> out) {
    out.addAll(valueEditors);
  }
}
