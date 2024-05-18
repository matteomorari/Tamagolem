package it.unibs.PgAr.Tamagolem.WorldBalance;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.kibo.fp.lib.RandomDraws;
import it.unibs.PgAr.Tamagolem.MyMath;

public class WorldBalance {
  private static final int MAX = 5;
  private static final int MIN = -5;
  private HashMap<String, Node> worldBalance;
  private ArrayList<String> elementListName = new ArrayList<>();
  private int numberOfElements;

  public WorldBalance(int numberOfElements) {
    if (numberOfElements < 3 || numberOfElements > 10) {
      throw new InvalidParameterException("The number of nods must be at least 3 and no more than 10");
    }
    this.numberOfElements = numberOfElements;
    this.worldBalance = new HashMap<>();
    this.createElementListName();
    this.createWorldBalance();
  }

  private void createElementListName() {
    this.elementListName.add("Bug");
    this.elementListName.add("Fire");
    this.elementListName.add("Fighting");
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
    for (int i = 0; i < this.numberOfElements; i++) {
      int randomNumber = RandomDraws.drawInteger(0, this.elementListName.size() - 1);
      String newNodeName = this.elementListName.get(randomNumber);
      Node newNode = new Node(newNodeName);
      this.worldBalance.put(newNodeName, newNode);
      this.elementListName.remove(randomNumber);
    }

    // TODO: Map.Entry or just Entry?
    // create empty edges for each node with each node with value zero
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {
      for (Map.Entry<String, Node> arrivalNode : this.worldBalance.entrySet()) {
        startingNode.getValue().addNewEdge(arrivalNode.getKey(), 0);
        // the node referred to its self must stay to zero
        if (startingNode.getKey() != arrivalNode.getKey()) {
          startingNode.getValue().addEdgesToBeDone(arrivalNode.getKey());
        }
      }
    }

    // after set all to zero lets generate random value for edges with condition
    // that each node has zero as sum of edges
    int numberOfLeftNodes = this.numberOfElements;
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {

      Iterator<String> iteratorArrivingNodes = startingNode.getValue().getEdgesToBeDone().iterator();

      while (iteratorArrivingNodes.hasNext()) {
        String arrivalNodeName = iteratorArrivingNodes.next();
        Node arrivalNode = this.worldBalance.get(arrivalNodeName);
        int randomEdgeValue;
        if (numberOfLeftNodes > 3) {
          // we check that the penultimate edge is not already a total sum of zero because
          // otherwise, given that the last edge cannot be zero, the final sum of all the
          // edges of a node cannot be zero
          // TODO: dobbiamo controllare anche quello di arrivo ma l'importante che non sia
          // l'ultimo di partenza
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
        }

        if (numberOfLeftNodes == 3) {
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
          // remove
          this.worldBalance.get(secondLastNode).removeEdgesToBeDone(thirdLastNode);

          // update
          this.worldBalance.get(thirdLastNode).updateEdgeValue(lastNode, valueLastEdgeThirdLastNode);
          this.worldBalance.get(lastNode).updateEdgeValue(thirdLastNode, valueLastEdgeThirdLastNode * -1);
          // remove
          this.worldBalance.get(lastNode).removeEdgesToBeDone(thirdLastNode);

          // update
          this.worldBalance.get(secondLastNode).updateEdgeValue(lastNode, valueLastEdgeSecondLastNode);
          this.worldBalance.get(lastNode).updateEdgeValue(secondLastNode, valueLastEdgeSecondLastNode * -1);
          // remove
          this.worldBalance.get(lastNode).removeEdgesToBeDone(secondLastNode);
          this.worldBalance.get(secondLastNode).removeEdgesToBeDone(lastNode);
        }
      }

      numberOfLeftNodes--;
    }
  }

  public Map<String, Node> getWorldBalance() {
    return worldBalance;
  }

  // TODO: create the toString @riki
  // some suggestions (to follow ty): create a matrix where the rows are the
  // starting node and the columns are the arrival node
  @Override
  public String toString() {
    StringBuffer stringToReturn = new StringBuffer();
    for (Map.Entry<String, Node> startingNode : this.worldBalance.entrySet()) {
      stringToReturn.append(startingNode.getValue().toString());
      stringToReturn.append("\n");
    }
    return stringToReturn.toString();
  }
}