package model;

import java.io.Serializable;

public class Priestess extends Hero implements Serializable {

    private static final long serialVersionUID = 1L;

    public Priestess(String theName) {
        super(theName, 75, 25, 45, 5, 0.7, 0.3);
        myImagePath = "images/priestess.png";
        myGold = 120;
    }

    @Override
    public String specialSkill(DungeonCharacter opponent) {
        int healAmount = (int)(Math.random() * (30 - 20 + 1)) + 20;
        myHitPoints += healAmount;
        return myName + " heals for " + healAmount + " HP!";
    }
}
