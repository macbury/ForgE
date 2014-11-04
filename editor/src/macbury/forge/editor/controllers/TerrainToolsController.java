package macbury.forge.editor.controllers;

import macbury.forge.editor.controllers.listeners.OnMapChangeListener;
import macbury.forge.editor.screens.EditorScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by macbury on 04.11.14.
 */
public class TerrainToolsController implements OnMapChangeListener, ActionListener {
  private final JToolBar toolbar;
  private final ButtonGroup toolsGroup;
  private final JToggleButton drawPencilButton;

  public TerrainToolsController(JToolBar terrainToolsToolbar) {
    toolbar = terrainToolsToolbar;

    this.toolsGroup = new ButtonGroup();

    drawPencilButton = buildToogleButton("draw_pencil", toolsGroup);
    buildToogleButton("draw_bucket", toolsGroup);
    buildToogleButton("draw_rect", toolsGroup);
    buildToogleButton("draw_airbrush", toolsGroup);
    buildToogleButton("draw_elipsis", toolsGroup);
    buildToogleButton("draw_eraser", toolsGroup);
  }

  private JToggleButton buildToogleButton(String iconName, ButtonGroup buttonGroup) {
    JToggleButton button = new JToggleButton();
    ImageIcon icon = new ImageIcon(getClass().getResource("/icons/"+iconName+".png"));
    button.setFocusable(false);
    button.setHorizontalTextPosition(SwingConstants.LEADING);
    button.setIcon(icon);
    toolbar.add(button);
    buttonGroup.add(button);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void onNewMap(ProjectController controller, EditorScreen screen) {
    drawPencilButton.setSelected(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
