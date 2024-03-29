package slogo.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import slogo.model.api.Model;
import slogo.model.api.SlogoException;
import slogo.model.api.SlogoModel;
import slogo.view.pages.MainScreen;
import slogo.view.pages.StartScreen;

/**
 * The Controller class is the main controller for the SLogo application. It is responsible for
 * bridging the Model and the View. It is also responsible for initializing the StartScreen and
 * then managing the state of the application and the windows that are open.
 */

public class Controller {

  private final Stage stage;
  private final List<Consumer<String>> languageObservers = new ArrayList<>();
  private final List<MainScreen> windows = new ArrayList<>();
  private final Properties prop;
  private String currentLanguage = "English";
  private String currentTheme = "LightMode.css";
  private Model model;
  private Consumer<String> parse;
  private File turtleImage;
  private String uploadedCommand;


  public Controller(Stage stage) throws IOException {
    this.stage = stage;
    openStartScreen();
    prop = new Properties();
  }

  public void setTurtleImage(File i) {
    turtleImage = i;
  }

  public void openStartScreen() throws FileNotFoundException {
    StartScreen startScreen = new StartScreen(stage, this);
    startScreen.setUp();
    setCurrentLanguage(currentLanguage);
    stage.setScene(startScreen.getScene());
    stage.show();
  }

  public void addLanguageObserver(Consumer<String> observer) {
    if (observer != null) {
      languageObservers.add(observer);
    }
  }

  private void updateLanguageObservers() {
    for (Consumer<String> observer : languageObservers) {
      observer.accept(currentLanguage);
    }
  }

  public String getCurrentLanguage() {
    return currentLanguage;
  }

  public void setCurrentLanguage(String language) {
    this.currentLanguage = language;

    updateLanguageObservers();
  }

  public void updateCurrentTheme(Scene scene) {
    scene.getStylesheets().clear();
    scene.getStylesheets()
        .add(Objects.requireNonNull(ViewInternal.class.getResource(currentTheme)).toExternalForm());
  }

  public void setCurrentTheme(String theme, Scene scene) {
    this.currentTheme = theme;
    System.out.print("Current Theme: " + currentTheme);
    updateCurrentTheme(scene);
  }

  public String getCurrentTheme() {
    return currentTheme;
  }

  public void openNewIdeSession(String slogoContent) throws IOException {
    Stage newStage = new Stage();
    newStage.setMaximized(false);
    getProperties();
    MainScreen mainScreen = new MainScreen(newStage, this);
    if (turtleImage != null) {
      mainScreen.setTurtleImage(turtleImage);
    }
    model = new SlogoModel(mainScreen, currentLanguage);

    windows.add(mainScreen);

    parse = t -> {
      try {
        model.parse(t);
      } catch (SlogoException e) {
        String template = (String) prop.getOrDefault(e.getCause().getClass().getSimpleName(),
            e.getMessage());
        String message = String.format(template, e.getToken());
        new Alert(AlertType.ERROR, message).show();
      }
    };
    mainScreen.addParser(parse, slogoContent);
    setCurrentLanguage(currentLanguage);
  }

  public void loadSession(String type) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open SLogo File");
    fileChooser.setInitialDirectory(new File("data/examples/"));
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("SLogo Files", "*.slogo"));
    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {
      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      try {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
          String line;
          while ((line = br.readLine()) != null) {
            if (line.startsWith("#")) {
              continue;
            }
            contentBuilder.append(line).append("\n");
          }
        }
        String slogoContent = contentBuilder.toString();
        if (type.equals("new")) {
          openNewIdeSession(slogoContent);
        } else {
          setSlogoContent(slogoContent);
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getSlogoContent() {
    return uploadedCommand;
  }

  private void setSlogoContent(String slogoContent) {
    uploadedCommand = slogoContent;
  }

  public Map<String, Map<String, String>> getCommandDetailsFromXml(String commandLanguage) {
    Map<String, Map<String, String>> commandDetails = new HashMap<>();
    try {
      File inputFile = new File("data/helpXmls/" + commandLanguage + ".xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      NodeList list = doc.getElementsByTagName("command");
      for (int temp = 0; temp < list.getLength(); temp++) {
        Node node = list.item(temp);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) node;
          String commandName = eElement.getElementsByTagName("canonicalName").item(0)
              .getTextContent();
          String description = eElement.getElementsByTagName("description").item(0)
              .getTextContent();

          NodeList exampleList = eElement.getElementsByTagName("example");
          String example = exampleList.getLength() > 0 ? exampleList
              .item(0).getTextContent() : "";
          Element helpDocumentation = (Element) eElement.getElementsByTagName("helpDocumentation")
              .item(0);
          NodeList parametersList = helpDocumentation.getElementsByTagName("parameters");
          String parameters =
              parametersList.getLength() > 0 ? parametersList.item(0).getTextContent() : "";
          String returnValue = helpDocumentation.getElementsByTagName("returnValue").item(0)
              .getTextContent();
          Map<String, String> details = new HashMap<>();
          details.put("description", description);
          details.put("example", example);
          details.put("parameters", parameters);
          details.put("returnValue", returnValue);
          commandDetails.put(commandName, details);
          System.out.print(temp);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return commandDetails;
  }

  private void getProperties() {
    File file = new File(
        "src/main/resources/slogo/languages/" + currentLanguage + ".properties");
    try {
      prop.load(new FileInputStream(file));
    } catch (IOException ex) {
      new Alert(AlertType.ERROR, "File for Language Not Found").show();
    }

  }

  public void loadSettings(File file, Scene scene) {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(file);
      doc.getDocumentElement().normalize();
      String language = doc.getElementsByTagName("language").item(0).getTextContent();
      String theme = doc.getElementsByTagName("theme").item(0).getTextContent();
      String penColor = doc.getElementsByTagName("penColor").item(0).getTextContent();
      setCurrentLanguage(language);
      for (MainScreen window : windows) {
        window.setPenColor(penColor);
      }
      setCurrentTheme(theme, scene);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void resetModel() {
    model.resetModel();
  }
}

