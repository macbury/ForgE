package macbury.forge.graphics.fbo;

/**
 * Created by macbury on 20.07.15.
 */
public class Fbo {
  // For shadowmap
  public final static String FRAMEBUFFER_SUN_FAR_DEPTH        = "forge:sun-far";
  public final static String FRAMEBUFFER_SUN_NEAR_DEPTH       = "forge:sun-near";

  // For reflection
  public static final String FRAMEBUFFER_REFLECTIONS          = "forge:reflections";
  public static final String FRAMEBUFFER_REFRACTIONS          = "forge:refractions";

  // For god rays
  public static final String FRAMEBUFFER_SUN                  = "forge:sun";
  public static final String FRAMEBUFFER_DEPTH                = "forge:depth";

  // Final color buffers
  public final static String FRAMEBUFFER_MAIN_COLOR           = "forge:main-color";
  public static final String FRAMEBUFFER_FINAL                = "forge:final-color";
}
