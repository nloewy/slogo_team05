package slogo.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import slogo.model.exceptions.InsufficientArgumentsException;
import slogo.model.exceptions.InvalidCommandException;
import slogo.model.exceptions.InvalidTokenException;
import slogo.model.exceptions.InvalidUserCommandException;
import slogo.model.exceptions.InvalidVariableException;
import slogo.model.node.CommandCreatorNode;
import slogo.model.node.CommandNode;
import slogo.model.node.ConstantNode;
import slogo.model.node.ListNode;
import slogo.model.node.Node;
import slogo.model.node.TurtleCommandNode;
import slogo.model.node.UserCommandNode;
import slogo.model.node.VariableNode;

/**
 * The Parser class is responsible for parsing Slogo commands and generating syntax trees from the
 * input strings. It takes a ModelState object and the current language as inputs, then uses these
 * to parse the input string into a syntax tree.
 *
 * @author Noah Loewy
 */

public class Parser {

  private static final String OPEN_BRACKET = "[";
  private static final String CLOSED_BRACKET = "]";
  private static final String TO_COMMAND = "To";
  private static final String RESOURCE_PATH = "src/main/resources/slogo/languages/";

  private final List<String> turtleCommands;

  private final ModelState modelState;
  private final PatternLoader patternLoader;
  private Node rootNode;
  private Map<String, String> commandMap;
  private Node currentNode;
  private int myIndex;
  private List<Map.Entry<Predicate<String>, Consumer<List<String>>>> nodeHandler;
  private Map<String, Consumer<Stack<Node>>> tokenHandlers;

  /**
   * Constructs a Parser object with the given model state and current language.
   *
   * @param modelState      the model state containing information about the simulation
   * @param currentLanguage the current language used for parsing commands
   * @throws IOException if an I/O error occurs while loading language properties
   */
  public Parser(ModelState modelState, String currentLanguage) throws IOException {
    this.modelState = modelState;
    commandMap = loadCommandMap(RESOURCE_PATH + currentLanguage + ".properties");
    patternLoader = new PatternLoader(
        RESOURCE_PATH + "Syntax.properties");
    initializeNodeHandler();
    initializeTokenMap();
    turtleCommands = Files.lines(Paths.get(RESOURCE_PATH + "TurtleCommands.txt"))
        .collect(Collectors.toList());

  }

  /**
   * Parses the input string and generates a syntax tree rooted at the given root node. The root
   * node, which is passed into the Parser from the model, then actually executes the commands by
   * "evaluating" the nodes. A greedy parsing method is used.
   *
   * @param input    the input string to be parsed
   * @param rootNode the root node of the syntax tree, which is assumed to be a ListNode passed in
   *                 by the model.
   */
  public void parse(String input, Node rootNode) {
    resetParsing();
    List<String> tokens = Arrays.asList(input.split("\\s+"));
    Stack<Node> nodeStack = new Stack<>();
    this.rootNode = rootNode;
    nodeStack.push(rootNode);
    while (myIndex < tokens.size()) {
      parseNextToken(tokens, nodeStack);
      myIndex++;
    }
    while (!nodeStack.isEmpty() && topNodeSatisfied(nodeStack)) {
      nodeStack.pop();
    }
  }

  /**
   * Resets the parsing state by resetting the index, root node, and current node.
   */
  private void resetParsing() {
    myIndex = 0;
    rootNode = null;
    currentNode = null;
  }


  /**
   * Checks if the node at the top of the node stack has received all of its arguments.
   */
  private boolean topNodeSatisfied(Stack<Node> nodeStack) {
    return nodeStack.peek().getNumArgs() <= nodeStack.peek().getChildren().size();
  }

  /**
   * Parses next token in the input and creates a new node, updating the nodeStack accordingly.
   */
  private void parseNextToken(List<String> tokens, Stack<Node> nodeStack) {
    while (myIndex < tokens.size() && (tokens.get(myIndex).isEmpty() || tokens.get(myIndex)
        .startsWith("#"))) {  //skips comments and white space
      myIndex++;
    }
    Consumer<Stack<Node>> handler = tokenHandlers.getOrDefault(
        tokens.get(myIndex),
        stack -> createTokenAndUpdateStack(tokens, stack)
    );
    handler.accept(nodeStack); //updates currentNode
  }


  /**
   * Creates a new node for current token and adds to stack. Handles bracket matching for list
   * nodes.
   */

  private void createTokenAndUpdateStack(List<String> tokens, Stack<Node> nodeStack) {
    createNode(tokens, nodeStack);
    while (!nodeStack.peek().getToken().equals(OPEN_BRACKET) && topNodeSatisfied(nodeStack)) {
      nodeStack.pop();
    }
    if (isInvalidCommand(tokens.get(myIndex), nodeStack)) {
      throw new InvalidCommandException("", tokens.get(myIndex));
    }
    nodeStack.peek().addChild(currentNode);
    nodeStack.push(currentNode);
  }

  private boolean isInvalidCommand(String token, Stack<Node> nodeStack) {
    return (!commandMap.containsKey(token.toLowerCase())
        && !modelState.getUserDefinedCommands().containsKey(token.toLowerCase()))
        && nodeStack.peek().equals(rootNode);
  }

  /**
   * Updates node stack to match closing brackets with corresponding open brackets.
   */
  private void handleClosedBracket(Stack<Node> nodeStack) {
    while (!nodeStack.peek().getToken().equals(OPEN_BRACKET)) {
      nodeStack.pop();
    }
    if (!nodeStack.peek().equals(rootNode)) {
      nodeStack.peek().addChild(new ListNode(CLOSED_BRACKET, modelState));
      nodeStack.pop();
    }
  }

  /**
   * Creates new ListNode when open bracket is parsed.
   */
  private void handleOpenBracket(Stack<Node> nodeStack) {
    while (!nodeStack.peek().equals(rootNode) && topNodeSatisfied(nodeStack)) {
      nodeStack.pop();
    }
    Node newNode = new ListNode(OPEN_BRACKET, modelState);
    nodeStack.peek().addChild(newNode);
    nodeStack.push(newNode);
  }

  /**
   * Creates a node based on the current token, if it is valid, and updates the node stack. Throws
   * exception if token is not valid
   */

  private void createNode(List<String> tokens, Stack<Node> nodeStack) {
    String token = tokens.get(myIndex);
    for (Map.Entry<Predicate<String>, Consumer<List<String>>> entry : nodeHandler) {
      if (entry.getKey().test(token)) {
        entry.getValue().accept(tokens);
        return;
      }
    }
    handleInvalidToken(token, nodeStack);
  }

  private void handleInvalidToken(String token, Stack<Node> nodeStack) {
    while (nodeStack.size() > 1) {
      boolean flag = topNodeSatisfied(nodeStack);
      nodeStack.pop();
      if (!flag) {
        nodeStack.peek().removeChildren();
      }
    }
    throw new InvalidTokenException("Token not valid", token);
  }


  /**
   * Creates a command creator node for the "TO" command.
   */

  private void makeCommandCreatorNode(List<String> tokens) {
    int index = myIndex + 3;
    index = getNextClosedBracket(tokens, index); //where params of new command end in tokens
    if (index == tokens.size()) {
      throw new InsufficientArgumentsException("", tokens.get(myIndex));
    }
    checkForInvalidCommandName(tokens, index);
    currentNode = new CommandCreatorNode(tokens.get(myIndex + 1).toLowerCase(), modelState,
        index - myIndex - 1);
    myIndex++;
  }

  private void checkForInvalidCommandName(List<String> tokens, int index) {
    if (myIndex + 1 >= tokens.size() || !tokenMatched(tokens.get(myIndex + 1), "Command")
        || commandMap.containsKey(tokens.get(myIndex + 1))) {
      String userCommandName = (tokens.size() > index) ? tokens.get(myIndex + 1) : "";
      throw new InvalidUserCommandException("", userCommandName);
    }
  }

  private int getNextClosedBracket(List<String> tokens, int index) {
    while (index < tokens.size() && !tokens.get(index).equals(CLOSED_BRACKET)) {
      if (!tokenMatched(tokens.get(index), "Variable")) {
        throw new InvalidVariableException("", tokens.get(index));
      }
      index++;
    }
    return index;
  }


  /**
   * Loads the command map from the specified file path. The command map maps command canonical
   * names and aliases to the actual Command class used.
   */
  private Map<String, String> loadCommandMap(String filePath) throws FileNotFoundException {
    Properties properties = new Properties();
    commandMap = new HashMap<>();
    try {
      File file = new File(filePath);
      properties.load(new FileInputStream(file));
      for (String commandName : properties.stringPropertyNames()) {
        String[] aliases = properties.getProperty(commandName).split("\\|");
        for (String alias : aliases) {
          commandMap.put(alias, commandName);
        }
      }
    } catch (IOException e) {
      throw new FileNotFoundException("Cannot find file " + filePath);
    }
    return commandMap;
  }

  /**
   * Initializes the node handler, which maps token patterns to node creation functions. The node
   * handler is used during parsing to create appropriate nodes based on the tokens encountered.
   */

  private void initializeNodeHandler() {
    nodeHandler = new ArrayList<>();

    nodeHandler.add(new SimpleEntry<>(
        token -> tokenMatched(token, "Constant"),
        tokens -> currentNode = new ConstantNode(tokens.get(myIndex).toLowerCase(), modelState)));

    nodeHandler.add(new SimpleEntry<>(
        token -> tokenMatched(token, "Variable"),
        tokens -> currentNode = new VariableNode(tokens.get(myIndex).toLowerCase(), modelState)));

    nodeHandler.add(new SimpleEntry<>(
        token -> isCommand(token) && isToCommand(token),
        this::makeCommandCreatorNode));

    nodeHandler.add(new SimpleEntry<>(
        token -> isCommand(token) && !isToCommand(token),
        tokens -> {
          if (turtleCommands.contains(commandMap.get(tokens.get(myIndex).toLowerCase()))) {
            currentNode = new TurtleCommandNode(commandMap.get(tokens.get(myIndex).toLowerCase()),
                modelState);

          } else {
            currentNode = new CommandNode(commandMap.get(tokens.get(myIndex).toLowerCase()),
                modelState);
          }
        }));

    nodeHandler.add(new SimpleEntry<>(
        token -> tokenMatched(token, "Command") && isUserDefinedCommand(token),
        tokens -> currentNode = new UserCommandNode(tokens.get(myIndex).toLowerCase(),
            modelState)));
  }

  /**
   * Checks if alias of token matches to the "TO" command.
   */

  private boolean isToCommand(String token) {
    return commandMap.get(token.toLowerCase()).equals(TO_COMMAND);
  }

  /**
   * Checks if token is a SLOGO pre-defined command.
   */
  private boolean isCommand(String token) {
    return commandMap.containsKey(token.toLowerCase());
  }

  /**
   * Checks if token is a user-defined command.
   */

  private boolean isUserDefinedCommand(String token) {
    return modelState.getUserDefinedCommands().containsKey(token.toLowerCase());
  }

  /**
   * Checks if token matches the pattern specified by regex key.
   */
  private boolean tokenMatched(String token, String key) {
    return token.matches(patternLoader.getPattern(key));
  }

  /**
   * Initializes the token map, which maps token strings to handling functions. The token map is
   * used during parsing to determine how to handle each token.
   */

  private void initializeTokenMap() {
    tokenHandlers = new HashMap<>();
    tokenHandlers.put(OPEN_BRACKET, this::handleOpenBracket);
    tokenHandlers.put(CLOSED_BRACKET, this::handleClosedBracket);
  }


}
