package slogo.model.command.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slogo.model.ModelState;
import slogo.model.Turtle;
import slogo.model.command.CommandTest;
import slogo.model.node.CommandNode;
import slogo.model.node.Node;

public class YcoordinateCommandTest extends CommandTest {

  public static final double DELTA = 0.001;

  private Turtle myTurtle;
  private Node node;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

    ModelState model = new ModelState();
    model.getTurtles().put(1, new Turtle(1));
    myTurtle = model.getTurtles().get(1);
    model.getActiveTurtles().add(new ArrayList<>());
    model.getActiveTurtles().peek().add(1);
    node = new CommandNode("Ycoordinate", model);

  }

  @ParameterizedTest
  @CsvSource({
      "0",
      "-3939393948",
      "-8.39",
      "0.5",
      "7.3",
      "2147483647.0", // Points at max integers
      "-2147483648.0", // Points at min integers
      "3.4028235E38", // Points at max/min floats
      "1.0001"
  })
  void testYCorrBasic(String y)
      throws InvocationTargetException, IllegalAccessException {
    myTurtle.setY(Double.parseDouble(y));
    assertEquals(myTurtle.getY(), node.evaluate(), DELTA);
  }

}