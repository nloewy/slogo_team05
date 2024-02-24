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

public class QuotientCommand extends Command {

  private final Turtle myTurtle;
  private final Map<String, Double> myVariables;

  public QuotientCommand(Turtle turtle, Map<String, Double> variables) {
    myTurtle = turtle;
    myVariables = variables;
  }

  public Function<ModelState, Double> execute(List<Node> arguments)
      throws InvocationTargetException, IllegalAccessException {
    if (arguments.get(1).getValue() == 0) {
      throw new ArithmeticException("Divisor must be non-zero");
    }
    return arguments.get(0).getValue() / arguments.get(1).getValue();
  }

  public void notifyListener(SlogoListener listener, double value) {
    super.notifyListener(listener, value);
  }

}
