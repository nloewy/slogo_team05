package slogo.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import slogo.model.command.Command;

public class CommandNode extends Node {

  private final Command command;
  private final Method m;
  private final ModelState myModelState;
  private final String myToken;


  public CommandNode(String token, ModelState modelState)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    super();
    myToken = token;
    Class<?> clazz = Class.forName(token);
    command = (Command) clazz.getDeclaredConstructor().newInstance();
    m = clazz.getDeclaredMethod("execute", List.class);
    myModelState = modelState;
  }

  public double getValue() throws InvocationTargetException, IllegalAccessException {
    List<Node> children = getChildren();
    Function<ModelState, Double> action = (Function<ModelState, Double>) m.invoke(command,
        children);
    return myModelState.applyCommandToModelState(action);
  }

  public String getToken() {
    return myToken;
  }
}