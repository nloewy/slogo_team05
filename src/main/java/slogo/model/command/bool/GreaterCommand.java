package slogo.model.command.bool;

import java.util.List;
import slogo.mathutils.MathUtils;
import slogo.model.SlogoListener;
import slogo.model.command.Command;

public class GreaterCommand extends Command {


  public double execute(List<Double> arguments) {
    return (arguments.get(0) > arguments.get(1)) ? 1 : 0;

  }
  public void notifyListener(SlogoListener listener, double value) {
    super.notifyListener(listener, value);
  }

}