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

  @Test
  public void testError(){
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      MyMath.drawIntegerWithExclusion(1, 2, 1, 2);
    });

    String expectedMessage = "All numbers in the range are excluded.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

}