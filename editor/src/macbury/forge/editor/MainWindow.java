package macbury.forge.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.screens.test.TestMeshScreen;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements ForgEBootListener {
  private final LwjglAWTCanvas openGLCanvas;
  private final ForgE engine;
  private JPanel contentPane;
  private JButton button1;
  private JPanel openGlContainer;
  private JTree tree1;

  public MainWindow() {
    setContentPane(contentPane);
    pack();
    setSize(1360, 768);
    setVisible(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    engine       = new ForgE();

    engine.setBootListener(this);

    openGLCanvas = new LwjglAWTCanvas(engine);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    setTitle("[ForgE] - New project");
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    engine.screens.set(new TestMeshScreen());
  }
}
