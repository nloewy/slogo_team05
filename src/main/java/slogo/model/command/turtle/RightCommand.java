package slogo.model.command.turtle;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import slogo.model.ModelState;
import slogo.model.Turtle;
import slogo.model.api.SlogoListener;
import slogo.model.command.Command;
import slogo.model.node.Node;

public class RightCommand implements Command {

  public static final int NUM_ARGS = 1;
  private final ModelState modelState;
  private final SlogoListener listener;

  public RightCommand(ModelState modelState, SlogoListener listener) {
    this.modelState = modelState;
    this.listener = listener;
  }

  @Override
  public double execute(List<Node> arguments)
      throws InvocationTargetException, IllegalAccessException {
    Turtle turtle = modelState.getTurtles().get(0);
    double oldHeading = turtle.getHeading();
    turtle.setHeading(turtle.getHeading() + arguments.get(0).evaluate());
    listener.onUpdateTurtleState(turtle.getImmutableTurtle());
    return turtle.getHeading() - oldHeading;
  }
}
