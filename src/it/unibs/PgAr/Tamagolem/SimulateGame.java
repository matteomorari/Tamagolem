package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.kibo.fp.lib.AnsiColors;
import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.unibs.PgAr.Tamagolem.WorldBalance.Node;
import it.unibs.PgAr.Tamagolem.WorldBalance.WorldBalance;

public class SimulateGame {
  private static final String MESSAGE_MENU = "Choose an element (%d/%d)";
  private static final String MESSAGE_NEW_TAMA = AnsiColors.GREEN + "%s, you have to evoke a new tamaGolem! (%d/%d)"+ AnsiColors.RESET;
  private static final String MESSAGE_TAMA_DIED = AnsiColors.RED + "%s, your tamaGolem has dead."+ AnsiColors.RESET;
  private static final String MESSAGE_GAME_LOST = AnsiColors.RED + "%s has lost the game."+ AnsiColors.RESET;
  private static final String MESSAGE_CREATE_SECOND_PLAYER = AnsiColors.GREEN + "Hi player 2, what's you name? "+ AnsiColors.RESET;
  private static final String MESSAGE_CREATE_FIRST_PLAYER = AnsiColors.GREEN + "Hi player 1, what's you name? "+ AnsiColors.RESET;
  private static final String MESSAGE_NEW_GAME = AnsiColors.GREEN + "Do you want to play again? "+ AnsiColors.RESET;
  private static HashMap<String, Node> worldBalanceMap;
  private static int numberOfElements = 9; // N
  private static int stonesPerTamaGolem; // P
  private static int tamaGolemPerPlayer; // G
  private static int reserveStones; // S //TODO: useless?
  private static int stonesPerElement;
  private static final int tamaGolemLife = 10; // V
  // TODO: because of there isn't a construcctor, is good put hear the
  // initializer? @ask
  private static HashMap<String, Integer> stonesAvailable = new HashMap<>();
  private static Player player1;
  private static Player player2;

  public static void starSimulation() {
    // TODO: how to choose numberOfElements?
    WorldBalance worldBalance = new WorldBalance(numberOfElements);
    worldBalanceMap = worldBalance.getWorldBalance();
    calculateParameters();
    createPlayers();
    boolean newGame = true;
    do {
      System.out.println();
      initializationStonesAvailable();
      newGame();
      System.out.println(worldBalance.toString());
      newGame = InputData.readYesOrNo(MESSAGE_NEW_GAME);
    } while (newGame);
  }

  public static void calculateParameters() {
    stonesPerTamaGolem = (int) Math.ceil((numberOfElements + 1) / 3.0) + 1;
    tamaGolemPerPlayer = (int) Math.ceil((numberOfElements - 1) * (numberOfElements - 2) / (stonesPerTamaGolem * 2.0));
    reserveStones = (int) Math.ceil((2.0 * tamaGolemPerPlayer * stonesPerTamaGolem) / numberOfElements)
        * numberOfElements;
    stonesPerElement = (int) Math.ceil((2.0 * tamaGolemPerPlayer * stonesPerTamaGolem) / numberOfElements);
  }

  private static void createPlayers() {
    String name1 = InputData.readNonEmptyString(MESSAGE_CREATE_FIRST_PLAYER, false);
    player1 = new Player(name1, tamaGolemPerPlayer);
    String name2 = InputData.readNonEmptyString(MESSAGE_CREATE_SECOND_PLAYER, false);
    player2 = new Player(name2, tamaGolemPerPlayer);
  }

  private static void initializationStonesAvailable() {
    for (String elementName : worldBalanceMap.keySet()) {
      stonesAvailable.put(elementName, stonesPerElement);
    }
  }

  private static void newGame() {
    // lets star with choose the first tamaGolem
    boolean player1Alive = true;
    boolean player2Alive = true;
    tamaGolemEvocation(player1);
    tamaGolemEvocation(player2);
    while (player1Alive && player2Alive) {
      startTamaGolemFight();

      // first check if any player has finished his tamaGolem
      if (player1.tamaGolemFinished()) {
        player1Alive = false;
        System.out.println(String.format(MESSAGE_GAME_LOST, player1.getName()));
      } else if (player2.tamaGolemFinished()) {
        player1Alive = false;
        System.out.println(String.format(MESSAGE_GAME_LOST, player2.getName()));
      } else {
        // if no player has lost, let's look for the player whose last tamaGolem died
        if (player1.getTamaGolem().isDead()) {
          tamaGolemEvocation(player1);
        }
        if (player2.getTamaGolem().isDead()) {
          tamaGolemEvocation(player2);
        }
      }
    }
  }

  private static void tamaGolemEvocation(Player player) {
    System.out.println(String.format(MESSAGE_NEW_TAMA, player.getName(), player.getTamaGolemUsed() + 1, player.getTotalTamaGolemUsable()));

    ArrayList<String> tamaGolemStones = new ArrayList<String>();
    // display and choose element
    for (int i = 0; i < stonesPerTamaGolem; i++) {
      // create menu array with only the element with at least a stone available
      ArrayList<String> menuOptions = new ArrayList<String>();
      for (Map.Entry<String, Integer> currentElement : stonesAvailable.entrySet()) {
        if (currentElement.getValue() != 0) {
          menuOptions.add(currentElement.getKey() + ": " + currentElement.getValue());
        }
      }
      // convert from ArrayList to simple array
      String[] arrayOptsMenu = new String[menuOptions.size()];
      arrayOptsMenu = menuOptions.toArray(arrayOptsMenu); // TODO: better way @ask
      // display menu
      Menu menu = new Menu(String.format(MESSAGE_MENU, i + 1, stonesPerTamaGolem), arrayOptsMenu, false, false, false);
      int numElementChosen = menu.choose();
      // extraction element name from the string
      String nameElementChosen = menuOptions.get(numElementChosen - 1).split(":")[0];
      // decrease by one the stone disposability for the element chosen
      stonesAvailable.compute(nameElementChosen, (k, v) -> v - 1);
      tamaGolemStones.add(nameElementChosen);
    }
    System.out.println();
    TamaGolem newTamaGolem = new TamaGolem(tamaGolemLife, tamaGolemStones);
    player.setTamaGolem(newTamaGolem);
  }

  private static void startTamaGolemFight() {
    TamaGolem tamaGolemPlayer1 = player1.getTamaGolem();
    TamaGolem tamaGolemPlayer2 = player2.getTamaGolem();
    // TODO!: what to do in this case? @ask
    int hasCode1 = player1.getTamaGolem().getStonesAvailableHashCode();
    int hasCode2 = player2.getTamaGolem().getStonesAvailableHashCode();
    while (!tamaGolemPlayer1.isDead() && !tamaGolemPlayer2.isDead()) {
      String stone1 = tamaGolemPlayer1.executeAttack();
      String stone2 = tamaGolemPlayer2.executeAttack();
      int valueInteractionElements = worldBalanceMap.get(stone1).getConnectionValueForNode(stone2);
      if (valueInteractionElements < 0) {
        tamaGolemPlayer1.reduceLife(valueInteractionElements);
      } else {
        tamaGolemPlayer2.reduceLife(valueInteractionElements * -1);
      }
    }

    if (player1.getTamaGolem().isDead()) {
      System.out.println(String.format(MESSAGE_TAMA_DIED, player1.getName()));
    }
    if (player2.getTamaGolem().isDead()) {
      System.out.println(String.format(MESSAGE_TAMA_DIED, player2.getName()));
    }
    System.out.println();
  }
}
