package model;

/**
 * Represents a general item that can exist in the player's inventory.
 * All items in the Dungeon Adventure game (e.g., potions, gold, weapons, pillars)
 * implement this interface.
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.0.1
 * 
 * <p>@date 11/16/25
 */
public interface Item {

	
  /**
   * Returns the display name of the item (e.g., "Healing Potion",
   * "Vision Potion", "Gold", "Falchion").
   *
   * <p>By default this just returns {@link #getDescription()}, but
   * concrete items are free to override with a shorter / nicer name.
   *
   * @return the item’s display name
   */
   default String getName() {
	 return getDescription();
   }
	  
  /**
   * Provides a string description of the item.
   * 
   * <p>@return a brief description or name of the item
   */
  String getDescription();

  /**
   * Defines the behavior that occurs when the item is used by the player.
   */
  public abstract void use();
}