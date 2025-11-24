package model;

public abstract class Hero extends DungeonCharacter {

    /** Probability that the hero blocks an incoming attack. */
    protected double myChanceToBlock;

    /**
     * Constructs a Hero with all necessary statistics.
     *
     * @param theName Hero name
     * @param theHitPoints Starting hit points
     * @param theMinDamage Minimum attack damage
     * @param theMaxDamage Maximum attack damage
     * @param theAttackSpeed Speed (affects number of attacks)
     * @param theChanceToHit Chance to hit the opponent
     * @param theChanceToBlock Chance to block incoming attacks
     */
    public Hero(String theName,
                int theHitPoints,
                int theMinDamage,
                int theMaxDamage,
                int theAttackSpeed,
                double theChanceToHit,
                double theChanceToBlock) {

        // Matches the updated DungeonCharacter constructor!
        super(theName, theHitPoints, theMinDamage, theMaxDamage,
                theAttackSpeed, theChanceToHit);

        myChanceToBlock = theChanceToBlock;
        myGold = 0; // Heroes start with no gold by default
    }

    /** Attempts to block an attack. */
    public boolean defend() {
        return Math.random() <= myChanceToBlock;
    }

    public double getChanceToBlock() {
        return myChanceToBlock;
    }

    public String defendAction() {
        if (defend()) {
            return myName + " blocks the attack!";
        }
        return myName + " fails to block!";
    }

    public void setChanceToBlock(final double theChanceToBlock) {
        myChanceToBlock = theChanceToBlock;
    }
    
    public abstract String specialSkill(DungeonCharacter opponent);
    
    //Add String method 

}