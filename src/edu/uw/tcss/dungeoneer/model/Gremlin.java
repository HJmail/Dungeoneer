package edu.uw.tcss.dungeoneer.model;

public class Gremlin extends Monster {

    public Gremlin() {
        super("Gremlin",
              70,    // hit points
              5,     // attack speed
              0.8,   // chance to hit
              15,    // min damage
              30,    // max damage
              0.4,   // chance to heal
              20,    // min heal
              40,    // max heal
              25);   // gold dropped
    }
}
