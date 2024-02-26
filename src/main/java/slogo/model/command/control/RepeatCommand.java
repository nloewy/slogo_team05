package slogo.model.command.control;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import slogo.model.ModelState;
import slogo.model.SlogoListener;
import slogo.model.command.Command;
import slogo.model.node.Node;

public class RepeatCommand extends Command {

  private final ModelState modelState;
  private final SlogoListener listener;

  public RepeatCommand(ModelState modelState, SlogoListener listener) {
    this.modelState = modelState;
    this.listener = listener;
  }

  @Override
  public double execute(List<Node> arguments)
      throws InvocationTargetException, IllegalAccessException {
    String variableName = "repcount";
    double end = arguments.get(0).getValue();
    Node commands = arguments.get(1);
      double holder = modelState.getVariables().getOrDefault("repcount", Double.MAX_VALUE);
      double res = 0.0;
      for (double i = 1; i <= end; i += 1) {
        modelState.getVariables().put(variableName, i);
        try {
          res = commands.getValue();
        } catch (InvocationTargetException | IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
      if (holder == Double.MAX_VALUE) {
        holder = end;
      }
      modelState.getVariables().put("repcount", holder);
      return res;
  }

  @Override
  public int getNumArgs() {
    return 2;
  }

  public void notifyListener(SlogoListener listener, double value) {

    //super.notifyListener(listener, value);
  }
}
