package macbury.forge.editor.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import macbury.forge.Config;
import macbury.forge.ForgE;
import macbury.forge.ForgEBootListener;
import macbury.forge.editor.controllers.ProjectController;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.utils.FormatUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainWindow extends JFrame implements ForgEBootListener {
  private final LwjglAWTCanvas openGLCanvas;
  private final ForgE engine;
  private final ProjectController projectController;
  private final MainMenu mainMenu;
  private JPanel contentPane;
  private JButton wireframeButton;
  private JPanel openGlContainer;
  private JButton texturedButton;
  private JPanel statusBarPanel;
  private JLabel statusLabel;
  private JTabbedPane tabbedPane1;

  public MainWindow() {
    setContentPane(contentPane);
    setSize(1360, 768);
    setVisible(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    Config config            = new Config();
    config.generateWireframe = true;
    config.renderBoundingBox = true;
    engine                   = new ForgE(config);

    engine.setBootListener(this);

    mainMenu     = new MainMenu();

    openGLCanvas = new LwjglAWTCanvas(engine);
    openGlContainer.add(openGLCanvas.getCanvas(), BorderLayout.CENTER);

    projectController = new ProjectController();
    projectController.setMainWindow(this);
    projectController.setWireframeButton(wireframeButton);
    projectController.setTextureButton(texturedButton);
    setJMenuBar(mainMenu);

    pack();
  }

  @Override
  public void afterEngineCreate(ForgE engine) {
    projectController.newMap();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(updateStatusInfo, 0, 1, TimeUnit.SECONDS);
  }

  private Runnable updateStatusInfo = new Runnable() {
    public void run() {
      statusLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + " Native: " + FormatUtils.humanReadableByteCount(Gdx.app.getNativeHeap(), true) + " Java: " + FormatUtils.humanReadableByteCount(Gdx.app.getJavaHeap(), true));
    }
  };
}
