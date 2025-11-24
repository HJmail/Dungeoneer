package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the player's inventory, which stores collectible items such as
 * potions, weapons, gold, and the four Pillars of OO. The inventory is
 * associated with both heroes and monsters and tracks which pillars have been
 * collected to determine whether the player can exit the dungeon.
 * 
 * <p>This class is part of the Dungeon Adventure project and provides essential
 * item management functionality for gameplay progression.
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.0.1
 * 
 * <p>@date 11/16/25
 */

public class Inventory {

  /** Represents the Abstract Pillar whether it has been collected. */
  private boolean myPillarAbstractionCollected;
  
  /** Represents the Encapsulation Pillar whether it has been collected. */
  private boolean myPillarEncapsulationCollected;
  
  /** Represents the Inheritance Pillar whether it has been collected. */
  private boolean myPillarInheritanceCollected;
  
  /** Represents the Polymorphism Pillar whether it has been collected. */
  private boolean myPillarPolymorphismCollected;
  
  /** The maximum number of each potion type that can be stacked. */
  private static final int MAX_POTION_STACK = 3;
  
  /** The maximum total number of items the player can carry. */
  private static final int MAX_ITEMS = 5;
  
  /** The list of all item currently stored in the player's inventory. */
  private final List<Item> myInventory;
  
  /** Tracks how many of each potion type the player holds. */
  private final Map<String, Integer> myPotionStacks;

  /**
   * Constructs an empty inventory with no collected items.
   */
  public Inventory() {
    myInventory = new ArrayList<>();
    myPotionStacks = new HashMap<>();
  }
    
  /**
   * Returns the list of all items currently in the player's inventory.
   * 
   * <p>@return a list of {@link Item} objects contained in the inventory
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
   * 
   * <p>@param theItem being the pillar.
   * Adds an item to the player's inventory.
   * Potions are stackable (maximum 3 per type). Pillars are tracked individually.
   * </p>
   *
   * @param theItem the item to add (must not be null)
   * @throws IllegalArgumentException if theItem is null
   */
  public void addItem(final Item theItem) {
    if (theItem == null) {
      throw new IllegalArgumentException("Cannot add null item to inventory.");
    }
    
    if (myInventory.size() >= MAX_ITEMS) {
      throw new IllegalStateException("Inventory full! You cannot carry more than " 
                                        + MAX_ITEMS + " items.");
    }

    // Handle Potion stacking logic
    if (theItem instanceof Potion) {
      String key = theItem.getClass().getSimpleName();
      int current = myPotionStacks.getOrDefault(key, 0);

      if (current >= MAX_POTION_STACK) {
        System.out.println("You cannot carry more than " + MAX_POTION_STACK + " " + key + "s.");
        return; // skip adding if stack is full
      }

      myPotionStacks.put(key, current + 1);
      System.out.println("Picked up " + key + " (" + (current + 1) + "/" + MAX_POTION_STACK + ")");
    }

    // Always add the item to the list for tracking
    myInventory.add(theItem);

    // Handle Pillar collection logic
    if (theItem instanceof Pillar) {
      char type = Character.toUpperCase(((Pillar) theItem).getPillarType());
      switch (type) {
        case 'A' -> myPillarAbstractionCollected = true;
        case 'E' -> myPillarEncapsulationCollected = true;
        case 'I' -> myPillarInheritanceCollected = true;
        case 'P' -> myPillarPolymorphismCollected = true;
        default -> { /* do nothing */ }
      }
    }
  }
  
  /**
   * Uses (consumes or activates) an item by name.
   * Supports both stackable items (like potions) and non-stackable items.
   * </p>
   *
   * @param theItemName the simple class name of the item (e.g., "HealingPotion")
   * @throws IllegalStateException if the item is not found or cannot be used
   */
  public void useItem(final String theItemName) {
    if (theItemName == null || theItemName.isBlank()) {
      throw new IllegalArgumentException("Item name cannot be null or empty.");
          
    }
    
    if (myPotionStacks.containsKey(theItemName)) {
      int current = myPotionStacks.get(theItemName);

      if (current <= 0) {
        throw new IllegalStateException("No " + theItemName + " left to use!");
      }

      // find one potion in list
      Item toUse = null;
      for (Item item : myInventory) {
        if (item.getClass().getSimpleName().equals(theItemName)) {
          toUse = item;
          break;
        }
      }

      if (toUse == null) {
        throw new IllegalStateException("Item not found in inventory list.");
      }

      toUse.use();  // activate its effect
      myInventory.remove(toUse);

      myPotionStacks.put(theItemName, current - 1);
      if (myPotionStacks.get(theItemName) <= 0) {
        myPotionStacks.remove(theItemName);
      }

      System.out.println("Used one " + theItemName + ". Remaining: "
              + myPotionStacks.getOrDefault(theItemName, 0));
      return;
    }

    Item toUse = null;
    for (Item item : myInventory) {
      if (item.getClass().getSimpleName().equals(theItemName)) {
        toUse = item;
        break;
      }
    }

    if (toUse == null) {
      throw new IllegalStateException("Item not found in inventory.");
    }

    toUse.use();
    myInventory.remove(toUse);
  }
 
  /**
   * Removes a specified item from the player's inventory.
   *
   * @param theItem the item to remove
   */
  public void removeItem(final Item theItem) {
    myInventory.remove(theItem);

    if (theItem instanceof Potion) {
      String key = theItem.getClass().getSimpleName();
      myPotionStacks.merge(key, -1, Integer::sum);
      if (myPotionStacks.getOrDefault(key, 0) <= 0) {
        myPotionStacks.remove(key);
      }
    }
  } 

  public Map<String, Integer> getPotionStacks() {
    return new HashMap<>(myPotionStacks);
  }

  /**
   * Checks if the player's inventory contains the specified item.
   * 
   * <p>@param theItem the item to search for
   * 
   * <p>@return {@code true} if the item is in the inventory; {@code false} otherwise
   */
  public boolean hasItem(final Item theItem) {
    return myInventory.contains(theItem);
  }

  /**
   * Determines whether the player can exit the dungeon.
   * The player can only exit if all four pillars (A, E, I, and P) have been collected.
   * 
   * <p>@return {@code true} if all pillars are collected; {@code false} otherwise
   */
  public boolean canExit() {
    return myPillarAbstractionCollected 
      && myPillarEncapsulationCollected 
      && myPillarInheritanceCollected 
      && myPillarPolymorphismCollected; 
  }
  
  /**
   * Returns how many of the four Pillars of OO have been collected.
   * This method counts the number of Pillar collection flags currently set to {@code true}.
   * Useful for displaying player progress (e.g., "3/4 Pillars collected").
   * </p>
   *
   * @return the number of collected pillars, from 0 to 4
   */
  public int getCollectedPillarsCount() {
    int count = 0;
    if (myPillarAbstractionCollected) {
      count++;
    }
    if (myPillarEncapsulationCollected) {
      count++;
    }
    if (myPillarInheritanceCollected) {
      count++;
    }
    if (myPillarPolymorphismCollected) {
      count++;
    }
    return count;
  }

  
  /**
   * Clears all items and resets the inventory to its initial state.
   * This method removes every item from the inventory list and potion stacks,
   * and resets all Pillar collection flags to {@code false}.
   * </p>
   * 
   * <p>Intended for use when starting a new game or resetting the dungeon.</p>
   */
  public void clearInventory() {
    myInventory.clear();
    myPotionStacks.clear();

    myPillarAbstractionCollected = false;
    myPillarEncapsulationCollected = false;
    myPillarInheritanceCollected = false;
    myPillarPolymorphismCollected = false;

    System.out.println("Inventory has been reset to default state.");
  }
}