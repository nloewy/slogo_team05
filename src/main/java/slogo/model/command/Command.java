package slogo.model.command;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import slogo.model.Node;
import slogo.model.SlogoListener;

public abstract class Command {

  public abstract double execute(List<Node> arguments)
      throws InvocationTargetException, IllegalAccessException;

  public abstract int getNumberOfArgs();

  public void notifyListener(SlogoListener listener, double value) {
    listener.onReturn(value);
  }
}
