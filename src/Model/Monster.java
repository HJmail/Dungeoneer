package edu.uw.tcss.dungeoneer.model;

public abstract class Monster extends DungeonCharacter {

    protected double myChanceToHeal;
    protected int myMinHeal;
    protected int myMaxHeal;

    public Monster(String theName, int theHitPoints, int theAttackSpeed,
                   double theChanceToHit, int theMinDamage, int theMaxDamage,
                   double theChanceToHeal, int theMinHeal, int theMaxHeal, int theGold) {
        super(theName, theHitPoints, theAttackSpeed, theChanceToHit, theMinDamage, theMaxDamage, theGold);
        myChanceToHeal = theChanceToHeal;
        myMinHeal = theMinHeal;
        myMaxHeal = theMaxHeal;
    }

    public void heal() {
        if (Math.random() <= myChanceToHeal) {
            int healPoints = (int) (Math.random() * (myMaxHeal - myMinHeal + 1)) + myMinHeal;
            System.out.println(getName() + " heals for " + healPoints + " points!");
        }
    }
    
    public double getChanceToHeal() {
        return myChanceToHeal;
    }

    public void setChanceToHeal(final double theChanceToHeal) {
        myChanceToHeal = theChanceToHeal;
    }

    public int getMinHeal() {
        return myMinHeal;
    }

    public void setMinHeal(final int theMinHeal) {
        myMinHeal = theMinHeal;
    }

    public int getMaxHeal() {
        return myMaxHeal;
    }
    
    public void setMaxHeal(final int theMaxHeal) {
        myMaxHeal = theMaxHeal;
    }
}
