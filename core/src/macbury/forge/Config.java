package macbury.forge;

import com.badlogic.gdx.Gdx;

/**
 * Created by macbury on 18.10.14.
 */
public class Config {


  public int depthMapSize = 2048;

  public enum RenderDebug {
    Textured, Wireframe, Normals, Lighting
  }
  public boolean generateWireframe = false;
  public boolean debug             = false;
  public RenderDebug renderDebug   = RenderDebug.Textured;
  public boolean renderDynamicOctree = false;
  public boolean renderStaticOctree = false;
  public boolean renderBoundingBox = false;
  public boolean cacheGeometry      = false;
  public boolean renderBulletDebug = false;
  public boolean buildColliders    = true;
  public void setRenderDebugTo(RenderDebug debug) {
    renderDebug = debug;
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        ForgE.shaders.reload();
      }
    });

  }
}
