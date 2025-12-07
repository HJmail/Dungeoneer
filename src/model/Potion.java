package model;

/**
 * Abstract base class for consumable potion items (healing, vision, etc.).
 * Potions are stackable in {@link Inventory} and can be consumed via {@link #use()}.
 *
 * Potions no longer have rarity – only weapons use {@link Rarity}.
 *
 * @author Cristian Acevedo-Villasana
 * @version 0.0.2
 * @date 12/04/25
 */
public abstract class Potion implements Item {

    /**
     * Applies the actual effect of this potion.
     * Subclasses implement this (heal, reveal map, etc.).
     */
    protected abstract void consume();

    /**
     * Called when the player uses this item from the inventory.
     * Default behavior for all potions is to consume them.
     */
    @Override
    public void use() {
        consume();
    }

    /**
     * Each potion subtype must provide a name (e.g. "Healing Potion").
     */
    @Override
    public abstract String getName();

    /**
     * Short description shown in inventory / tooltips.
     */
    @Override
    public abstract String getDescription();
}