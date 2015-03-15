package macbury.forge.editor.controllers.inspector.properties;

import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import macbury.forge.editor.screens.EditorScreen;

/**
 * Created by macbury on 15.03.15.
 */
public class EditorScreenBeanInfo extends BaseBeanInfo {
  private static final String CATEGORY_LEVEL = "Map";

  public EditorScreenBeanInfo() {
    super(EditorScreenBean.class);

    ExtendedPropertyDescriptor tilesetProperty = addProperty("title").setCategory(CATEGORY_LEVEL);
    tilesetProperty.setDisplayName("Title");
    tilesetProperty.setShortDescription("Map title for editor");
    //tilesetProperty.setPropertyEditorClass(TilesetEditor.class);
  }

  public static class EditorScreenBean {
    private final EditorScreen screen;

    public EditorScreenBean(EditorScreen screen) {
      this.screen = screen;
    }

    public String getTitle() {
      return screen.level.state.getName();
    }

    public void setTitle(String title) {
      screen.level.state.setName(title);
    }
  }
}
