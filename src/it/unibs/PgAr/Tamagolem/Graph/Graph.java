package it.unibs.PgAr.Tamagolem.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.kibo.fp.lib.RandomDraws;
import it.unibs.PgAr.Tamagolem.MyMath;

public class Graph {
  private static final int MAX = 5;
  private static final int MIN = -5;
  private Map<String, Node> graph;
  private ArrayList<String> elementListName = new ArrayList<>();
  private int numberOfElements;

  public Graph(int numberOfElements) {
    this.numberOfElements = numberOfElements;
    this.graph = new HashMap<>();
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
      // int randomNumber = RandomDraws.drawInteger(0, this.elementListName.size() -
      // 1);
      String newNodeName = this.elementListName.get(0);
      Node newNode = new Node(newNodeName);
      this.graph.put(newNodeName, newNode);
      this.elementListName.remove(0);
    }

    // TODO: Map.Entry or just Entry?
    // create empty edges for each node with each node with value zero
    for (Map.Entry<String, Node> startingNode : this.graph.entrySet()) {
      for (Map.Entry<String, Node> arrivalNode : this.graph.entrySet()) {
        startingNode.getValue().addNewEdge(arrivalNode.getKey(), 0);
        // the node referred to its self must stay to zero
        if (startingNode.getKey() != arrivalNode.getKey()) {
          startingNode.getValue().addEdgesToBeDone(arrivalNode.getKey());
        }
      }
    }

    // after set all to zero lets generate random value for edges with condition
    // that each node has zero as sum of edges
    int numberOfLeftNodes = this.elementListName.size();
    for (Map.Entry<String, Node> startingNode : this.graph.entrySet()) {

      Iterator<String> iteratorArrivingNodes = startingNode.getValue().getEdgesToBeDone().iterator();

      while (iteratorArrivingNodes.hasNext()) {
        String arrivalNodeName = iteratorArrivingNodes.next();
        Node arrivalNode = this.graph.get(arrivalNodeName);
        int randomEdgeValue;
        if (numberOfLeftNodes > 3) {
          // we check that the penultimate edge is not already a total sum of zero because
          // otherwise, given that the last edge cannot be zero, the final sum of all the
          // edges of a node cannot be zero
          // TODO: dobbiamo controllare anche quello di arrivo ma l'importante che non sia
          // l'ultimo di partenza
          if (startingNode.getValue().getEdgesToBeDone().size() == 2) {
            do {
              randomEdgeValue = MyMath.drawIntegerWithExclusion(MIN, MAX, 0);
            } while (startingNode.getValue().getEdgesValueSum() == (randomEdgeValue * -1));
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
          int valueLastEdgeThirdLastNode;
          int valueLastEdgeSecondLastNode;
          int valueLastEdgeLastNode;
          do {
            randomEdgeValue = MyMath.drawIntegerWithExclusion(MIN, MAX, 0);
            valueLastEdgeThirdLastNode = (this.graph.get(thirdLastNode).getEdgesValueSum() + randomEdgeValue) * -1;
            valueLastEdgeSecondLastNode = ((this.graph.get(secondLastNode).getEdgesValueSum() + randomEdgeValue * -1) * -1);
            valueLastEdgeLastNode = (this.graph.get(lastNode).getEdgesValueSum() + randomEdgeValue) * -1;
          } while (valueLastEdgeThirdLastNode == 0 || valueLastEdgeSecondLastNode == 0 || valueLastEdgeLastNode == 0 || randomEdgeValue == 0);
          // update
          this.graph.get(thirdLastNode).updateEdgeValue(secondLastNode, randomEdgeValue);
          this.graph.get(secondLastNode).updateEdgeValue(thirdLastNode, randomEdgeValue * -1);
          // remove
          this.graph.get(secondLastNode).removeEdgesToBeDone(thirdLastNode);

          // update
          this.graph.get(thirdLastNode).updateEdgeValue(lastNode, valueLastEdgeThirdLastNode);
          this.graph.get(lastNode).updateEdgeValue(thirdLastNode, valueLastEdgeThirdLastNode * -1);
          // remove
          this.graph.get(lastNode).removeEdgesToBeDone(thirdLastNode);

          // update
          this.graph.get(secondLastNode).updateEdgeValue(lastNode, valueLastEdgeSecondLastNode);
          this.graph.get(lastNode).updateEdgeValue(secondLastNode, valueLastEdgeSecondLastNode * -1);
          // remove
          this.graph.get(lastNode).removeEdgesToBeDone(secondLastNode);
          this.graph.get(secondLastNode).removeEdgesToBeDone(lastNode);

        }
      }

      numberOfLeftNodes--;
    }

    for (Map.Entry<String, Node> startingNode : this.graph.entrySet()) {
      System.out.println(startingNode.getValue().toString());
    }
  }

  public Map<String, Node> getGraph() {
    return graph;
  }

  // TODO: create the toString @riki
  // some suggestions (to follow ty): create a matrix where the rows are the
  // starting node and the columns are the arrival node
  // @Override
  // public String toString() {

  // }

}
