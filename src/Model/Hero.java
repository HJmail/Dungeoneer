package model;

public abstract class Hero extends DungeonCharacter {

    /** Probability that the hero blocks an incoming attack. */
    protected double myChanceToBlock;

    /** Each hero has their own inventory. */
    protected final Inventory myInventory;

    /** The maximum HP this hero can have. */
    private  int myMaxHitPoints;

    /**
     * Constructs a Hero with all necessary statistics.
     *
     * @param theName Hero name
     * @param theHitPoints Starting (and max) hit points
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

        super(theName, theHitPoints, theMinDamage, theMaxDamage,
              theAttackSpeed, theChanceToHit);

        myChanceToBlock = theChanceToBlock;
        myGold = 0;

        // max HP is whatever we started with
        myMaxHitPoints = theHitPoints;

        // every hero starts with an inventory, and we tell the inventory who owns it
        myInventory = new Inventory();
        myInventory.setOwner(this);   // <-- IMPORTANT
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

    /** Public getter so the GUI can access the hero's inventory. */
    public Inventory getInventory() {
        return myInventory;
    }

    /**
     * Adds the specified amount of gold to this hero.
     * Negative amounts are allowed for spending, but
     * gold will never drop below zero.
     */
    public void addGold(final int theAmount) {
        myGold += theAmount;
        if (myGold < 0) {
            myGold = 0;
        }
    }

    /** Maximum HP for this hero (used to avoid overhealing). */
    public int getMaxHitPoints() {
        return myMaxHitPoints;
    }
    
    public void setMaxHitPoints(final int theMax) {
        myMaxHitPoints = theMax;
    }

    // This should just assign, not clamp again
    public void setHitPoints(final int theHp) {
        myHitPoints = theHp;
    }

    public abstract String specialSkill(DungeonCharacter opponent);
}