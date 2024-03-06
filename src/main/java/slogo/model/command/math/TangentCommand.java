package slogo.model.command.math;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import slogo.model.ModelState;
import slogo.model.api.SlogoListener;
import slogo.model.command.Command;
import slogo.model.exceptions.InvalidOperandException;
import slogo.model.node.Node;

/**
 * The TangentCommand class represents the tangent mathematical operation. It calculates the tangent
 * of a given angle in degrees.
 *
 * @author Noah Loewy
 */
public class TangentCommand implements Command {

  /**
   * The number of arguments this command requires.
   */
  public static final int NUM_ARGS = 1;
  private final ModelState modelState;

  /**
   * Constructs an instance of TangentCommand with the given model state and listener. This
   * constructor does not actually do anything, and exists for the sake of consistency across
   * commands.
   *
   * @param modelState the model state
   * @param listener   the listener for state change events
   */
  public TangentCommand(ModelState modelState, SlogoListener listener) {
    this.modelState = modelState;
  }

  /**
   * Executes the tangent mathematical operation.
   *
   * @param arguments a list containing a single node representing the angle in degrees
   * @param index     the index of the turtle in the list at the top of getActiveTurtles() stack
   * @return the tangent of the input angle
   * @throws InvalidOperandException   if the tangent function is undefined
   */
  @Override
  public double execute(List<Node> arguments, int index) {
    modelState.setOuter(false);
    double arg1 = arguments.get(0).evaluate();
    if (Math.abs(arg1 % 180) == 90) {
      throw new InvalidOperandException("Illegal Value for Tangent Function");
    }
    return Math.tan(Math.toRadians(arg1));
  }
}
