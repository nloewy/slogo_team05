package slogo.model.exceptions;

import slogo.model.exceptions.InvalidOperandException;

public class UndefinedExponentException extends InvalidOperandException {

  public UndefinedExponentException(String s) {
    super(s);
  }
}