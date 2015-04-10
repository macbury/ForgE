package macbury.forge.editor.views.property_editor.editors;


import org.apache.commons.lang3.StringUtils;

/**
 * Created by macbury on 10.04.15.
 */
public abstract class AbstractPropertyValue {
  private String title;

  public AbstractPropertyValue() {

  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = StringUtils.join(
        StringUtils.splitByCharacterTypeCamelCase(StringUtils.capitalize(title)),
        ' '
    );
  }
}
