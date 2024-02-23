package slogo.model.command.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slogo.model.CommandNode;
import slogo.model.ConstantNode;
import slogo.model.Node;
import slogo.model.Turtle;

public class SumCommandTest {

  public static final double DELTA = 0.001;

  private Turtle myTurtle;
  private Node node;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

    myTurtle = null;
    node = new CommandNode("slogo.model.command.math.SumCommand", myTurtle);

  }

  @ParameterizedTest
  @CsvSource({
      "720, 0, 720",
      "-39393, -200, -39593",
      "-8.39, 3.14, -5.25",
      "0.5, -1.2, -0.7",
      "7.3, -2.8, 4.5",
      "2147483647.0, 2147483647.0, 4294967294.0",
      "-2147483648.0, -2147483648.0, -4294967296.0",
      "3.4028235E38, -3.4028235E38, 0.0",
      "1.0001, 0.0001, 1.0002",
      "360, 10, 370"
  })
  void testSumBasic(String op1, String op2, String result)
      throws InvocationTargetException, IllegalAccessException {
    node.addChildren(new ConstantNode(op1, myTurtle));
    node.addChildren(new ConstantNode(op2, myTurtle));
    assertEquals(Double.parseDouble(result), node.getValue(), DELTA);
  }

}