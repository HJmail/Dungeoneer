package Model;

/**
 * Represents a general item that can exist in the player's inventory.
 * All items in the Dungeon Adventure game (e.g., potions, gold, weapons, pillars)
 * implement this interface.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/1/25
 */
public interface Item {

    /**
     * Provides a string description of the item.
     * 
     * @return a brief description or name of the item
     */
    String getDescription();

    /**
     * Defines the behavior that occurs when the item is used by the player.
     */
    void use();
}
