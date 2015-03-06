package macbury.forge.editor.controllers;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by macbury on 06.03.15.
 */
public class LogController {
  private final JTextArea logArea;

  public LogController(JTextArea logArea) {
    this.logArea = logArea;
    PrintStream origOut     = System.out;
    PrintStream interceptor = new LogInterceptor(origOut);
    System.setOut(interceptor);
  }

  private class LogInterceptor extends PrintStream {
    public LogInterceptor(OutputStream out) {
      super(out, true);
    }
    @Override
    public void print(String s) {
      JTextArea area = LogController.this.logArea;
      try {
        area.getDocument().insertString(area.getDocument().getEndPosition().getOffset(),s+"\n", null);
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
      area.setCaretPosition(area.getDocument().getLength());
      super.print(s);
    }
  }
}
