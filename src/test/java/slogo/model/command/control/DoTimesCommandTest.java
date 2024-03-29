package slogo.model.command.control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slogo.model.ModelState;
import slogo.model.Turtle;
import slogo.model.command.CommandTest;
import slogo.model.node.CommandNode;
import slogo.model.node.ConstantNode;
import slogo.model.node.ListNode;
import slogo.model.node.Node;
import slogo.model.node.VariableNode;

public class DoTimesCommandTest extends CommandTest {

  public static final double DELTA = 0.001;

  private Turtle myTurtle;
  private Node node;
  private ModelState model;

  @BeforeEach
  void setUp()
      throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    model = new ModelState();
    model.getActiveTurtles().add(new ArrayList<>());
    model.getActiveTurtles().peek().add(1);

    myTurtle = new Turtle(1);
    model.getTurtles().put(1, myTurtle);
  }

  @Test
  void testDoTimesForwardVariableNotUsed()
      throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException {
    node = new CommandNode("DoTimes", model);
    Node fwdNode = new CommandNode("Backward", model);
    Node varNode = new VariableNode("i", model);
    Node listNode = new ListNode("", model);
    Node commandListNode = new ListNode("", model);
    commandListNode.addChild(fwdNode);
    fwdNode.addChild(new ConstantNode("2", model));
    listNode.addChild(varNode);
    listNode.addChild(new ConstantNode("5", model));
    node.addChild(listNode);
    node.addChild(commandListNode);
    dfsAddListener(node);
    assertEquals(node.evaluate(), 2, DELTA);
    assertEquals(myTurtle.getY(), 10, DELTA);
  }

  @Test
  void testDoTimesForwardVariableUsed()
      throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException {
    node = new CommandNode("DoTimes", model);
    Node listNode = new ListNode("", model);
    Node commandListNode = new ListNode("", model);
    Node cmdNode = new CommandNode("Backward", model);
    commandListNode.addChild(cmdNode);
    Node varNode = new VariableNode("i", model);
    cmdNode.addChild(new VariableNode("i", model));
    listNode.addChild(varNode);
    listNode.addChild(new ConstantNode("5", model));
    Node cmdNode2 = new CommandNode("Backward", model);
    cmdNode2.addChild(new VariableNode("i", model));
    commandListNode.addChild(cmdNode2);
    node.addChild(listNode);
    node.addChild(commandListNode);
    dfsAddListener(node);
    assertEquals(node.evaluate(), 5, DELTA);
    assertEquals(myTurtle.getY(), 30, DELTA);
  }
}
