package it.unibs.PgAr.Tamagolem;

import it.kibo.fp.lib.RandomDraws;

public final class MyMath {

  public static int drawIntegerWithExclusion(int minimum, int maximum, int... numbersToExclude) {
    int randomEdgeValue;
    do {
      randomEdgeValue = RandomDraws.drawInteger(minimum, maximum);
    } while (isNumberInArray(randomEdgeValue, numbersToExclude));
    return randomEdgeValue;
  }

  private static boolean isNumberInArray(int number, int[] numbersToExclude) {
    for (int i = 0; i < numbersToExclude.length; i++) {
      if (number == numbersToExclude[i]) {
        return true;
      }
    }
    return false;
  }

}
