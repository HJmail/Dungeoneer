package Model;

public class Warrior extends Hero {

    public Warrior(String theName) {
        super(theName,
              125, 4, 0.8,
              35, 60,
              0.2, 100);
        myImagePath = "images/warrior.png";
    }
}
