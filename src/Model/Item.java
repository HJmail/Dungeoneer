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