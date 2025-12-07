package model;

/**
 * Represents a Healing Potion that restores health points to the hero when used.
 * The actual HP restoration is handled by {@link Inventory} because it knows
 * which {@link Hero} owns the potion.
 *
 * This class just tells the game: "I'm a healing potion that heals N HP."
 *
 * @author Cristian Acevedo-Villasana
 * @version 0.0.2
 * @date 12/04/25
 */
public class HealingPotion extends Potion {

    /** How many hit points this potion restores before capping at max HP. */
    private final int myHealAmount;

    /**
     * Default constructor – heals 50 HP.
     */
    public HealingPotion() {
        this(50);
    }

    /**
     * Creates a Healing Potion with a custom heal amount.
     *
     * @param theHealAmount the number of HP this potion should restore
     */
    public HealingPotion(final int theHealAmount) {
        myHealAmount = theHealAmount;
    }

    /**
     * Returns the heal amount this potion provides (before capping at max HP).
     *
     * @return heal amount in HP
     */
    public int getHealAmount() {
        return myHealAmount;
    }

    @Override
    protected void consume() {
        // The Inventory actually adjusts the Hero's HP.
        // This is mainly for console / debugging feedback.
        System.out.println("You drink a Healing Potion and feel reinvigorated!");
    }

    @Override
    public String getName() {
        return "Healing Potion";
    }

    @Override
    public String getDescription() {
        return "Healing Potion (Heals " + myHealAmount + " HP)";
    }
}