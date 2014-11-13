package macbury.forge.graphics.mesh;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by macbury on 13.11.14.
 */
public class Material extends Color {

  public void setAO(float ao) {
    this.r = ao;
  }

  public void setSpecular(float specular) {
    this.g = specular;
  }

  public void setTransparent(boolean transparent) {
    this.b = transparent ? 1 : 0;
  }

  public void reset() {
    this.set(Color.BLACK);
  }

}
