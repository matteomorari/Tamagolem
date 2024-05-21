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
      for (Map.Entry<String, Node> startingNode : graph.getWorldBalance().entrySet()) {
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