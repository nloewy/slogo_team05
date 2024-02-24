package slogo.model.command.math;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import slogo.model.ModelState;
import slogo.model.Node;
import slogo.model.SlogoListener;
import slogo.model.Turtle;
import slogo.model.command.Command;

public class PowerCommand extends Command {

  private final Turtle myTurtle;
  private final Map<String, Double> myVariables;

  public PowerCommand(Turtle turtle, Map<String, Double> variables) {
    myTurtle = turtle;
    myVariables = variables;
  }

  public Function<ModelState, Double> execute(List<Node> arguments)
      throws InvocationTargetException, IllegalAccessException {
    double base = arguments.get(0).getValue();
    double exp = arguments.get(1).getValue();
    double result = Math.pow(base, exp);
    if (Double.isNaN(result) || result == Double.NEGATIVE_INFINITY ||
        result == Double.POSITIVE_INFINITY) {
      throw new IllegalArgumentException("Result is operation is undefined");
    }
    return result;
  }

  public void notifyListener(SlogoListener listener, double value) {
    super.notifyListener(listener, value);
  }

}
