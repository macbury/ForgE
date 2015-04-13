package macbury.forge.editor.windows;

import com.badlogic.gdx.math.Vector3;
import macbury.forge.editor.views.property_editor.PropertyTableEditor;
import macbury.forge.editor.views.property_editor.PropertyTableForObject;

import javax.swing.*;

public class PropertyTableTest extends JFrame {

  public PropertyTableTest() {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }

      // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    PropertyTableEditor editor = new PropertyTableEditor();
    editor.bind("Object 1", new ExampleObjectToInspect());
    editor.bind("Object 2", new ExampleObjectToInspect());
    editor.bind("Object 3", new ExampleObjectToInspect());
    //PropertyTableForObject table = new PropertyTableForObject();

    //table.bind(new StringPropertyValue("Test title"));
    //table.addToInspector(new ExampleObjectToInspect());

    setContentPane(editor);
    setSize(320, 480);
    setVisible(true);
  }

  public static void main (String[] arg) {
    PropertyTableTest test = new PropertyTableTest();
    test.setVisible(true);
  }

  public class ExampleObjectToInspect {
    private String title = "test title";
    private int number;
    private float decimalNumber;
    private boolean yesOrNo;
    private Vector3 vector3;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public int getNumber() {
      return number;
    }

    public void setNumber(int number) {
      this.number = number;
    }

    public float getDecimalNumber() {
      return decimalNumber;
    }

    public void setDecimalNumber(float decimalNumber) {
      this.decimalNumber = decimalNumber;
    }

    public boolean isYesOrNo() {
      return yesOrNo;
    }

    public void setYesOrNo(boolean yesOrNo) {
      this.yesOrNo = yesOrNo;
    }

    public Vector3 getVector3() {
      return vector3;
    }

    public void setVector3(Vector3 vector3) {
      this.vector3 = vector3;
    }
  }
}
