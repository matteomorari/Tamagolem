package it.unibs.PgAr.Tamagolem;

public class Player {
  private String name;
  private int totalTamaGolemUsable;
  private int tamaGolemUsed;
  private TamaGolem tamaGolem;

  public Player(String name, int totalTamaGolemUsable) {
    this.name = name;
    this.tamaGolemUsed = 0;
    this.totalTamaGolemUsable = totalTamaGolemUsable;
  }

  public String getName() {
    return name;
  }

  public TamaGolem getTamaGolem() {
    return tamaGolem;
  }

  public void setTamaGolem(TamaGolem newTamaGolem) {
    this.tamaGolem = newTamaGolem;
    this.tamaGolemUsed++;
  }

  public void removeTamaGolem() {
    this.tamaGolem = null;
    this.tamaGolemUsed--;
  }

  public int getTotalTamaGolemUsable() {
    return totalTamaGolemUsable;
  }

  public boolean tamaGolemFinished() {
    if (this.tamaGolemUsed == this.totalTamaGolemUsable) {
      return true;
    }
    return false;
  }

  public int getTamaGolemUsed() {
    return tamaGolemUsed;
  }
}
