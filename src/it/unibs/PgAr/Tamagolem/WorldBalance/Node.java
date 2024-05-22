package it.unibs.PgAr.Tamagolem.WorldBalance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Node class represents a node in a graph.
 */
public class Node {
  private String name;
  private HashMap<String, Integer> nodeConnections;

  // the following parameters are used only during the creation of the balance of
  // the world to skip some cycles to improve the performance ;)
  private Set<String> edgesToBeDone;
  private int edgesValueSum;

  /**
   * Constructs a new Node object with the given name.
   *
   * @param name the name of the node
   */
  public Node(String name) {
    this.name = name;
    this.nodeConnections = new HashMap<>();
    this.edgesToBeDone = new HashSet<>();
    this.edgesValueSum = 0;
  }

  /**
   * Adds a new edge to the node with the given name and value.
   *
   * @param name  the name of the edge
   * @param value the value of the edge
   */
  public void addNewEdge(String name, int value) {
    this.nodeConnections.put(name, value);
  }

  /**
   * Updates the value of the edge with the given name.
   *
   * @param name  the name of the edge
   * @param value the new value of the edge
   */
  public void updateEdgeValue(String name, int value) {
    this.edgesValueSum -= this.nodeConnections.get(name);
    this.nodeConnections.replace(name, value);
    this.edgesValueSum += value;
  }

  /**
   * Returns the set of edges that need to be processed.
   *
   * @return the set of edges to be done
   */
  public Set<String> getEdgesToBeDone() {
    return this.edgesToBeDone;
  }

  /**
   * Returns the map of node connections.
   *
   * @return the node connections map
   */
  public HashMap<String, Integer> getNodeConnections() {
    return this.nodeConnections;
  }

  /**
   * Returns the sum of all edge values.
   *
   * @return the sum of edge values
   */
  public int getEdgesValueSum() {
    return this.edgesValueSum;
  }

  /**
   * Sets the sum of all edge values.
   *
   * @param edgesValueSum the sum of edge values
   */
  public void setEdgesValueSum(int edgesValueSum) {
    this.edgesValueSum = edgesValueSum;
  }

  /**
   * Adds an edge to the set of edges to be done.
   *
   * @param name the name of the edge
   */
  public void addEdgesToBeDone(String name) {
    edgesToBeDone.add(name);
  }

  /**
   * Removes an edge from the set of edges to be done.
   *
   * @param name the name of the edge
   */
  public void removeEdgesToBeDone(String name) {
    this.edgesToBeDone.remove(name);
  }

  /**
   * Returns the name of the node.
   *
   * @return the name of the node
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value of the connection to the specified node.
   *
   * @param node the name of the connected node
   * @return the value of the connection
   */
  public int getConnectionValueForNode(String node) {
    return this.nodeConnections.get(node);
  }

  /**
   * Returns a string representation of the node.
   *
   * @return a string representation of the node
   */
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
