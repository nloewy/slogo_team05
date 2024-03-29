package slogo.view.pages;

import static slogo.view.UserInterfaceUtil.generateButton;
import static slogo.view.UserInterfaceUtil.generateComboBox;
import static slogo.view.UserInterfaceUtil.generateComboStringBox;
import static slogo.view.UserInterfaceUtil.generateImageView;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import slogo.view.ComboChoice;
import slogo.view.Controller;

/**
 * The StartScreen class is a page that is displayed when the application is first opened. It
 * provides options for the user to load a new Slogo session, load a new general session, upload
 * preferences, and change the language and theme of the application.
 */

public class StartScreen {

  private static final String DEFAULT_RESOURCE_PACKAGE = "slogo.languages.";
  private static final ResourceBundle LANG_OPT_BUNDLE = ResourceBundle.getBundle(
      DEFAULT_RESOURCE_PACKAGE + "LanguageOptions");
  private static final ObservableList<String> SUPPORTED_LANGUAGES =
      FXCollections.observableArrayList(
          LANG_OPT_BUNDLE.keySet().stream().map(LANG_OPT_BUNDLE::getString).toList());
  private static final String LOGO_IMAGE_PATH = "turtleimages/SlogoLOGO.png";
  private final Controller controller;
  private final Pane root = new Pane();
  private final Stage stage;
  private Scene scene;

  public StartScreen(Stage stage, Controller controller) {
    this.controller = controller;
    this.stage = stage;
    root.setId("startScreen");
  }


  public javafx.scene.Scene getScene() {
    return scene;
  }

  public void setUp() {
    ResourceBundle myResources = ResourceBundle.getBundle(
        DEFAULT_RESOURCE_PACKAGE + controller.getCurrentLanguage());
    Button loadSlogo = generateButton("LoadSlogo", 100, 300, e -> {
      controller.loadSession("new");
    });
    Button loadGen = generateButton("LoadGen", 100, 330, e -> {
      try {
        controller.openNewIdeSession(null);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });

    Button uploadPref = generateButton("uploadPreferences", 100, 360, e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files",
          "*.xml"));
      File selectedFile = fileChooser.showOpenDialog(stage);
      if (selectedFile != null) {
        controller.loadSettings(selectedFile, scene);
      }
    });
//    Button loadOld = generateButton("LoadOld", 100, 360, e -> controller.loadSession());

    ResourceBundle defaultResources = ResourceBundle.getBundle(
        DEFAULT_RESOURCE_PACKAGE + "English");
    ObservableList<ComboChoice> supportedThemes = FXCollections.observableArrayList();
    for (String theme : defaultResources.getString("ColorThemes").split(",")) {
      supportedThemes.add(new ComboChoice(theme, theme));
    }
    ComboBox<ComboChoice> themeComboBox = generateComboBox(supportedThemes, "themeBox",
        400, 330,
        (s) -> {
          return s.replace(" ", "") + ".css";
        }, (event) -> {
          controller.setCurrentTheme(event, scene);
        });

    controller.addLanguageObserver((s) -> {
      ResourceBundle newLang = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + s);
      loadSlogo.setText(newLang.getString("LoadSlogo"));
      loadGen.setText(newLang.getString("LoadGen"));
      uploadPref.setText(newLang.getString("uploadPreferences"));

      String[] themes = newLang.getString("ColorThemes").split(",");
      ObservableList<ComboChoice> comboBoxItems = themeComboBox.getItems();
      for (int i = 0; i < themes.length; i++) {
        comboBoxItems.set(i, new ComboChoice(themes[i], comboBoxItems.get(i).getValue()));
        if (comboBoxItems.get(i).getValue().equals(themeComboBox.getValue().toString())) {
          themeComboBox.setValue(comboBoxItems.get(i));
        }
      }
    });

    root.getChildren().addAll(
        generateImageView(LOGO_IMAGE_PATH, 100, 50),
        generateComboStringBox(SUPPORTED_LANGUAGES, "languageBox", 100, 200,
            (s) -> {
              return LANG_OPT_BUNDLE.keySet().stream()
                  .filter(key -> LANG_OPT_BUNDLE.getString(key).equals(s)).findFirst().get();
            },
            controller::setCurrentLanguage),
        loadSlogo,
        loadGen,

//        loadOld,
        //uploadTurtle,
        uploadPref,
        themeComboBox
    );

    scene = new Scene(root, 600, 400);

    controller.updateCurrentTheme(scene);
  }

}

//private void handleLoadOldFile() {
//    File dataFile = Screen.FILE_CHOOSER.showOpenDialog(stage);
//
//    if (dataFile != null) {
//        System.out.println(dataFile.getAbsolutePath());
//    }
//}
//
//private void handleLoadNewFile() {
//    System.out.println("To Be Implemented!");
//}
