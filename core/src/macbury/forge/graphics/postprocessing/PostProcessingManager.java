package macbury.forge.graphics.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.*;
import macbury.forge.ForgE;
import macbury.forge.graphics.postprocessing.model.FrameBufferFactory;
import macbury.forge.graphics.postprocessing.model.PostProcessingFactory;
import macbury.forge.graphics.postprocessing.model.PostProcessingStepFactory;
import macbury.forge.graphics.postprocessing.steps.PostProcessingStep;
import macbury.forge.level.env.LevelEnv;


/**
 * Created by macbury on 26.08.15.
 */
public class PostProcessingManager implements Disposable {
  private static final String TAG = "PostProcessingManager";
  private FileHandle fileHandle;
  private Array<PostProcessingStep> steps;
  private ObjectMap<String, FrameBuffer> frameBuffers;
  private PostProcessingFactory factory;

  //public PostProcessingManager() {

    //this.effects.add(new PostProcessBloomImage());
   // this.effects.add(new PostProcessFinalImage());
  //}

  public PostProcessingManager(FileHandle internal) {
    this.steps        = new Array<PostProcessingStep>();
    this.frameBuffers = new  ObjectMap<String, FrameBuffer>();
    this.fileHandle   = internal;
    reload();
  }

  public void reload() {
    Gdx.app.log(TAG, "Reloading...");
    unloadEffectsAndBuffers();

    Json json    = new Json();
    this.factory = json.fromJson(PostProcessingFactory.class, fileHandle.readString());

    if (factory.buffers != null) {
      Gdx.app.log(TAG, "Found buffers to create:");
      for (FrameBufferFactory frameBufferFactory : factory.buffers) {
        frameBuffers.put(frameBufferFactory.name, frameBufferFactory.build());
      }
    }

    if (factory.steps != null) {
      Gdx.app.log(TAG, "Found steps to create:");
      for (PostProcessingStepFactory stepFactory : factory.steps) {
        steps.add(stepFactory.build(this));
      }
    }

    factory = null;
  }

  public void render(RenderContext renderContext, LevelEnv env) {
    for (int i = 0; i < steps.size; i++) {
      steps.get(i).run(renderContext, env);
    }
  }

  @Override
  public void dispose() {
    unloadEffectsAndBuffers();
    frameBuffers = null;
    steps = null;
    fileHandle = null;
    factory = null;
  }

  private void unloadEffectsAndBuffers() {
    Gdx.app.log(TAG, "Unloading framebuffers and effects...");
    for (String frameBufferName : frameBuffers.keys()) {
      ForgE.fb.destroy(frameBufferName);
    }
    for (PostProcessingStep step : steps) {
      step.dispose();
    }
    frameBuffers.clear();
    steps.clear();
  }
}
