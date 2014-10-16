package macbury.forge.assets;

/**
 * Created by macbury on 16.10.14.
 */
public class ShaderLoader {
  private String fragment;
  private String vertex;

  public String getFragment() {
    return fragment+".frag.glsl";
  }

  public String getVertex() {
    return vertex+".vert.glsl";
  }
}
