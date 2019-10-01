package com.tfar.notenoughshinies;

public class CoinEntry {

  public final String mob;
  public final double[] copper;
  public final double[] silver;
  public final double[] gold;

  public CoinEntry(String mob, double[] copper, double[] silver, double[] gold) {
    this.mob = mob;
    this.copper = copper;
    this.silver = silver;
    this.gold = gold;
  }
}
