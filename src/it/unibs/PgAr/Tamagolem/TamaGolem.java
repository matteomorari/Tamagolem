package it.unibs.PgAr.Tamagolem;

import java.util.ArrayList;

public class TamaGolem {
  private int life;
  private ArrayList<String> stonesAvailable;

  public TamaGolem(int life, ArrayList<String> stonesAvailable) {
    this.life = life;
    this.stonesAvailable = stonesAvailable;
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
}
