package slogo.model.node;

import slogo.model.ModelState;

/**
 * Represents a list node in the syntax tree. This node serves as a container for other nodes and
 * does not perform any evaluation itself.
 */
public class ListNode extends Node {

  private static final String CLOSED_BRACKET = "]";
  /**
   * The token representing the list node.
   */
  private final String myToken;

  /**
   * Constructs a ListNode with the specified token and model state.
   *
   * @param token the token representing the list node
   * @param model the model state associated with this node
   */
  public ListNode(String token, ModelState model) {
    super();
    myToken = token;
  }

  /**
   * Evaluates the list node by recursively evaluating its children nodes. Returns the result of the
   * last evaluated child node.
   *
   * @return the result of the last evaluated child node
   */
  @Override
  public double evaluate() {
    double ret = 0.0;
    for (Node child : getChildren()) {
      if (!child.getToken().equals(CLOSED_BRACKET)) {
        ret = child.evaluate();
      }
    }
    return ret;
  }

  /**
   * Retrieves the token associated with this list node.
   *
   * @return the token representing the list node
   */
  @Override
  public String getToken() {
    return myToken;
  }
}
