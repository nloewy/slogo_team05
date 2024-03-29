package slogo.model.node;

import slogo.model.ModelState;

/**
 * Represents a variable node in the syntax tree. This node retrieves the value associated with a
 * variable from the model state during evaluation.
 *
 * @author Noah Loewy
 */
public class VariableNode extends Node {

  private final String myToken;
  private final ModelState myModelState;

  /**
   * Constructs a VariableNode with the specified case insensitive token and model state.
   *
   * @param token      the token representing the variable node
   * @param modelState the model state associated with this node
   */

  public VariableNode(String token, ModelState modelState) {
    super();
    myModelState = modelState;
    myToken = token.toLowerCase();
  }

  /**
   * Evaluates the variable node by retrieving the value associated with the variable from the model
   * state. If the variable does not exist, it is initialized with a value of 0.0.
   *
   * @return the value associated with the variable
   */

  @Override
  public double evaluate() {
    if (!myModelState.getVariables().containsKey(myToken)) {
      getListener().onUpdateValue(myToken, 0.0);
      myModelState.getVariables().put(myToken, 0.0);
    }
    return myModelState.getVariables().get(myToken);
  }

  /**
   * Retrieves the token associated with this variable node.
   *
   * @return the token representing the variable node, the name of the variable
   */

  @Override
  public String getToken() {
    return myToken;
  }
}


