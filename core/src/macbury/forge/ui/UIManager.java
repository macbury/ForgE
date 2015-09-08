package macbury.forge.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ui.console.GameConsoleView;

/**
 * Created by macbury on 03.09.15.
 */
public class UIManager extends Stage {
  public static final String STORE_PATH = "graphics/ui/";
  public final Skin skin;
  public final GameConsoleView console;

  public UIManager() {
    super(new ScreenViewport());

    this.skin = new Skin(new TextureAtlas(ForgE.files.internal("ui:ui.atlas")));
    loadFonts();
    skin.load(ForgE.files.internal("ui:ui.json"));
    this.console = new GameConsoleView(this);


  }

  public WidgetGroup container() {
    return new WidgetGroup();
  }

  public Table table() {
    Table table = new Table();
    table.setDebug(ForgE.config.getBool(Config.Key.Debug));
    table.setFillParent(true);
    return table;
  }

  public Label label(String text, String style) {
    return new Label(text, skin, style);
  }

  public Button button(String text, String style) {
    return new TextButton(text, skin, style);
  }

  private void loadFonts() {
    FreeTypeFontGenerator generator                       = new FreeTypeFontGenerator(ForgE.files.internal("ui:advocut-webfont.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 20;
    BitmapFont defaultFont = generator.generateFont(parameter);
    skin.add("default", defaultFont);
    generator.dispose();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
      console.toggle();
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    skin.dispose();
  }
}
