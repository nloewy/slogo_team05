package slogo.model.command.query;

import java.util.List;
import slogo.model.SlogoListener;
import slogo.model.Turtle;
import slogo.model.command.Command;

public class YcorCommand extends Command {

  private Turtle myTurtle;

  public YcorCommand(Turtle turtle) {
    myTurtle = turtle;
  }

  public double execute(List<Double> arguments) {
    return myTurtle.getY();

  }
  @Override
  public void notifyListener(SlogoListener listener, double value) {
    super.notifyListener(listener, value);
    listener.onUpdateTurtleState(myTurtle.getImmutableTurtle());
  }

}