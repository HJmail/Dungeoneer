package Model;

public class Ogre extends Monster {

    public Ogre() {
        super("Ogre",
              200, 2, 0.6,
              30, 60,
              0.1, 30, 60, 50);
        myImagePath = "images/ogre.png";
    }
}
