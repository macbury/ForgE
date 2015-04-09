package icons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import javax.swing.*;
/**
 * Created by macbury on 10.11.14.
 */
public  class Utils {
  private static final String TAG = "utils";

  public static ImageIcon getIcon(String name) {
    try {
      return new ImageIcon(Utils.class.getResource("./"+name+".png"));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static java.awt.Color fromLibgdx(Color c) {
    float [] hsbvals = java.awt.Color.RGBtoHSB((int)(255*c.r), (int)(255*c.g), (int)(255*c.b), null);
    return java.awt.Color.getHSBColor(hsbvals[0],hsbvals[1],hsbvals[2]);
  }

  public static Color fromAwt(java.awt.Color tempColor) {
    return new com.badlogic.gdx.graphics.Color(((float)tempColor.getRed())/255f,((float)tempColor.getGreen())/255f,((float)tempColor.getBlue())/255f,1f);
  }

  public static String rgbToString(float r, float g, float b) {
    String rs = Integer.toHexString((int)(r * 256));
    String gs = Integer.toHexString((int)(g * 256));
    String bs = Integer.toHexString((int)(b * 256));
    return rs + gs + bs;
  }
}
