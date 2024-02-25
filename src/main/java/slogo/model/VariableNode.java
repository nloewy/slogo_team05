package slogo.model;

import java.lang.reflect.InvocationTargetException;

public class VariableNode extends Node {

  private final String myToken;
  private final ModelState myModelState;

  public VariableNode(String token, ModelState modelState) {
    myModelState = modelState;
    myToken = token;
  }

  @Override
  public double getValue() throws InvocationTargetException, IllegalAccessException {
    return myModelState.getVariables().get(myToken);
  }

  @Override
  public String getToken() {
    return myToken;
  }
}


