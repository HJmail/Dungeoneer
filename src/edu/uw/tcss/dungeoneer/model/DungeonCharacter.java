package edu.uw.tcss.dungeoneer.model;

public abstract class DungeonCharacter {

    protected String myName;
    protected int myHitPoints;
    protected int myAttackSpeed;
    protected double myChanceToHit;
    protected int myMinDamage;
    protected int myMaxDamage;
    protected int myGold;

    public DungeonCharacter(String theName, int theHitPoints, int theAttackSpeed,
                            double theChanceToHit, int theMinDamage, int theMaxDamage, int theGold) {
        myName = theName;
        myHitPoints = theHitPoints;
        myAttackSpeed = theAttackSpeed;
        myChanceToHit = theChanceToHit;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myGold = theGold;
    }

    public boolean isAlive() {
        return myHitPoints > 0;
    }

    public void attack(DungeonCharacter theOpponent) {
        if (Math.random() <= myChanceToHit) {
            int damage = (int)(Math.random() * (myMaxDamage - myMinDamage + 1)) + myMinDamage;
            theOpponent.myHitPoints -= damage;
            System.out.println(myName + " hits " + theOpponent.myName + " for " + damage + " damage!");
        } else {
            System.out.println(myName + " missed!");
        }
    }
    /**
     * Returns the character's name.
     * @return the character name
     */
    public String getName() {
        return myName;
    }

}
