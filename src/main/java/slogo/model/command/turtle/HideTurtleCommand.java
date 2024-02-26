package slogo.model.command.turtle;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import slogo.model.ModelState;
import slogo.model.SlogoListener;
import slogo.model.Turtle;
import slogo.model.command.Command;
import slogo.model.node.Node;

public class HideTurtleCommand extends Command {
  public static final int NUM_ARGS = 0;
  private final ModelState modelState;
  private final SlogoListener listener;

  public HideTurtleCommand(ModelState modelState, SlogoListener listener) {
    this.modelState = modelState;
    this.listener = listener;
  }

  @Override
  public double execute(List<Node> arguments)
      throws InvocationTargetException, IllegalAccessException {
    Turtle turtle = modelState.getTurtles().get(0);
    turtle.setVisible(false);
    return 0.0;
  }

  }

/**
 * @Override public void notifyListener(SlogoListener listener, double value) {
 * super.notifyListener(listener, value);
 * listener.onUpdateTurtleState(myTurtle.getImmutableTurtle()); }
 */


