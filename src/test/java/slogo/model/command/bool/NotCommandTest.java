package slogo.model.command.bool;

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
import slogo.model.node.ConstantNode;
import slogo.model.node.Node;

public class NotCommandTest extends CommandTest {

  public static final double DELTA = 0.001;
  private Turtle myTurtle;
  private Node node;
  private ModelState model;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    myTurtle = null;
    model = new ModelState();
    model.getActiveTurtles().add(new ArrayList<>());
    model.getActiveTurtles().peek().add(1);

    node = new CommandNode("Not", model);
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1",
      "0.0000000, 1",
      "00000.0000, 1",
      "1, 0",
      "0.00000000000002, 0",
      "-340404, 0",
      "23568765435, 0",
      "3.14, 0"
  })
  void testNot(String op1, int result)
      throws InvocationTargetException, IllegalAccessException {
    node.addChild(new ConstantNode(op1, model));
    assertEquals(result, node.evaluate(), DELTA);
  }
}
