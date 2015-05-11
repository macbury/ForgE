package macbury.forge.editor.controllers.tools.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import macbury.forge.editor.controllers.tools.terrain.properties.CustomBrushChangeableProvider;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by macbury on 17.03.15.
 */
public class BrushTypeModel implements ComboBoxModel<CustomBrushChangeableProvider.BrushType> {
  private static final String TAG = "BrushTypeModel";
  private final Array<CustomBrushChangeableProvider.BrushType> brushes;
  private CustomBrushChangeableProvider.BrushType selectedBrushType;

  public BrushTypeModel() {
    this.brushes = new Array<CustomBrushChangeableProvider.BrushType>();
  }

  public void reload() {
    for (CustomBrushChangeableProvider.BrushType brush : brushes) {
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
      brushes.add(new CustomBrushChangeableProvider.BrushType(brushFile));
    }

    selectedBrushType = brushes.get(0);
  }

  @Override
  public void setSelectedItem(Object anItem) {
    selectedBrushType = (CustomBrushChangeableProvider.BrushType) anItem;
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
  public CustomBrushChangeableProvider.BrushType getElementAt(int index) {
    return brushes.get(index);
  }

  @Override
  public void addListDataListener(ListDataListener l) {

  }

  @Override
  public void removeListDataListener(ListDataListener l) {

  }
}
