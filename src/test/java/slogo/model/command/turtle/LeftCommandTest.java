package slogo.model.command.turtle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slogo.model.ModelState;
import slogo.model.Turtle;
import slogo.model.command.CommandTest;
import slogo.model.node.CommandNode;
import slogo.model.node.ConstantNode;
import slogo.model.node.Node;

public class LeftCommandTest extends CommandTest {


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
    node = new CommandNode("Left", model);
    node.addListener(myListener);

  }

  @Test
  void testBasicLeft()
      throws InvocationTargetException, IllegalAccessException {
    String rot = "75";
    node.addChild(new ConstantNode(rot, model));
    assertEquals(75, node.evaluate(), DELTA);
    assertEquals(-75, myTurtle.getHeading(), DELTA);

  }

  @Test
  void testLeftWithNonZeroHeading()
      throws InvocationTargetException, IllegalAccessException {
    myTurtle.setHeading(60);
    String rot = "20";
    node.addChild(new ConstantNode(rot, model));
    assertEquals(20, node.evaluate(), DELTA);
    assertEquals(40, myTurtle.getHeading(), DELTA);
  }

  @Test
  void testLeftWithZeroHeading()
      throws InvocationTargetException, IllegalAccessException {
    String rot = "0";
    myTurtle.setHeading(500);
    node.addChild(new ConstantNode(rot, model));
    assertEquals(0, node.evaluate(), DELTA);
    assertEquals(500, myTurtle.getHeading(), DELTA);

  }

  @Test
  void testLeftNegativeHeading()
      throws InvocationTargetException, IllegalAccessException {
    String rot = "-75";
    node.addChild(new ConstantNode(rot, model));
    assertEquals(-75, node.evaluate(), DELTA);
    assertEquals(75, myTurtle.getHeading(), DELTA);
  }

  @Test
  void testLeftHeadingOver360()
      throws InvocationTargetException, IllegalAccessException {
    String rot = "900";
    node.addChild(new ConstantNode(rot, model));
    assertEquals(900, node.evaluate(), DELTA);
    assertEquals(-900, myTurtle.getHeading(), DELTA);
  }
}
