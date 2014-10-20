package macbury.forge.editor.controllers;

import macbury.forge.ForgE;
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

}
