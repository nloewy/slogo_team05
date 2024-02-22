package slogo.model.command.query;

import java.util.List;
import slogo.model.Node;
import slogo.model.SlogoListener;
import slogo.model.Turtle;
import slogo.model.command.Command;

public class YcoordinateCommand extends Command {

  private final Turtle myTurtle;

  public YcoordinateCommand(Turtle turtle) {
    myTurtle = turtle;
  }

  public double execute(List<Node> arguments) {
    return myTurtle.getY();

  }

  public int getNumberOfArgs() {
    return 0;
  }

  @Override
  public void notifyListener(SlogoListener listener, double value) {
    super.notifyListener(listener, value);
    listener.onUpdateTurtleState(myTurtle.getImmutableTurtle());
  }

}
