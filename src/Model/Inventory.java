package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's inventory, which stores collectible items such as
 * potions, weapons, gold, and the four Pillars of OO. The inventory is
 * associated with both heroes and monsters and tracks which pillars have been
 * collected to determine whether the player can exit the dungeon.
 * 
 * This class is part of the Dungeon Adventure project and provides essential
 * item management functionality for gameplay progression.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/24/25
 */

public class Inventory {
	
  /** Represents the Abstract Pillar whether it has been collected */
  private boolean myPillarACollected;
  
  /** Represents the Encapsulation Pillar whether it has been collected */
  private boolean myPillarECollected;
  
  /** Represents the Inheritance Pillar whether it has been collected */
  private boolean myPillarICollected;
  
  /** Represents the Polymorphism Pillar whether it has been collected */
  private boolean myPillarPCollected;
  
  /** The list of all item currently stored in the player's inventory. */
  private List<Item> myInventory;
	
  /**
   * Constructs an empty inventory with no collected items.
   */
  public Inventory() {
    myInventory = new ArrayList<>();
    
  }
    
  /**
   * Returns the list of all items currently in the player's inventory.
   * 
   * @return a list of {@link Item} objects contained in the inventory
   */
  public List<Item> getInventory() {
	return myInventory; 
	  
  }
  
  /**
   * addItem is a method where if the player picks up a Pillar type of
   * - A being Abstract pillar
   * - E being Encapsulation pillar
   * - I being Inheritance pillar
   * - P being Polymorphism pillar
   * If the player has a pillar it's true and if the player doesn't have a 
   * pillar it it's false.
   * @param theItem
   */
  public void addItem(Item theItem) {
	    if (theItem != null) {
	        myInventory.add(theItem);

	        if (theItem instanceof Pillar) {
	            char type = Character.toUpperCase(((Pillar) theItem).getPillarType());

	          if (type == 'A') {
	              myPillarACollected = true;
	          } else if (type == 'E') {
	              myPillarECollected = true;
	          } else if (type == 'I') {
	              myPillarICollected = true;
	          } else if (type == 'P') {
	              myPillarPCollected = true;
	          }
	      }
	  }
  }
  
  /**
   * Removes a specified item from the player's inventory.
   * 
   * @param theItem the item to remove
   */
  public void removeItem(Item theItem) {
    myInventory.remove(theItem);
  }

  /**
   * Checks if the player's inventory contains the specified item.
   * 
   * @param theItem the item to search for
   * @return {@code true} if the item is in the inventory; {@code false} otherwise
   */
  public boolean hasItem(Item theItem) {
	  return myInventory.contains(theItem);
  }
	
  /**
   * Determines whether the player can exit the dungeon.
   * <p>The player can only exit if all four pillars (A, E, I, and P) have been collected.</p>
   * 
   * @return {@code true} if all pillars are collected; {@code false} otherwise
   */
  public boolean canExit() {
	 return myPillarACollected && myPillarECollected
			 && myPillarICollected && myPillarPCollected;
	 
  }	
}
