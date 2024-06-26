package it.unibs.PgAr.Tamagolem.WorldBalance;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.kibo.fp.lib.RandomDraws;
import it.unibs.PgAr.Tamagolem.MyMath;

/**
 * The WorldBalance class represents the world balance in the Tamagolem game.
 * It manages the creation and maintenance of the balance between different
 * elements in the game world.
 */
public class WorldBalance {
  private static final String MESSAGE_ERROR_CONSTRUCTOR = "The number of nodes must be at least 3 and no more than 10";
  private static final int MAX = 4;
  private static final int MIN = -4;
  private HashMap<String, Node> worldBalance;
  private ArrayList<String> elementListName = new ArrayList<>();
  private int numberOfElementsChosen;
  private int supremum;

  /**
   * Constructs a new WorldBalance object with the specified number of elements.
   * 
   * @param numberOfElements The number of elements to be chosen.
   * @throws InvalidParameterException if the number of elements is less than 3 or
   *                                   greater than 10.
   */
  public WorldBalance(int numberOfElements) {
    if (numberOfElements < 3 || numberOfElements > 10) {
      throw new InvalidParameterException(MESSAGE_ERROR_CONSTRUCTOR);
    }
    this.supremum = 0;
    this.numberOfElementsChosen = numberOfElements;
    this.worldBalance = new HashMap<>();
    this.createElementListName();
    this.createWorldBalance();
  }

  /**
   * Adds the names of elements to the elementListName.
   * The names of the elements are: Bug, Fire, Ghost, Water, Dark, Grass, Poison,
   * Rock, Ice, and Dragon.
   */
  private void createElementListName() {
    this.elementListName.add("Bug");
    this.elementListName.add("Fire");
    this.elementListName.add("Ghost");
    this.elementListName.add("Water");
    this.elementListName.add("Dark");
    this.elementListName.add("Grass");
    this.elementListName.add("Poison");
    this.elementListName.add("Rock");
    this.elementListName.add("Ice");
    this.elementListName.add("Dragon");
  }

  private void createWorldBalance() {
    // create each node
    for (int i = 0; i < this.numberOfElementsChosen; i++) {
      int randomNumber = RandomDraws.drawInteger(0, this.elementListName.size() - 1);
      String newNodeName = this.elementListName.get(randomNumber);
      Node newNode = new Node(newNodeName);
      this.worldBalance.put(newNodeName, newNode);
      this.elementListName.remove(randomNumber);
    }

    // For each node initializes the link to every other node with value zero
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {
      for (Map.Entry<String, Node> arrivalNode : this.worldBalance.entrySet()) {
        startingNode.getValue().addNewEdge(arrivalNode.getKey(), 0);
        // the node referred to its self must stay to zero, so in the set the edges that
        // will be update, we don't put the node itself
        if (startingNode.getKey() != arrivalNode.getKey()) {
          startingNode.getValue().addEdgesToBeDone(arrivalNode.getKey());
        }
      }
    }

    // after set all to zero lets generate random value for edges with condition
    // that each node has zero as sum of edges
    int numberOfStartingNodesLeft = this.numberOfElementsChosen;
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {

      Iterator<String> iteratorArrivingNodes = startingNode.getValue().getEdgesToBeDone().iterator();

      while (iteratorArrivingNodes.hasNext()) {
        String arrivalNodeName = iteratorArrivingNodes.next();
        Node arrivalNode = this.worldBalance.get(arrivalNodeName);
        int randomEdgeValue;
        if (numberOfStartingNodesLeft > 3) {
          // we check that the penultimate edge is not already a total sum of zero because
          // otherwise, given that the last edge cannot be zero, the final sum of all the
          // edges of a node cannot be zero
          if (startingNode.getValue().getEdgesToBeDone().size() == 2) {
            int numberToAvoid = startingNode.getValue().getEdgesValueSum() * -1;
            randomEdgeValue = MyMath.drawIntegerWithExclusion(MIN, MAX, 0, numberToAvoid);
          } else if (!iteratorArrivingNodes.hasNext()) {
            // for the last edge
            randomEdgeValue = startingNode.getValue().getEdgesValueSum() * -1; // even if its not random, lol
          } else {
            randomEdgeValue = MyMath.drawIntegerWithExclusion(MIN, MAX, 0);
          }

          // update the edge value for the starting node
          startingNode.getValue().updateEdgeValue(arrivalNodeName, randomEdgeValue);
          // also update the value for the arrival node changing the sign
          arrivalNode.updateEdgeValue(startingNode.getKey(), randomEdgeValue * -1);
          // remove from the attribute edgesToBeDone in the class Node for both
          // startingNode and arrivalNode
          iteratorArrivingNodes.remove();
          arrivalNode.removeEdgesToBeDone(startingNode.getKey());
          this.updateSupremum(randomEdgeValue);
        }

        // at the third last node, we manually get the next two nodes
        if (numberOfStartingNodesLeft == 3) {
          String thirdLastNode = startingNode.getKey();
          String secondLastNode = arrivalNodeName;
          iteratorArrivingNodes.remove();
          String lastNode = iteratorArrivingNodes.next();
          iteratorArrivingNodes.remove();
          int valueSecondLastEdgeThirdLastNode;
          int valueLastEdgeThirdLastNode;
          int valueLastEdgeSecondLastNode;
          do {
            // valueSecondLastEdgeThirdLastNode with the second last node (with the sign for
            // the third last)
            valueSecondLastEdgeThirdLastNode = MyMath.drawIntegerWithExclusion(MIN, MAX, 0);
            // valueLastEdgeThirdLastNode with the last node (with the sign for the third
            // last)
            valueLastEdgeThirdLastNode = (this.worldBalance.get(thirdLastNode).getEdgesValueSum()
                + valueSecondLastEdgeThirdLastNode) * -1;
            // valueLastEdgeSecondLastNode with the last node (with the sign for the second
            // last)
            valueLastEdgeSecondLastNode = ((this.worldBalance.get(secondLastNode).getEdgesValueSum()
                + valueSecondLastEdgeThirdLastNode * -1) * -1);

            // If 'valueLastEdgeThirdLastNode' is different from zero means that
            // valueSecondLastEdgeThirdLastNode is not equals to the opposite of the sum of
            // the previous edges (for the third last node).
            // If 'valueLastEdgeSecondLastNode' is different from zero means that
            // valueSecondLastEdgeThirdLastNode is not equals to to the opposite of the sum
            // of the previous edges (for the second last node).
          } while (valueLastEdgeThirdLastNode == 0 || valueLastEdgeSecondLastNode == 0);
          // update
          this.worldBalance.get(thirdLastNode).updateEdgeValue(secondLastNode, valueSecondLastEdgeThirdLastNode);
          this.worldBalance.get(secondLastNode).updateEdgeValue(thirdLastNode, valueSecondLastEdgeThirdLastNode * -1);
          this.updateSupremum(valueSecondLastEdgeThirdLastNode);
          // remove
          this.worldBalance.get(secondLastNode).removeEdgesToBeDone(thirdLastNode);

          // update
          this.worldBalance.get(thirdLastNode).updateEdgeValue(lastNode, valueLastEdgeThirdLastNode);
          this.worldBalance.get(lastNode).updateEdgeValue(thirdLastNode, valueLastEdgeThirdLastNode * -1);
          this.updateSupremum(valueLastEdgeThirdLastNode);
          // remove
          this.worldBalance.get(lastNode).removeEdgesToBeDone(thirdLastNode);

          // update
          this.worldBalance.get(secondLastNode).updateEdgeValue(lastNode, valueLastEdgeSecondLastNode);
          this.worldBalance.get(lastNode).updateEdgeValue(secondLastNode, valueLastEdgeSecondLastNode * -1);
          this.updateSupremum(valueLastEdgeSecondLastNode);
          // remove
          this.worldBalance.get(lastNode).removeEdgesToBeDone(secondLastNode);
          this.worldBalance.get(secondLastNode).removeEdgesToBeDone(lastNode);
        }
      }

      numberOfStartingNodesLeft--;
    }
  }

  private void updateSupremum(int value) {
    if (Math.abs(value) > this.supremum) {
      this.supremum = value;
    }
  }

  /**
   * Returns the supremum value.
   *
   * @return the supremum value
   */
  public int getSupremum() {
    return supremum;
  }

  /**
   * Returns the world balance.
   *
   * @return the world balance as a HashMap of String and Node objects.
   */
  public HashMap<String, Node> getWorldBalance() {
    return worldBalance;
  }

  /**
   * Returns a string representation of the WorldBalance object.
   * The string includes the starting nodes and their connections.
   *
   * @return a string representation of the WorldBalance object
   */
  @Override
  public String toString() {
    StringBuffer stringToReturn = new StringBuffer(String.format("%-12s", ""));
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {
      stringToReturn.append(String.format("%-12s", startingNode.getKey()));
    }

    stringToReturn.append("\n");
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {
      stringToReturn.append(String.format("%-12s", startingNode.getKey()));
      for (Map.Entry<String, Integer> arrivalNode : startingNode.getValue().getNodeConnections().entrySet()) {
        stringToReturn.append(String.format("%-12d", arrivalNode.getValue()));
      }
      stringToReturn.append("\n");
    }

    return stringToReturn.toString();
  }
}
