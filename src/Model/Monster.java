package Model;

/**
 * Abstract Monster class that extends DungeonCharacter and adds
 * healing behavior specific to all monster types.
 * Works with MonsterFactory and MonsterDatabase.
 *
 * @author Hiba
 * @version 1.0 (03/02/2025)
 */
public abstract class Monster extends DungeonCharacter {

    /** Probability the monster will heal after being hit. */
    protected double myChanceToHeal;

    /** Minimum healing amount. */
    protected int myMinHeal;

    /** Maximum healing amount. */
    protected int myMaxHeal;

    /**
     * Constructs a Monster with all necessary statistics.
     * This matches the parameters passed from MonsterFactory.
     *
     * @param theName Monster name
     * @param theHitPoints Health points
     * @param theMinDamage Minimum damage
     * @param theMaxDamage Maximum damage
     * @param theAttackSpeed Attack speed (affects number of turns)
     * @param theChanceToHit Chance to hit (0.0 to 1.0)
     * @param theChanceToHeal Chance to heal after being hit
     * @param theMinHeal Minimum heal value
     * @param theMaxHeal Maximum heal value
     */
    public Monster(String theName,
                   int theHitPoints,
                   int theMinDamage,
                   int theMaxDamage,
                   int theAttackSpeed,
                   double theChanceToHit,
                   double theChanceToHeal,
                   int theMinHeal,
                   int theMaxHeal) {

        // Matches updated DungeonCharacter constructor
        super(theName, theHitPoints, theMinDamage, theMaxDamage,
                theAttackSpeed, theChanceToHit);

        myChanceToHeal = theChanceToHeal;
        myMinHeal = theMinHeal;
        myMaxHeal = theMaxHeal;
    }

    /**
     * Attempts to heal the monster.
     * @return A message describing the result.
     */
    public String heal() {
        if (Math.random() <= myChanceToHeal) {
            int healPoints = (int) (Math.random() * (myMaxHeal - myMinHeal + 1)) + myMinHeal;
            myHitPoints += healPoints;
            return getName() + " heals for " + healPoints + " points!";
        }
        return getName() + " tried to heal but failed.";
    }

    // ===== Getters and Setters =====

    public double getChanceToHeal() { return myChanceToHeal; }
    public void setChanceToHeal(double theChanceToHeal) { myChanceToHeal = theChanceToHeal; }

    public int getMinHeal() { return myMinHeal; }
    public void setMinHeal(int theMinHeal) { myMinHeal = theMinHeal; }

    public int getMaxHeal() { return myMaxHeal; }
    public void setMaxHeal(int theMaxHeal) { myMaxHeal = theMaxHeal; }
}
