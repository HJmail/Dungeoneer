package Model;

public class Skeleton extends Monster {

    public Skeleton() {
        super("Skeleton",
              100,    // hit points
              3,      // attack speed
              0.8,    // chance to hit
              30,     // min damage
              50,     // max damage
              0.3,    // chance to heal
              30,     // min heal
              50,     // max heal
              35);    // gold dropped
        myImagePath = "images/skeleton.png";
    }
}
