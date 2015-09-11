package macbury.forge.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import macbury.forge.ForgE;
import macbury.forge.scripts.script.ConsoleScriptRunner;
import macbury.forge.ui.UIManager;

/**
 * Created by macbury on 08.09.15.
 */
public class GameConsoleView extends Table implements ForgE.LogListener {
  public final static int MAX_LOG_LINES = 100;
  public static final String TAG = "GameConsoleView";
  private final Array<Label> labels;
  private final ScrollPane scroll;
  private Array<String> commandsHistory;
  private final VerticalGroup logEntries;
  private final TextField input;
  private final UIManager ui;
  private boolean visible;
  private boolean cursorIsCatched;
  private int currentCommandIndex = 0;
  public GameConsoleView(UIManager ui) {
    super();

    this.commandsHistory = new Array<String>();
    this.ui = ui;
    this.visible = false;
    setBackground(ui.skin.getDrawable("console_bg"));
    setDebug(false);
    pad(4);
    setFillParent(true);

    labels     = new Array<Label>();
    logEntries = new VerticalGroup();

    input = new TextField("Sample input!", ui.skin, "console");
    this.setTouchable(Touchable.childrenOnly);
    input.addListener(new InputListener() {
      @Override
      public boolean keyUp(InputEvent event, int keycode) {
        if (keycode == Input.Keys.ENTER) {
          processCommand();
        } else if (keycode == Input.Keys.UP) {
          moveCommandUp();
        } else if (keycode == Input.Keys.DOWN) {
          moveCommandDown();
        }
        return super.keyUp(event, keycode);
      }
    });

    scroll = new ScrollPane(logEntries, ui.skin, "console");
    scroll.setFadeScrollBars(false);
    scroll.setScrollbarsOnTop(false);
    scroll.setOverscroll(false, false);

    this.add(scroll).left().expand().fill().pad(4).row();

    this.add(input).expandX().fillX().pad(4);
    //this.addListener(new KeyListener(input));
    ForgE.addLogListener(this);
  }

  private void moveCommandDown() {
    currentCommandIndex++;
    currentCommandIndex = Math.min(currentCommandIndex, commandsHistory.size - 1);
    showCommandForCurrentIndex();
  }

  private void showCommandForCurrentIndex() {
    if (currentCommandIndex > 0 && currentCommandIndex < commandsHistory.size) {
      String command = commandsHistory.get(currentCommandIndex);
      if (command != null) {
        input.setText(command);
        input.setCursorPosition(command.length());
      }
    }

  }

  private void moveCommandUp() {
    currentCommandIndex--;
    currentCommandIndex = Math.max(currentCommandIndex, 0);
    showCommandForCurrentIndex();
  }

  private void processCommand() {
    String currentCommand      = input.getText();
    commandsHistory.add(currentCommand);
    ConsoleScriptRunner runner = new ConsoleScriptRunner(currentCommand);
    ForgE.scripts.run(runner);
    input.setText("");
    currentCommandIndex        = commandsHistory.size;
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
    visible = true;
    setColor(Color.BLACK);
    setZIndex(100);
    ui.addActor(this);
    input.setText("");
    ui.setKeyboardFocus(input);
    ui.setScrollFocus(scroll);
  }

  public void hide() {
    Gdx.input.setCursorCatched(cursorIsCatched);
    visible = false;
    remove();
  }

  @Override
  public void onLogResult(String tag, String msg) {
    int diff = labels.size - MAX_LOG_LINES;
    if (diff > 0) {
      for (int i = 0; i < diff; i++) {
        labels.removeIndex(0).remove();
      }
    }
    Label label = new Label(msg, ui.skin, "console");
    logEntries.left().addActor(label);
    labels.add(label);

    scroll.validate();
    scroll.setScrollPercentY(1);
  }
}
