package slogo.model.node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import slogo.model.InvalidCommandException;
import slogo.model.ModelState;
import slogo.model.SlogoListener;
import slogo.model.command.Command;

public class CommandNode extends Node {

  private final Command command;
  private final Method m;
  private final String myToken;

  public CommandNode(String token, ModelState modelState, SlogoListener listener)
      throws ClassNotFoundException {
    super();
    try {
      myToken = "slogo.model.command." + token + "Command";
      Class<?> clazz = Class.forName(myToken);
      command = (Command) clazz.getDeclaredConstructor(ModelState.class, SlogoListener.class)
          .newInstance(modelState, listener);
      m = clazz.getDeclaredMethod("execute", List.class);
    }
    catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
           NoSuchMethodException | IllegalAccessException e) {
      throw new ClassNotFoundException("Command Class Not Found");
    }
  }


  @Override
  public double getValue() throws InvocationTargetException, IllegalAccessException {
    List<Node> children = getChildren();
    return (double) m.invoke(command, children);
  }

  @Override
  public int getNumArgs()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    return (int) Class.forName(myToken).getField("NUM_ARGS").get(null);
  }

  public String getToken() {
    return myToken;
  }
}
