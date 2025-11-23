package Model;

import Model.Inventory;
import Model.Item;

/**
 * Abstract Hero class representing the player-controlled character.
 * Heroes collect items, pick up pillars, and can buy items from the Shopkeeper.
 *
 * Supports inventory, blocking, and special skills.
 * 
 * @author Hiba
 * @version 0.0.6 (03/02/2025)
 */
public abstract class Hero extends DungeonCharacter {

    /** Probability that the hero blocks an incoming attack. */
    protected double myChanceToBlock;

    /** The Hero's inventory (potions, pillars, gold, weapons, etc.). */
    protected Inventory myInventory;

    /**
     * Constructs a Hero with all necessary statistics.
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

        // ⭐ Every Hero begins with an empty inventory
        myInventory = new Inventory();
    }

    /** Attempts to block an attack. */
    public boolean defend() {
        return Math.random() <= myChanceToBlock;
    }

    public double getChanceToBlock() {
        return myChanceToBlock;
    }

    public void setChanceToBlock(final double theChanceToBlock) {
        myChanceToBlock = theChanceToBlock;
    }

    /**
     * Returns the Hero's inventory so other parts of the game
     * (like Shopkeeper, DungeonAdventure, or Room) can add items.
     */
    public Inventory getInventory() {
        return myInventory;
    }

    /**
     * Adds an item to the hero's inventory.
     * Rooms and Shopkeeper will call this.
     */
    public void addToInventory(Item theItem) {
        myInventory.addItem(theItem);
    }
    
    /**
     * Each Hero type must implement their special skill.
     */
    public abstract String specialSkill(DungeonCharacter opponent);
}
