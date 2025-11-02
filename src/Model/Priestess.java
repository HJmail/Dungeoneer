package Model;

public class Priestess extends Hero {

    public Priestess(String theName) {
        super(theName,
              75,    // hit points
              5,     // attack speed
              0.7,   // chance to hit
              25,    // min damage
              45,    // max damage
              0.3,   // chance to block
              120);  // starting gold
    }
}
