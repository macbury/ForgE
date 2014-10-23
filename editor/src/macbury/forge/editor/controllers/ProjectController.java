package macbury.forge.editor.controllers;

import macbury.forge.ForgE;
import macbury.forge.editor.views.MainMenu;
import macbury.forge.editor.windows.MainWindow;
import macbury.forge.editor.screens.EditorScreen;
import macbury.forge.graphics.batch.VoxelBatch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 18.10.14.
 */
public class ProjectController implements ActionListener {
  private MainWindow mainWindow;
  private EditorScreen editorScreen;
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
    mainMenu.debugShowOctree.setState(ForgE.config.renderOctree);
    mainMenu.debugBoundingBox.setState(ForgE.config.renderBoundingBox);
  }

  public void setWireframeButton(JButton wireframeButton) {
    this.wireframeButton = wireframeButton;
    wireframeButton.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == wireframeButton) {
      editorScreen.getLevel().setRenderType(VoxelBatch.RenderType.Wireframe);
      //editorScreen.batch.setType(VoxelBatch.RenderType.Wireframe);
    } else {
      editorScreen.getLevel().setRenderType(VoxelBatch.RenderType.Normal);
      //editorScreen.batch.setType(VoxelBatch.RenderType.Normal);
    }
  }

  public void setTextureButton(JButton textureButton) {
    this.textureButton = textureButton;
    this.textureButton.addActionListener(this);
  }

  public void setMainMenu(MainMenu mainMenu) {
    this.mainMenu = mainMenu;

    mainMenu.debugShowOctree.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ForgE.config.renderOctree = ProjectController.this.mainMenu.debugShowOctree.getState();
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
