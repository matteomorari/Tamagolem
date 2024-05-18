package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.unibs.PgAr.Tamagolem.WorldBalance.Node;
import it.unibs.PgAr.Tamagolem.WorldBalance.WorldBalance;

public class SimulateGame {
  private static Map<String, Node> worldBalance;
  private static int numberOfElements = 5; // N
  private static int stonesPerTamaGolem; // P
  private static int tamaGolemPerPlayer; // G
  private static int reserveStones; // S //TODO: useless?
  private static int stonesPerElement;
  private static HashMap<String, Integer> stonesAvailable = new HashMap<>(); // TODO: because of there isn't a
                                                                             // construcctor, is good put hear the
                                                                             // initializer? @ask
  private static final int tamaGolemLife = 10; // V
  private static Player player1;
  private static Player player2;

  public static void starGame() {
    worldBalance = new WorldBalance(5).getWorldBalance();
    calculateParameters();
    createPlayers();
    initializationStonesAvailable();
    tamaGolemEvocation(player1);
    tamaGolemEvocation(player2);
  }

  public static void calculateParameters() {
    stonesPerTamaGolem = (int) Math.ceil((numberOfElements + 1) / 3) + 1;
    tamaGolemPerPlayer = (int) Math.ceil((numberOfElements - 1) * (numberOfElements - 2) / (stonesPerTamaGolem * 2));
    reserveStones = (int) Math.ceil((2 * tamaGolemPerPlayer * stonesPerTamaGolem) / numberOfElements)
        * numberOfElements;
    stonesPerElement = (int) Math.ceil((2 * tamaGolemPerPlayer * stonesPerTamaGolem) / numberOfElements);
  }

  private static void createPlayers() {
    String name1 = InputData.readNonEmptyString("Hi player 1, what's you name? ", false);
    player1 = new Player(name1, tamaGolemPerPlayer);
    String name2 = InputData.readNonEmptyString("Hi player 2, what's you name? ", false);
    player2 = new Player(name2, tamaGolemPerPlayer);
  }

  private static void initializationStonesAvailable() {
    for (String elementName : worldBalance.keySet()) {
      stonesAvailable.put(elementName, stonesPerElement);
    }

  }

  private static void tamaGolemEvocation(Player player) {
    System.out.println(player.getName() + ", you have to evoke a new tamaGolem ()!");

    ArrayList<String> tamaGolemStones = new ArrayList<String>();
    // display adn choose element
    for (int i = 0; i < stonesPerTamaGolem; i++) {
      // create menu array
      ArrayList<String> menuOptions = new ArrayList<String>();
      // int n = 0;
      for (Map.Entry<String, Integer> currentElement : stonesAvailable.entrySet()) {
        if (currentElement.getValue() != 0) {
          menuOptions.add(currentElement.getKey() + ": " + currentElement.getValue());
          // n++;
        }
      }
      String[] arrayOptsMenu = new String[stonesPerTamaGolem];
      arrayOptsMenu = menuOptions.toArray(arrayOptsMenu); //TODO: better way @ask
      Menu menu = new Menu("Choose an element", arrayOptsMenu, false, false, false);
      int numElementChosen = menu.choose();
      String nameElementChosen = menuOptions.get(numElementChosen - 1).split(":")[0];
      tamaGolemStones.add(nameElementChosen);
      stonesAvailable.compute(nameElementChosen, (k, v) -> v - 1);
    }
    TamaGolem newTamaGolem = new TamaGolem(tamaGolemLife, tamaGolemStones);
    player.setTamaGolem(newTamaGolem);
  }

}
