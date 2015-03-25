package macbury.forge.editor.controllers.tools.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import javafx.scene.control.ComboBox;
import macbury.forge.editor.undo_redo.actions.ApplyCustomBrushChangeable;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by macbury on 17.03.15.
 */
public class BrushTypeModel implements ComboBoxModel<ApplyCustomBrushChangeable.BrushType> {
  private static final String TAG = "BrushTypeModel";
  private final Array<ApplyCustomBrushChangeable.BrushType> brushes;
  private ApplyCustomBrushChangeable.BrushType selectedBrushType;

  public BrushTypeModel() {
    this.brushes = new Array<ApplyCustomBrushChangeable.BrushType>();
  }

  public void reload() {
    for (ApplyCustomBrushChangeable.BrushType brush : brushes) {
      brush.dispose();
    }
    brushes.clear();

    FileHandle[] brushFiles = Gdx.files.internal("ed/brushes").list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });

    for (FileHandle brushFile : brushFiles) {
      brushes.add(new ApplyCustomBrushChangeable.BrushType(brushFile));
    }

    selectedBrushType = brushes.get(0);
  }

  @Override
  public void setSelectedItem(Object anItem) {
    selectedBrushType = (ApplyCustomBrushChangeable.BrushType) anItem;
  }

  @Override
  public Object getSelectedItem() {
    return selectedBrushType;
  }

  @Override
  public int getSize() {
    return brushes.size;
  }

  @Override
  public ApplyCustomBrushChangeable.BrushType getElementAt(int index) {
    return brushes.get(index);
  }

  @Override
  public void addListDataListener(ListDataListener l) {

  }

  @Override
  public void removeListDataListener(ListDataListener l) {

  }
}
