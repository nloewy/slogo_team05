package slogo.model.command.math;

import java.util.List;
import slogo.model.SlogoListener;
import slogo.model.command.Command;

public class QuotientCommand extends Command {


  public double execute(List<Double> arguments) throws ArithmeticException {
    try {
      return arguments.get(0) / arguments.get(1);
    }
    catch (ArithmeticException e)  {
      return 0.0;
    }

  }
  public void notifyListener(SlogoListener listener, double value) {
    super.notifyListener(listener, value);
  }

}
