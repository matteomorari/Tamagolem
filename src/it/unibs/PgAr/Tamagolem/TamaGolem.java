package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;

public class TamaGolem {
  private int life;
  private ArrayList<String> stonesAvailable;
  private int stonePositionNewAttack;

  public TamaGolem(int life, ArrayList<String> stonesAvailable) {
    this.life = life;
    this.stonesAvailable = stonesAvailable;
    this.stonePositionNewAttack = 0;
  }

  public ArrayList<String> getStonesAvailable() {
    return stonesAvailable;
  }

  public boolean isDead() {
    if (this.life < 1) {
      return true;
    }
    return false;
  }

  public String executeAttack() {
    if (this.stonePositionNewAttack < this.stonesAvailable.size()) {
      return this.stonesAvailable.get(this.stonePositionNewAttack++);
    } else {
      this.stonePositionNewAttack = 0;
      return this.stonesAvailable.get(this.stonePositionNewAttack);
    }
  }

  public int getStonesAvailableHashCode(){
    return this.stonesAvailable.hashCode();
  }

  /**
   * Reduces the life of the TamaGolem by the specified value.
   * 
   * @param value the value by which to reduce the life of the TamaGolem. TO
   *              actually decrease the life, the param bust be negative
   */
  public void reduceLife(int value) {
    this.life += value;
  }
}
