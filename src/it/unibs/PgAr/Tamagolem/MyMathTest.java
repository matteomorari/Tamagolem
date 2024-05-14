package it.unibs.PgAr.Tamagolem;

import static org.junit.Assert.*;
import org.junit.Test;

public class MyMathTest {

  @Test
  public void testDrawIntegerWithExclusion() {
    // Test when minimum and maximum are the same
    int result = MyMath.drawIntegerWithExclusion(1, 5, 1, 2, 3, 4);
    assertEquals(5, result);
  }

}