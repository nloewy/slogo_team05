package slogo.view.pages;

import java.io.File;
import javafx.scene.Group;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public abstract class Screen {

  public static final FileChooser IMAGE_CHOOSER = makeImageChooser("png");

  private static FileChooser makeImageChooser(String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setTitle("Select Turtle");
    result.setInitialDirectory(new File("src/main/resources/turtleimages/"));

    result.getExtensionFilters()
        .setAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    return result;
  }

  public abstract void setUp();

}

