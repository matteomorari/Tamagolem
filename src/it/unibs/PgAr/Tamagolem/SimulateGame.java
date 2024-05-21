package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.kibo.fp.lib.AnsiColors;
import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.unibs.PgAr.Tamagolem.WorldBalance.WorldBalance;

public class SimulateGame {
  private static final String MESSAGE_TAMAGOLEM_EQUALS = AnsiColors.YELLOW
      + "You have created a tamaGolem with the same sequence of stones than the other tamaGolem. You have to recreate it."
      + AnsiColors.RESET;
  private static final String MESSAGE_INTERACTION_STONES = "-> %s Vs %s: win %s";
  private static final String MESSAGE_INTERACTION_STONES_TIE = "-> %s Vs %s: tie";
  private static final String MESSAGE_MENU = "Choose an element (%d/%d)";
  private static final String MESSAGE_NEW_TAMA = AnsiColors.GREEN + "%s, you have to evoke a new tamaGolem! (%d/%d)"
      + AnsiColors.RESET;
  private static final String MESSAGE_TAMA_DIED = AnsiColors.RED + "%s, your tamaGolem has dead." + AnsiColors.RESET;
  private static final String MESSAGE_GAME_LOST = AnsiColors.RED + "%s has lost the game.\n" + AnsiColors.RESET;
  private static final String MESSAGE_CREATE_SECOND_PLAYER = AnsiColors.GREEN + "Hi player 2, what's you name? "
      + AnsiColors.RESET;
  private static final String MESSAGE_CREATE_FIRST_PLAYER = AnsiColors.GREEN + "\nHi player 1, what's you name? "
      + AnsiColors.RESET;
  private static final String MESSAGE_NEW_GAME = AnsiColors.GREEN + "Do you want to play again? " + AnsiColors.RESET;

  private static int numberOfElements; // N
  private static int stonesPerTamaGolem; // P
  private static int tamaGolemPerPlayer; // G
  private static int stonesPerElement;
  private static WorldBalance worldBalance;
  private static HashMap<String, Integer> stonesAvailable;
  private static Player player1;
  private static Player player2;

  public static void starSimulation() {
    boolean newGame = true;
    do {
      chooseDifficulty();
      worldBalance = new WorldBalance(numberOfElements);
      calculateParameters();
      createPlayers();
      System.out.println();
      initializationStonesAvailable();
      newGame();
      System.out.println(worldBalance.toString());
      newGame = InputData.readYesOrNo(MESSAGE_NEW_GAME);
    } while (newGame);
  }

  public static void chooseDifficulty() {
    String[] menuOptions = {
        "Easy", "Medium", "Hard", "Extreme", "Impossible"
    };
    Menu menuDifficulty = new Menu("Choose the difficulty of the game", menuOptions, false, false, false);
    int difficulty = menuDifficulty.choose();
    switch (difficulty) {
      case 1:
        numberOfElements = 4;
        break;
      case 2:
        numberOfElements = 6;
        break;
      case 3:
        numberOfElements = 8;
        break;
      case 4:
        numberOfElements = 9;
        break;
      case 5:
        numberOfElements = 10;
        break;
      default:
        break;
    }
  }

  public static void calculateParameters() {
    stonesPerTamaGolem = (int) Math.ceil((numberOfElements + 1) / 3.0) + 1;
    tamaGolemPerPlayer = (int) Math.ceil((numberOfElements - 1) * (numberOfElements - 2) / (stonesPerTamaGolem * 2.0));
    stonesPerElement = (int) Math.ceil((2.0 * tamaGolemPerPlayer * stonesPerTamaGolem) / numberOfElements);
  }

  private static void createPlayers() {
    String name1 = InputData.readNonEmptyString(MESSAGE_CREATE_FIRST_PLAYER, false);
    player1 = new Player(name1, tamaGolemPerPlayer);
    String name2 = InputData.readNonEmptyString(MESSAGE_CREATE_SECOND_PLAYER, false);
    player2 = new Player(name2, tamaGolemPerPlayer);
  }

  private static void initializationStonesAvailable() {
    stonesAvailable = new HashMap<String, Integer>();
    for (String elementName : worldBalance.getWorldBalance().keySet()) {
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
    System.out.println(String.format(MESSAGE_NEW_TAMA, player.getName(), player.getTamaGolemUsed() + 1,
        player.getTotalTamaGolemUsable()));

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
      arrayOptsMenu = menuOptions.toArray(arrayOptsMenu);
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
    TamaGolem newTamaGolem = new TamaGolem(worldBalance.getSupremum(), tamaGolemStones);
    player.setTamaGolem(newTamaGolem);

    // when the first tama of the first player is created, the tama of the second
    // player hasn't been created yet
    if (player2.getTamaGolem() != null) {
      int hasCode1 = player1.getTamaGolem().getStonesAvailableHashCode();
      int hasCode2 = player2.getTamaGolem().getStonesAvailableHashCode();
      if (hasCode1 == hasCode2) {
        // we need to add again the stone taken in the HashMap stonesAvailable
        for (String stone : player.getTamaGolem().getStonesAvailable()) {
          stonesAvailable.compute(stone, (k, v) -> v + 1);
        }
        System.out.println(
            MESSAGE_TAMAGOLEM_EQUALS);
        player.removeTamaGolem();
        tamaGolemEvocation(player);
      }
    }
  }

  private static void startTamaGolemFight() {
    TamaGolem tamaGolemPlayer1 = player1.getTamaGolem();
    TamaGolem tamaGolemPlayer2 = player2.getTamaGolem();
    while (!tamaGolemPlayer1.isDead() && !tamaGolemPlayer2.isDead()) {
      String stone1 = tamaGolemPlayer1.executeAttack();
      String stone2 = tamaGolemPlayer2.executeAttack();
      // refers to stone1
      int valueInteractionElements = worldBalance.getWorldBalance().get(stone1).getConnectionValueForNode(stone2);
      String strongerElement;
      if (valueInteractionElements < 0) {
        tamaGolemPlayer1.reduceLife(valueInteractionElements);
        strongerElement = stone2;
      } else {
        tamaGolemPlayer2.reduceLife(valueInteractionElements * -1);
        strongerElement = stone1;
      }
      if (valueInteractionElements == 0) {
        System.out.println(String.format(MESSAGE_INTERACTION_STONES_TIE, stone1, stone2));
      } else {
        System.out.println(String.format(MESSAGE_INTERACTION_STONES, stone1, stone2, strongerElement));
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
