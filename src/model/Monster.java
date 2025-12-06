package model;

import java.io.Serializable;

public abstract class Monster extends DungeonCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    protected double myChanceToHeal;
    protected int myMinHeal;
    protected int myMaxHeal;

    public Monster(String theName, int theHitPoints, int theMinDamage,
                   int theMaxDamage, int theAttackSpeed,
                   double theChanceToHit, double theChanceToHeal,
                   int theMinHeal, int theMaxHeal) {

        super(theName, theHitPoints, theMinDamage, theMaxDamage,
              theAttackSpeed, theChanceToHit);

        myChanceToHeal = theChanceToHeal;
        myMinHeal = theMinHeal;
        myMaxHeal = theMaxHeal;
    }
}
