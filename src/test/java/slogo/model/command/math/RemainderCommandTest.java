package slogo.model.command.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slogo.model.node.CommandNode;
import slogo.model.node.ConstantNode;
import slogo.model.ModelState;
import slogo.model.node.Node;
import slogo.model.Turtle;

public class RemainderCommandTest {

  public static final double DELTA = 0.001;

  private Turtle myTurtle;
  private Node node;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

    myTurtle = null;
    ModelState model = new ModelState();
    node = new CommandNode("math.RemainderCommand", model);

  }

  @ParameterizedTest
  @CsvSource({
      "720, 2, 0",
      "-39393, -200, -193",
      "-8.39, 3.14, -2.11",
      "0.5, -1.2, 0.5",
      "7.3, -2.8, 1.7",
      "2147483647.0, 2147483647.0, 0.0",
      "-2147483648.0, -2147483648.0, 0.0",
      "3.4028235E38, -3.4028235E38, 0.0",
      "1.0001, 0.0001, 0.0001",
      "360, 10, 0"
  })
  void testRemainderBasic(String op1, String op2, String result)
      throws InvocationTargetException, IllegalAccessException {
    node.addChild(new ConstantNode(op1, null));
    node.addChild(new ConstantNode(op2, null));
    assertEquals(Double.parseDouble(result), node.getValue(), DELTA);
  }

  @Test
  void testRemainderDivideByZero() throws InvocationTargetException, IllegalAccessException {
    {
      node.addChild(new ConstantNode("50", null));
      node.addChild(new ConstantNode("0", null));
      Throwable e = assertThrows(ArithmeticException.class, () -> {
        node.getValue();
      });
    }

  }

}
