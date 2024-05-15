package it.unibs.PgAr.Tamagolem.Graph;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

import java.util.Map;

public class GraphTest {

  @Test
  public void testCreateWorldBalance() {
    for (int i = 0; i < 1_000; i++) {
      System.err.println(i);
      Graph graph = new Graph(5);
      for (Map.Entry<String, Node> startingNode : graph.getGraph().entrySet()) {
        if (startingNode.getValue().getEdgesValueSum() != 0) {
          System.out.println(startingNode.getKey());
        }
        assertEquals(0, startingNode.getValue().getEdgesValueSum());
        assertEquals(0, startingNode.getValue().getEdgesToBeDone().size());
        for (Map.Entry<String, Integer> childNode : startingNode.getValue().getNodeConnections().entrySet()) {
          if (!startingNode.getValue().getName().equals(childNode.getKey())) {
            if (childNode.getValue() == 0) {
              System.out.println(startingNode.getKey() + ", " + childNode.getKey());
            }
            assertNotEquals(0, (int) childNode.getValue());
          }
        }
      }
    }
  }

}