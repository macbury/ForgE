package macbury.forge.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.scripts.script.ConsoleScriptRunner;
import macbury.forge.ui.UIManager;

/**
 * Created by macbury on 08.09.15.
 */
public class GameConsoleView extends Table {
  public static final String TAG = "GameConsoleView";
  private final Array<Label> labels;
  private Array<String> commandsHistory;
  private final Table logEntries;
  private final TextField input;
  private final UIManager ui;
  private boolean visible;
  private boolean cursorIsCatched;
  //private final ScrollPane scroll;


  public GameConsoleView(UIManager ui) {
    super();

    this.commandsHistory = new Array<String>();
    this.ui = ui;
    this.visible = false;
    setBackground(ui.skin.getDrawable("console_bg"));
    setDebug(false);
    pad(4);
    padTop(22);
    setHeight(Gdx.graphics.getHeight() * 0.3f);
    setFillParent(true);

    labels     = new Array<Label>();
    logEntries = new Table(ui.skin);

    input = new TextField("Sample input!", ui.skin, "console");
    this.setTouchable(Touchable.childrenOnly);
    input.addListener(new InputListener() {
      @Override
      public boolean keyUp(InputEvent event, int keycode) {
        if (keycode == Input.Keys.ENTER) {
          processCommand();
        }
        return super.keyUp(event, keycode);
      }
    });
/*
    scroll = new ScrollPane(logEntries, ui.skin);
    scroll.setFadeScrollBars(false);
    scroll.setScrollbarsOnTop(false);
    scroll.setOverscroll(false, false);
*/
    this.add(/*scroll*/).expand().fill().pad(4).row();

    this.add(input).expandX().fillX().pad(4);
    //this.addListener(new KeyListener(input));
  }

  private void processCommand() {
    String currentCommand      = input.getText();
    commandsHistory.add(currentCommand);
    ConsoleScriptRunner runner = new ConsoleScriptRunner(currentCommand);
    ForgE.scripts.run(runner);
    input.setText("");
  }

  public void toggle() {
    if (visible) {
      hide();
    } else {
      show();
    }
  }

  public boolean isEnabled() {
    return visible;
  }

  private void show() {
    this.cursorIsCatched = Gdx.input.isCursorCatched();
    Gdx.input.setCursorCatched(false);
    Gdx.app.log(TAG, "Show");
    visible = true;
    setColor(Color.BLACK);
    setZIndex(100);
    ui.addActor(this);
    input.setText("");
    ui.setKeyboardFocus(input);
  }

  public void hide() {
    Gdx.input.setCursorCatched(cursorIsCatched);
    Gdx.app.log(TAG, "hide");
    visible = false;
    remove();
  }
}
