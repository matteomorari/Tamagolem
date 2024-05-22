package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;

/**
 * The TamaGolem class represents a Tamagolem creature.
 * It has attributes such as life, available stones, and the position of the
 * next stone to be used in an attack.
 */
public class TamaGolem {
  private int life;
  private ArrayList<String> stonesAvailable;
  private int stonePositionNewAttack;

  /**
   * Constructs a TamaGolem object with the specified life and available stones.
   * 
   * @param life            the initial life of the TamaGolem
   * @param stonesAvailable the list of available stones for the TamaGolem
   */
  public TamaGolem(int life, ArrayList<String> stonesAvailable) {
    this.life = life;
    this.stonesAvailable = stonesAvailable;
    this.stonePositionNewAttack = 0;
  }

  /**
   * Returns the list of available stones for the TamaGolem.
   * 
   * @return the list of available stones
   */
  public ArrayList<String> getStonesAvailable() {
    return stonesAvailable;
  }

  /**
   * Checks if the TamaGolem is dead.
   * 
   * @return true if the TamaGolem is dead, false otherwise
   */
  public boolean isDead() {
    if (this.life < 1) {
      return true;
    }
    return false;
  }

  /**
   * Executes an attack by returning the next stone to be used.
   * If all stones have been used, it starts again from the beginning of the list.
   * 
   * @return the next stone to be used in the attack
   */
  public String executeAttack() {
    if (this.stonePositionNewAttack < this.stonesAvailable.size()) {
      return this.stonesAvailable.get(this.stonePositionNewAttack++);
    } else {
      this.stonePositionNewAttack = 0;
      return this.stonesAvailable.get(this.stonePositionNewAttack);
    }
  }

  /**
   * Returns the hash code of the list of available stones.
   * 
   * @return the hash code of the list of available stones
   */
  public int getStonesAvailableHashCode() {
    return this.stonesAvailable.hashCode();
  }

  /**
   * Reduces the life of the TamaGolem by the specified value.
   * 
   * @param value the value by which to reduce the life of the TamaGolem. To
   *              actually decrease the life, the param must be negative
   */
  public void reduceLife(int value) {
    this.life += value;
  }
}
