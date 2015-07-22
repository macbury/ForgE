package macbury.forge.editor.controllers.tools.inspector.properties;

import com.badlogic.gdx.graphics.Color;
import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import macbury.forge.editor.controllers.tools.inspector.editors.SpinnerPropertyEditor;
import macbury.forge.editor.screens.LevelEditorScreen;

import javax.swing.*;

/**
 * Created by macbury on 15.03.15.
 */
public class EditorScreenBeanInfo extends BaseBeanInfo {
  private static final String CATEGORY_LEVEL = "Map";
  private static final String CATEGORY_LIGHTING = "Lighting";
  private static final String CATEGORY_WIND = "Wind";
  private static final String CATEGORY_WATER = "Water";

  public EditorScreenBeanInfo() {
    super(EditorScreenBean.class);

    createLevelOptions();
    createLightingOptions();
    createWindOptions();
    createWaterOptions();
  }

  private void createWindOptions() {
    ExtendedPropertyDescriptor foliageSpeed = addProperty("windSpeed").setCategory(CATEGORY_WIND);
    foliageSpeed.setDisplayName("Speed");
    foliageSpeed.setShortDescription("Wind speed and direction");
  }

  private void createLightingOptions() {
    ExtendedPropertyDescriptor ambientLightColorProperty = addProperty("ambientLight").setCategory(CATEGORY_LIGHTING);
    ambientLightColorProperty.setDisplayName("Ambient Light Color");
    ambientLightColorProperty.setShortDescription("color for ambient light");

    ExtendedPropertyDescriptor sunLightColor = addProperty("sunLightColor").setCategory(CATEGORY_LIGHTING);
    sunLightColor.setDisplayName("Sun Light Color");
    sunLightColor.setShortDescription("color for sun light");
  }

  private void createLevelOptions() {
    ExtendedPropertyDescriptor tilesetProperty = addProperty("title").setCategory(CATEGORY_LEVEL);
    tilesetProperty.setDisplayName("Title");
    tilesetProperty.setShortDescription("Map title for editor");

    ExtendedPropertyDescriptor fogProperty = addProperty("fogColor").setCategory(CATEGORY_LEVEL);
    fogProperty.setDisplayName("Fog & sky color");
    fogProperty.setShortDescription("Color of the fog and sky");
  }

  private void createWaterOptions() {
    ExtendedPropertyDescriptor waterColorProperty = addProperty("waterColor").setCategory(CATEGORY_WATER);
    waterColorProperty.setDisplayName("Color");
    waterColorProperty.setShortDescription("color for water");

    ExtendedPropertyDescriptor waterTintProperty = addProperty("waterTint").setCategory(CATEGORY_WATER);
    waterTintProperty.setDisplayName("Tint");

    ExtendedPropertyDescriptor waterWaveStrengthProperty = addProperty("waterWaveStrength").setCategory(CATEGORY_WATER);
    waterWaveStrengthProperty.setDisplayName("Wave strength");
    waterWaveStrengthProperty.setShortDescription("Water wave strength");

    ExtendedPropertyDescriptor waterElevationProperty = addProperty("waterElevation").setCategory(CATEGORY_WATER);
    waterElevationProperty.setDisplayName("Elevation");
    waterElevationProperty.setShortDescription("Elevation for reflection and refraction, only one for whole level");

    ExtendedPropertyDescriptor waterDisplacementTilingProperty = addProperty("waterDisplacementTiling").setCategory(CATEGORY_WATER);
    waterDisplacementTilingProperty.setDisplayName("Displacement tiling");

    ExtendedPropertyDescriptor waterSpeedProperty = addProperty("waterSpeed").setCategory(CATEGORY_WATER);
    waterSpeedProperty.setDisplayName("Speed");

    ExtendedPropertyDescriptor waterRefractiveFactorProperty = addProperty("waterRefractiveFactor").setCategory(CATEGORY_WATER);
    waterRefractiveFactorProperty.setDisplayName("Refraction factor");
  }

  public static class EditorScreenBean {
    private final LevelEditorScreen screen;

    public EditorScreenBean(LevelEditorScreen screen) {
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

    public void setWaterColor(Color newColor) {
      screen.level.env.water.color = newColor;
    }

    public Color getWaterColor() {
      return screen.level.env.water.color;
    }

    public void setWaterWaveStrength(int waveStrenght) {
      screen.level.env.water.waveStrength = (float)waveStrenght / 1000f;
    }

    public int getWaterWaveStrength() {
      return (int)(1000f * screen.level.env.water.waveStrength);
    }

    public void setWaterElevation(float waterElevation) {
      screen.level.env.water.elevation = waterElevation;
    }

    public float getWaterElevation() {
      return screen.level.env.water.elevation;
    }

    public void setWaterDisplacementTiling(int tiling) {
      screen.level.env.water.displacementTiling = (float)tiling / 100f;
    }

    public int getWaterDisplacementTiling() {
      return (int)(screen.level.env.water.displacementTiling * 100);
    }

    public void setWaterTint(int colorTint) {
      screen.level.env.water.colorTint = (float)colorTint / 100f;
    }

    public int getWaterTint() {
      return (int)(screen.level.env.water.colorTint * 100);
    }

    public void setWaterSpeed(int speed) {
      screen.level.env.water.waterSpeed = (float)speed / 100f;
    }

    public int getWaterSpeed() {
      return (int)(screen.level.env.water.waterSpeed * 100);
    }

    public void setWaterRefractiveFactor(int refractiveFactor) {
      screen.level.env.water.refractiveFactor = (float)refractiveFactor / 100f;
    }

    public int getWaterRefractiveFactor() {
      return (int)(screen.level.env.water.refractiveFactor * 100);
    }
  }
}
