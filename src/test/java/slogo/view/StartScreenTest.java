package slogo.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class StartScreenTest extends DukeApplicationTest {

  private ComboBox languageBox;
  private ComboBox themeBox;
  private Button loadGen;
  private Pane root;

  @Override
  public void start(Stage stage) throws FileNotFoundException {
    stage.setTitle("SLogo");
    try {
      Controller controller = new Controller(stage);
    } catch (IOException e) {
      e.printStackTrace();
    }

    languageBox = lookup("#languageBox").query();
    themeBox = lookup("#themeBox").query();
    loadGen = lookup("#LoadGen").query();
    root = lookup("#startScreen").query();
  }

  @Test
  void testLanguageBoxDefault() {
    clickOn(languageBox);
    assertEquals("English", languageBox.getValue());
    assertEquals("Load New General Session", loadGen.getText());
  }

  @Test
  void testLanguageBoxChangeFrench() {
    clickOn(languageBox);
    clickOn("Français");
    assertEquals("Français", languageBox.getValue());
    assertEquals("Nouvelle Generale", loadGen.getText());
  }

  @Test
  void testLanguageBoxChangeSpanish() {
    clickOn(languageBox);
    clickOn("Español");
    assertEquals("Español", languageBox.getValue());
    assertEquals("Nueva session general", loadGen.getText());
  }

  @Test
  void testThemeBoxDefault() {
    clickOn(themeBox);
    assertEquals("Light Mode", themeBox.getValue().toString());
    assertEquals(Color.WHITE, root.getBackground().getFills().get(0).getFill());
  }

  @Test
  void testThemeBoxChangeDark() {
    clickOn(themeBox);
    clickOn("Dark Mode");
    assertEquals("Dark Mode", themeBox.getValue().toString());
    assertEquals(Color.BLACK, root.getBackground().getFills().get(0).getFill());
  }
}
