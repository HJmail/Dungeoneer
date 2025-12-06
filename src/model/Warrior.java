package model;

import java.io.Serializable;

public class Warrior extends Hero implements Serializable {

    private static final long serialVersionUID = 1L;

    public Warrior(String theName) {
        super(theName, 125, 35, 60, 4, 0.8, 0.2);
        myImagePath = "images/warrior.png";
        myGold = 100;
    }

    @Override
    public String specialSkill(DungeonCharacter opponent) {
        if (Math.random() <= 0.4) {
            int damage = (int)(Math.random() * (175 - 75 + 1)) + 75;
            opponent.setHitPoints(opponent.getHitPoints() - damage);
            return myName + " performs a CRUSHING BLOW for " + damage + " damage!";
        }
        return myName + " attempts a CRUSHING BLOW but misses!";
    }
}
