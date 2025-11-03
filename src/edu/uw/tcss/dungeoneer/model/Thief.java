package edu.uw.tcss.dungeoneer.model;

public class Thief extends Hero {

    public Thief(String theName) {
        super(theName,
              75,    // hit points
              6,     // attack speed
              0.8,   // chance to hit
              20,    // min damage
              40,    // max damage
              0.4,   // chance to block
              80);   // starting gold
    }
}