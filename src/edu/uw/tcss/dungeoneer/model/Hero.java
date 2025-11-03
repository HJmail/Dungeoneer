package edu.uw.tcss.dungeoneer.model;

public abstract class Hero extends DungeonCharacter {

    protected double myChanceToBlock;

    public Hero(String theName, int theHitPoints, int theAttackSpeed,
                double theChanceToHit, int theMinDamage, int theMaxDamage,
                double theChanceToBlock, int theGold) {
        super(theName, theHitPoints, theAttackSpeed, theChanceToHit, theMinDamage, theMaxDamage, theGold);
        myChanceToBlock = theChanceToBlock;
    }

    public boolean defend() {
        return Math.random() <= myChanceToBlock;
    }
    
    public double getChanceToBlock() {
        return myChanceToBlock;
    }
    
    public void setChanceToBlock(final double theChanceToBlock) {
        myChanceToBlock = theChanceToBlock;
    }
}