package macbury.forge.editor.views.property_editor;

import com.badlogic.gdx.utils.Array;
import macbury.forge.editor.windows.PropertyTableTest;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by macbury on 10.04.15.
 */
public class PropertyTableEditor extends JScrollPane {
  private final JPanel root;
  private HashMap<Object, PropertyTableGroup> panels;
  public PropertyTableEditor() {
    super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
    panels = new HashMap<Object, PropertyTableGroup>();
    root = new JPanel();
    root.setLayout(new BoxLayout(root, BoxLayout.PAGE_AXIS));
    this.setAlignmentY(TOP_ALIGNMENT);
    getViewport().setView(root);
  }

  public void bind(String groupTitle, Object objectToInspect) {
    PropertyTableGroup tableGroup = new PropertyTableGroup(groupTitle, new PropertyTableForObject(objectToInspect));
    panels.put(objectToInspect, tableGroup);
    root.add(panels.get(objectToInspect));
  }

  private class PropertyTableGroup extends JPanel {

    private final JLabel titleLabel;

    public PropertyTableGroup(String title, PropertyTableForObject propertyTableForObject) {
      super();
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

      titleLabel = new JLabel(title);
      titleLabel.setLabelFor(propertyTableForObject);
      Border paddingBorder = BorderFactory.createEmptyBorder(10,10,5,5);
      titleLabel.setBorder(paddingBorder);
      titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


      JPanel pan = new JPanel(new BorderLayout());
      pan.add(titleLabel);
      Dimension max = pan.getMaximumSize();
      max.height = 30;
      pan.setMaximumSize(max);
      add(pan);
      add(propertyTableForObject);

    }
  }
}
