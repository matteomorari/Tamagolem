package it.unibs.PgAr.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import it.unibs.PgAr.Tamagolem.WorldBalance.Node;
import it.unibs.PgAr.Tamagolem.WorldBalance.WorldBalance;

import java.util.Map;

public class GraphTest {

  @Test
  public void testCreateWorldBalance() {
    for (int i = 0; i < 100_000; i++) {
      WorldBalance graph = new WorldBalance(10);

      // Iterate over each starting node in the WorldBalance
      for (Map.Entry<String, Node> startingNode : graph.getWorldBalance().entrySet()) {
        if (startingNode.getValue().getEdgesValueSum() != 0) {
          System.out.println(startingNode.getKey());
        }

        assertEquals(0, startingNode.getValue().getEdgesValueSum());

        assertEquals(0, startingNode.getValue().getEdgesToBeDone().size());

        // Iterate over each child node connected to the starting node
        for (Map.Entry<String, Integer> childNode : startingNode.getValue().getNodeConnections().entrySet()) {
          // Check if the child node is not the same as the starting node
          if (!startingNode.getValue().getName().equals(childNode.getKey())) {
            // Check if the value of the edge is 0
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
