package edu.uw.tcss.dungeoneer.model;

public class Warrior extends Hero {

    public Warrior(String theName) {
        super(theName,
              125,   // hit points
              4,     // attack speed
              0.8,   // chance to hit
              35,    // min damage
              60,    // max damage
              0.2,   // chance to block
              100);  // starting gold
    }
}
