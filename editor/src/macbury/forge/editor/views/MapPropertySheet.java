package macbury.forge.editor.views;

import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

import java.awt.*;

/**
 * Created by macbury on 06.11.14.
 */
public class MapPropertySheet extends PropertySheetPanel {

  public MapPropertySheet() {
    super();

    this.setSortingProperties(true);
    this.setSortingCategories(true);
    this.setMode(PropertySheet.VIEW_AS_CATEGORIES);
    this.setBackground(Color.WHITE);
  }
}
