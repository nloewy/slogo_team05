package slogo.model.api;

public class InvalidOperandException extends RuntimeException {

  public InvalidOperandException(String s) {
    super(s);
  }
}