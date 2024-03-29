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

public class OrCommandTest extends CommandTest {

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

    node = new CommandNode("Or", model);
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 1",
      "0, 0, 0",
      "0.000000, 0000000, 0",
      "0, 0.0000, 0",
      "-1, 1, 1",
      "1, -1, 1",
      "1.5, 2.5, 1",
      "0, 2.2220003, 1",
      "0, -2.2221, 1",
      "-2.2221, 0, 1",
      "2.2220003, 0, 1",
      "1E40, 1.000000000001E40, 1",
      "1E40, 1E40, 1",
      "1E-40, 1E-41, 0",
      "1E-41, 1E-40, 0",
      "-1E-61, -1E-62, 0",
      "-1E-62, -1E-61, 0",
      "-1E-62, -1E-62, 0"

  })
  void testOr(String op1, String op2, int result)
      throws InvocationTargetException, IllegalAccessException {
    node.addChild(new ConstantNode(op1, model));
    node.addChild(new ConstantNode(op2, model));
    assertEquals(result, node.evaluate(), DELTA);
  }
}
