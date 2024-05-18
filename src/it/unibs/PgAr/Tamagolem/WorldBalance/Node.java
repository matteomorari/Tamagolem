package it.unibs.PgAr.Tamagolem.WorldBalance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node {
  private String name;
  private HashMap<String, Integer> nodeConnections;

  // the following parameters are used only during the creation of the balance of
  // the world to skip some cycles to improve the performance ;)
  private Set<String> edgesToBeDone;
  private int edgesValueSum;

  public Node(String name) {
    this.name = name;
    this.nodeConnections = new HashMap<>();
    this.edgesToBeDone = new HashSet<>();
    this.edgesValueSum = 0;
  }

  public void addNewEdge(String name, int value) {
    this.nodeConnections.put(name, value);
  }

  public void updateEdgeValue(String name, int value) {
    this.edgesValueSum -= this.nodeConnections.get(name);
    this.nodeConnections.replace(name, value);
    this.edgesValueSum += value;
  }

  public Set<String> getEdgesToBeDone() {
    return this.edgesToBeDone;
  }

  public Map<String, Integer> getNodeConnections() {
    return this.nodeConnections;
  }

  public int getEdgesValueSum() {
    return this.edgesValueSum;
  }

  public void setEdgesValueSum(int edgesValueSum) {
    this.edgesValueSum = edgesValueSum;
  }

  public void addEdgesToBeDone(String name) {
    edgesToBeDone.add(name);
  }

  public void removeEdgesToBeDone(String name) {
    this.edgesToBeDone.remove(name);
  }

  public String getName() {
    return name;
  }

  // // TODO: change name
  // public void createRandomEdge() {
  // ArrayList<Integer> edgesToBeDone = new ArrayList<>();
  // int edgesValueSum = 0;
  // for (Map.Entry<Integer, Integer> currentNode :
  // this.nodeConnections.entrySet()) {
  // // TODO: add comment
  // if (this.name.hashCode() != currentNode.getKey()) {
  // if (currentNode.getValue() != 0) {
  // edgesValueSum += currentNode.getValue();
  // } else {
  // edgesToBeDone.add(currentNode.getKey());
  // }
  // }
  // }

  // // TODO: iterator?
  // for (int i = 0; i < edgesToBeDone.size(); i++) {
  // if (i == edgesToBeDone.size() - 1) {
  // this.nodeConnections.replace(edgesToBeDone.get(i), edgesValueSum);
  // // TODO: change also the other node
  // } else {
  // int randomEdgeValue = RandomDraws.drawInteger(-5, 5);
  // this.nodeConnections.replace(edgesToBeDone.get(i), randomEdgeValue);
  // edgesValueSum += randomEdgeValue;
  // // TODO: change also the other node
  // }
  // }
  // }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public String toString() {
    StringBuffer stringToReturn = new StringBuffer();
    stringToReturn.append("The connection of the node " + this.name + " are:");
    for (Map.Entry<String, Integer> currentNode : nodeConnections.entrySet()) {
      stringToReturn.append("\n\t- " + currentNode.getKey() + ": " + currentNode.getValue());
    }
    return stringToReturn.toString();
  }
}
