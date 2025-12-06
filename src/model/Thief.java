package model;

import java.io.Serializable;

public class Thief extends Hero implements Serializable {

    private static final long serialVersionUID = 1L;

    public Thief(String theName) {
        super(theName, 75, 20, 40, 6, 0.8, 0.4);
        myImagePath = "images/thief.png";
        myGold = 80;
    }

    @Override
    public String specialSkill(DungeonCharacter opponent) {
        double roll = Math.random();

        if (roll <= 0.4) { // surprise attack success
            int dmg1 = attack(opponent);
            int dmg2 = attack(opponent);
            return myName + " lands a SURPRISE ATTACK for " + dmg1 +
                   " + EXTRA attack for " + dmg2 + " damage!";
        }

        if (roll <= 0.6) { // caught
            return myName + " is caught trying a surprise attack — no damage done!";
        }

        // normal fallback attack
        int normal = attack(opponent);
        if (normal == -1) return myName + " attacks normally but misses!";
        return myName + " attacks normally for " + normal + " damage!";
    }
}
