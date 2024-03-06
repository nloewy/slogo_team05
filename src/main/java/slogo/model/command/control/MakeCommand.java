package slogo.model.command.control;

import java.util.List;
import slogo.model.ModelState;
import slogo.model.api.SlogoListener;
import slogo.model.command.Command;
import slogo.model.node.Node;

/**
 * The MakeCommand class represents the "make" command in the control structure. It assigns a value
 * to a variable in the model state and notifies the listener.
 *
 * @author Noah Loewy
 */
public class MakeCommand implements Command {

  /**
   * The number of arguments this command requires.
   */
  public static final int NUM_ARGS = 2;

  private final ModelState modelState;
  private final SlogoListener listener;

  /**
   * Constructs an instance of MakeCommand with the given model state and listener.
   *
   * @param modelState the model state
   * @param listener   the listener for state change events
   */
  public MakeCommand(ModelState modelState, SlogoListener listener) {
    this.modelState = modelState;
    this.listener = listener;
  }

  /**
   * Executes the "make" command.
   *
   * @param arguments a list containing two nodes: the first node represents the variable name, and
   *                  the second node represents the value to assign to the variable
   * @param turtleId  the id of the turtle currently active
   *
   * @return the assigned value
   */
  @Override
  public double execute(List<Node> arguments, int turtleId) {
    modelState.outer = false;
    String token = arguments.get(0).getToken();
    double variableValue = arguments.get(1).evaluate();
    modelState.getVariables().put(token, variableValue);
    listener.onUpdateValue(token, variableValue);
    return variableValue;
  }
}
