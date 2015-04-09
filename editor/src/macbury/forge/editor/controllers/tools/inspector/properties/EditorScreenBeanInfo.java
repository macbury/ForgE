package macbury.forge.editor.controllers.tools.inspector.properties;

import com.badlogic.gdx.graphics.Color;
import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import macbury.forge.editor.screens.EditorScreen;

/**
 * Created by macbury on 15.03.15.
 */
public class EditorScreenBeanInfo extends BaseBeanInfo {
  private static final String CATEGORY_LEVEL = "Map";
  private static final String CATEGORY_LIGHTING = "Lighting";
  private static final String CATEGORY_WIND = "Wind";
  public EditorScreenBeanInfo() {
    super(EditorScreenBean.class);

    ExtendedPropertyDescriptor tilesetProperty = addProperty("title").setCategory(CATEGORY_LEVEL);
    tilesetProperty.setDisplayName("Title");
    tilesetProperty.setShortDescription("Map title for editor");

    ExtendedPropertyDescriptor fogProperty = addProperty("fogColor").setCategory(CATEGORY_LEVEL);
    fogProperty.setDisplayName("Fog & sky color");
    fogProperty.setShortDescription("Color of the fog and sky");

    ExtendedPropertyDescriptor ambientLightColorProperty = addProperty("ambientLight").setCategory(CATEGORY_LIGHTING);
    ambientLightColorProperty.setDisplayName("Ambient Light Color");
    ambientLightColorProperty.setShortDescription("color for ambient light");

    ExtendedPropertyDescriptor sunLightColor = addProperty("sunLightColor").setCategory(CATEGORY_LIGHTING);
    sunLightColor.setDisplayName("Sun Light Color");
    sunLightColor.setShortDescription("color for sun light");

    ExtendedPropertyDescriptor foliageSpeed = addProperty("windSpeed").setCategory(CATEGORY_WIND);
    foliageSpeed.setDisplayName("Speed");
    foliageSpeed.setShortDescription("Wind speed and direction");
  }

  public static class EditorScreenBean {
    private final EditorScreen screen;

    public EditorScreenBean(EditorScreen screen) {
      this.screen = screen;
    }

    public Color getFogColor() {
      return screen.level.env.skyColor;
    }

    public void setFogColor(Color newColor) {
      screen.level.env.skyColor.set(newColor);
    }

    public String getTitle() {
      return screen.level.state.getName();
    }

    public void setTitle(String title) {
      screen.level.state.setName(title);
    }

    public Color getSunLightColor() {
      return screen.level.env.mainLight.color;
    }

    public void setSunLightColor(Color hexColor) {
      screen.level.env.mainLight.color.set(hexColor);
    }

    public Color getAmbientLight() {
      return screen.level.env.ambientLight;
    }

    public void setAmbientLight(Color hexColor) {
      screen.level.env.ambientLight.set(hexColor);
    }

    public String getWindSpeed() {
      return screen.level.env.windDirection.toString();
    }

    public void setWindSpeed() {

    }
  }
}
