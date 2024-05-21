package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.kibo.fp.lib.RandomDraws;

public final class MyMath {

  /**
   * Generates a random integer within a specified range, excluding certain numbers.
   *
   * @param minimum the minimum value of the range (inclusive)
   * @param maximum the maximum value of the range (inclusive)
   * @param numbersToExclude the numbers to be excluded from the range
   * @return a random integer within the specified range, excluding the specified numbers
   * @throws IllegalArgumentException if all numbers in the range are excluded
   */
  public static int drawIntegerWithExclusion(int minimum, int maximum, Integer... numbersToExclude) {
    int randomEdgeValue;
    List<Integer> listNumbersToExclude = Arrays.asList(numbersToExclude);
    ArrayList<Integer> listNumberExtractable = new ArrayList<Integer>();

    for (int i = minimum; i <= maximum; i++) {
      if (!listNumbersToExclude.contains(i)) {
        listNumberExtractable.add(i);
      }
    }
    if (listNumberExtractable.size() == 0) {
      throw new IllegalArgumentException("All numbers in the range are excluded.");
    }

    randomEdgeValue = RandomDraws.drawInteger(0, listNumberExtractable.size() - 1);

    return listNumberExtractable.get(randomEdgeValue);
  }
}
