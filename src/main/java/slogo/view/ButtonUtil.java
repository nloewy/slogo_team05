package slogo.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ButtonUtil {

  public static Button generateButton(String text,
      int x,
      int y,
      EventHandler<ActionEvent> consumer) {
    Button ret = new Button(text);
    ret.setLayoutX(x);
    ret.setLayoutY(y);
    ret.setOnAction(consumer);

    ret.setId("test");

    return ret;
  }
}