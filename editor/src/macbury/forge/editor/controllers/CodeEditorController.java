package macbury.forge.editor.controllers;

import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.windows.CodeEditorWindow;
import macbury.forge.editor.windows.MainWindow;

/**
 * Created by macbury on 13.05.15.
 */
public class CodeEditorController implements ForgEBootListener {
  private final CodeEditorWindow codeEditorWindow;

  public CodeEditorController(CodeEditorWindow codeEditorWindow) {
    this.codeEditorWindow = codeEditorWindow;
    codeEditorWindow.setSize(1280, 800);
  }

  public void show() {
    codeEditorWindow.setVisible(true);
    codeEditorWindow.setLocationRelativeTo(MainWindow.current);
  }

  @Override
  public void afterEngineCreate(ForgE engine) {

  }
}
