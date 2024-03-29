package slogo.model.command.turtle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slogo.model.ModelState;
import slogo.model.Turtle;
import slogo.model.command.CommandTest;
import slogo.model.node.CommandNode;
import slogo.model.node.ConstantNode;
import slogo.model.node.Node;

public class SetHeadingCommandTest extends CommandTest {

  public static final double DELTA = 0.001;

  private Turtle myTurtle;
  private Node node;

  private ModelState model;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    model = new ModelState();
    model.getTurtles().put(1, new Turtle(1));
    model.getActiveTurtles().add(new ArrayList<>());
    model.getActiveTurtles().peek().add(1);
    myTurtle = model.getTurtles().get(1);
    node = new CommandNode("SetHeading", model);
    node.addListener(myListener);


  }

  @ParameterizedTest
  @CsvSource({
      "45, 45, 45",
      "90, 90, 90",
      "180, 180, 180",
      "181, 179, 181",
      "0, 0, 0"
  })
  void testBasicHeading(String newHeading, String expectedValue, String expectedHeading)
      throws InvocationTargetException, IllegalAccessException {
    node.addChild(new ConstantNode(newHeading, model));
    assertEquals(Double.parseDouble(expectedValue), node.evaluate(), DELTA);
    assertEquals(Double.parseDouble(expectedHeading), myTurtle.getHeading(), DELTA);

  }

  @Test
  void testHeadingWithNonZeroInitial()
      throws InvocationTargetException, IllegalAccessException {
    myTurtle.setHeading(60);
    String newHeading = "20";
    node.addChild(new ConstantNode(newHeading, model));
    assertEquals(40, node.evaluate(), DELTA);
    assertEquals(20, myTurtle.getHeading(), DELTA);
  }

  @Test
  void testHeadingOver360()
      throws InvocationTargetException, IllegalAccessException {
    String newHeading = "940";
    myTurtle.setHeading(500);
    node.addChild(new ConstantNode(newHeading, model));
    assertEquals(80, node.evaluate(), DELTA);
    assertEquals(940, myTurtle.getHeading(), DELTA);
  }

  @Test
  void testHeadingNoChange()
      throws InvocationTargetException, IllegalAccessException {
    String newHeading = "1081";
    myTurtle.setHeading(361);
    node.addChild(new ConstantNode(newHeading, model));
    assertEquals(0, node.evaluate(), DELTA);
    assertEquals(1081, myTurtle.getHeading(), DELTA);
  }

  @Test
  void testHeadingNegativeValue()
      throws InvocationTargetException, IllegalAccessException {
    String newHeading = "-270";
    myTurtle.setHeading(95);
    node.addChild(new ConstantNode(newHeading, model));
    assertEquals(5, node.evaluate(), DELTA);
    assertEquals(-270, myTurtle.getHeading(), DELTA);
  }
}