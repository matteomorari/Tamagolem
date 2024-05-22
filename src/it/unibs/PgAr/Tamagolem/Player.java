package it.unibs.PgAr.Tamagolem;

/**
 * The Player class represents a player in the Tamagolem game.
 */
public class Player {
  private String name;
  private int totalTamaGolemUsable;
  private int tamaGolemUsed;
  private TamaGolem tamaGolem;

  /**
   * Constructs a new Player object with the specified name and total number of
   * usable TamaGolems.
   * 
   * @param name                 the name of the player
   * @param totalTamaGolemUsable the total number of usable TamaGolems for the
   *                             player
   */
  public Player(String name, int totalTamaGolemUsable) {
    this.name = name;
    this.tamaGolemUsed = 0;
    this.totalTamaGolemUsable = totalTamaGolemUsable;
  }

  /**
   * Returns the name of the player.
   * 
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the TamaGolem owned by the player.
   * 
   * @return the TamaGolem owned by the player
   */
  public TamaGolem getTamaGolem() {
    return tamaGolem;
  }

  /**
   * Sets the TamaGolem owned by the player to the specified TamaGolem.
   * 
   * @param newTamaGolem the new TamaGolem to be set
   */
  public void setTamaGolem(TamaGolem newTamaGolem) {
    this.tamaGolem = newTamaGolem;
    this.tamaGolemUsed++;
  }

  /**
   * Removes the TamaGolem owned by the player.
   */
  public void removeTamaGolem() {
    this.tamaGolem = null;
    this.tamaGolemUsed--;
  }

  /**
   * Returns the total number of usable TamaGolems for the player.
   * 
   * @return the total number of usable TamaGolems for the player
   */
  public int getTotalTamaGolemUsable() {
    return totalTamaGolemUsable;
  }

  /**
   * Checks if the player has used all their TamaGolems.
   * 
   * @return true if the player has used all their TamaGolems, false otherwise
   */
  public boolean tamaGolemFinished() {
    if (this.tamaGolemUsed == this.totalTamaGolemUsable) {
      return true;
    }
    return false;
  }

  /**
   * Returns the number of TamaGolems used by the player.
   * 
   * @return the number of TamaGolems used by the player
   */
  public int getTamaGolemUsed() {
    return tamaGolemUsed;
  }
}
