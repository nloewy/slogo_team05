package slogo.model.command.bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slogo.model.CommandNode;
import slogo.model.ConstantNode;
import slogo.model.Node;
import slogo.model.Turtle;

public class LessThanCommandTest {

  public static final double DELTA = 0.001;
  private Turtle myTurtle;
  private Node node;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    myTurtle = null;
    node = new CommandNode("slogo.model.command.bool.LessThanCommand", myTurtle);
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 1",
      "0, 0, 0",
      "-1, 1, 1",
      "1, -1, 0",
      "1.5, 2.5, 1",
      "2.222, 2.2220003, 1",
      "-2.2222, -2.2221, 1",
      "-2.2221, -2.2222, 0",
      "2.2220003, 2.222, 0",
      "1E40, 1.000000000001E40, 1",
      "1E40, 1E40, 0",
      "1E-40, 1E-41, 0",
      "1E-41, 1E-40, 1"

  })
  void testLess(String op1, String op2, int result)
      throws InvocationTargetException, IllegalAccessException {
    node.addChildren(new ConstantNode(op1, myTurtle));
    node.addChildren(new ConstantNode(op2, myTurtle));
    assertEquals(result, node.getValue(), DELTA);
  }
}
