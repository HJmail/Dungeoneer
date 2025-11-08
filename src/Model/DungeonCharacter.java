package Model;

/**
 * Abstract base class representing all characters in the dungeon.
 * Supports both console and GUI-based interactions.
 * 
 * @author Hiba
 * @version 0.0.4 10/27/2025
 */
public abstract class DungeonCharacter {

    /** Character's name. */
    protected String myName;

    /** Character's health points. */
    protected int myHitPoints;

    /** Character's attack speed. */
    protected int myAttackSpeed;

    /** Character's chance to successfully hit an opponent. */
    protected double myChanceToHit;

    /** Character's minimum attack damage. */
    protected int myMinDamage;

    /** Character's maximum attack damage. */
    protected int myMaxDamage;

    /** Amount of gold held by this character. */
    protected int myGold;

    /** Path to this character's image for GUI use. */
    protected String myImagePath;

    /**
     * Constructs a DungeonCharacter with its stats.
     */
    public DungeonCharacter(String theName, int theHitPoints, int theAttackSpeed,
                            double theChanceToHit, int theMinDamage, int theMaxDamage, int theGold) {
        myName = theName;
        myHitPoints = theHitPoints;
        myAttackSpeed = theAttackSpeed;
        myChanceToHit = theChanceToHit;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myGold = theGold;
        myImagePath = "images/default.png"; // Default placeholder
    }

    /**
     * Checks if this character is still alive.
     */
    public boolean isAlive() {
        return myHitPoints > 0;
    }

    /**
     * Performs a standard attack on an opponent.
     */
    public void attack(DungeonCharacter theOpponent) {
        if (Math.random() <= myChanceToHit) {
            int damage = (int) (Math.random() * (myMaxDamage - myMinDamage + 1)) + myMinDamage;
            theOpponent.myHitPoints -= damage;
            System.out.println(myName + " hits " + theOpponent.myName + " for " + damage + " damage!");
        } else {
            System.out.println(myName + " missed!");
        }
    }

    // === Getters and Setters ===

    public String getName() {
        return myName;
    }

    public int getHitPoints() {
        return myHitPoints;
    }

    public void setHitPoints(final int theHitPoints) {
        myHitPoints = theHitPoints;
    }

    public int getGold() {
        return myGold;
    }

    public void setGold(final int theGold) {
        myGold = theGold;
    }

    public int getAttackSpeed() {
        return myAttackSpeed;
    }

    public double getChanceToHit() {
        return myChanceToHit;
    }

    public int getMinDamage() {
        return myMinDamage;
    }

    public int getMaxDamage() {
        return myMaxDamage;
    }

    public String getImagePath() {
        return myImagePath;
    }

    public void setImagePath(final String thePath) {
        myImagePath = thePath;
    }

    @Override
    public String toString() {
        return myName + " [HP: " + myHitPoints + ", Gold: " + myGold + "]";
    }
}
