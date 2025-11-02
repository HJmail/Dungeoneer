package Model;

public class Ogre extends Monster {

    public Ogre() {
        super("Ogre",
              200,   // hit points
              2,     // attack speed
              0.6,   // chance to hit
              30,    // min damage
              60,    // max damage
              0.1,   // chance to heal
              30,    // min heal
              60,    // max heal
              50);   // gold dropped
    }
}
