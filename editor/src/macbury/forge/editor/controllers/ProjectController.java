package macbury.forge.editor.controllers;

import macbury.forge.ForgE;
import macbury.forge.editor.runnables.UpdateStatusBar;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.graphics.batch.VoxelBatch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by macbury on 18.10.14.
 */
public class ProjectController implements ActionListener {
  private MainWindow mainWindow;
  public EditorScreen editorScreen;
  private JButton wireframeButton;
  private JButton textureButton;
  private MainMenu mainMenu;

  public void setMainWindow(MainWindow mainWindow) {
    this.mainWindow = mainWindow;
  }

  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public void newMap() {
    mainWindow.setTitle("[ForgE] - New project");
    this.editorScreen = new EditorScreen();
    ForgE.screens.set(editorScreen);
    refresh();
  }

  private void refresh() {
    mainMenu.debugRenderDynamicOctree.setState(ForgE.config.renderDynamicOctree);
    mainMenu.debugBoundingBox.setState(ForgE.config.renderBoundingBox);
    mainMenu.debugRenderStaticOctree.setState(ForgE.config.renderStaticOctree);
  }

  public void setWireframeButton(JButton wireframeButton) {
    this.wireframeButton = wireframeButton;
    wireframeButton.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == wireframeButton) {
      editorScreen.level.setRenderType(VoxelBatch.RenderType.Wireframe);
      //editorScreen.batch.setType(VoxelBatch.RenderType.Wireframe);
    } else {
      editorScreen.level.setRenderType(VoxelBatch.RenderType.Normal);
      //editorScreen.batch.setType(VoxelBatch.RenderType.Normal);
    }
  }

  public void setTextureButton(JButton textureButton) {
    this.textureButton = textureButton;
    this.textureButton.addActionListener(this);
  }

  public void setStatusLabel(JLabel statusLabel, JLabel statusMemoryLabel, JLabel statusRenderablesLabel) {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(new UpdateStatusBar(this, statusLabel, statusMemoryLabel, statusRenderablesLabel), 0, 1, TimeUnit.SECONDS);
  }

  public void setMainMenu(MainMenu mainMenu) {
    this.mainMenu = mainMenu;

    mainMenu.debugRenderDynamicOctree.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderDynamicOctree = ProjectController.this.mainMenu.debugRenderDynamicOctree.getState();
      }
    });

    mainMenu.debugRenderStaticOctree.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderStaticOctree = ProjectController.this.mainMenu.debugRenderStaticOctree.getState();
      }
    });

    mainMenu.debugBoundingBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderBoundingBox = ProjectController.this.mainMenu.debugBoundingBox.getState();
      }
    });
  }

}
