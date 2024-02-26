package slogo.model.command.bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slogo.model.node.CommandNode;
import slogo.model.node.ConstantNode;
import slogo.model.ModelState;
import slogo.model.node.Node;
import slogo.model.Turtle;

public class EqualCommandTest {

  public static final double DELTA = 0.001;
  private Turtle myTurtle;
  private Node node;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    myTurtle = null;
    ModelState model = new ModelState();
    node = new CommandNode("bool.EqualCommand", model);
  }

  @ParameterizedTest
  @CsvSource({
      "1, 1, 1",
      "0, 0, 1",
      "-1, 1, 0",
      "1, -1, 0",
      "1.5, 2.5, 0",
      "2.222, 2.2220003, 0",
      "-2.2222, -2.2221, 0",
      "-2.2221, -2.2222, 0",
      "2.2220003, 2.222, 0",
      "1E40, 1.000000000001E40, 0",
      "1E40, 1E40, 1",
      "-1E-61, -1E-62, 0",
      "-1E-62, -1E-61, 0",
      "-1E-62, -1E-62, 1"
  })
  void testEqual(String op1, String op2, int result)
      throws InvocationTargetException, IllegalAccessException {
    node.addChild(new ConstantNode(op1, null));
    node.addChild(new ConstantNode(op2, null));
    assertEquals(result, node.getValue(), DELTA);
  }
}
