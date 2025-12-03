package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.GameView;
//import model.Pillar;
//import model.Potion;


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
 * @version 0.0.2
 * @date 11/28/25
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

  /** The list of all items currently stored in the player's inventory. */
  private final List<Item> myInventory;
  
  private Hero myOwner;

  /** Tracks how many of each potion type the player holds. */
  private final Map<String, Integer> myPotionStacks;

  /** View used to display inventory messages (GUI log / console). */
  private transient GameView myView;

  /**
   * Constructs an empty inventory with no collected items.
   */
  public Inventory() {
    myInventory = new ArrayList<>();
    myPotionStacks = new HashMap<>();
  }

  /**
   * Attach a {@link GameView} so the inventory can display messages
   * through the GUI/console instead of printing directly.
   *
   * <p>If no view is set, messages fall back to System.out.
   *
   * @param theView the GameView to use for messages
   */
  public void setView(final GameView theView) {
    myView = theView;
  }

  /** Helper to send messages to the view (or console if view is null). */
  private void log(final String theMessage) {
    if (myView != null) {
      myView.showMessage(theMessage);
    } else {
      System.out.println(theMessage);
    }
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
   * 
   * <p>Adds an item to the player's inventory.
   * Potions are stackable (maximum 3 per type). Pillars are tracked individually.
   * </p>
   *
   * @param theItem the item to add (must not be null)
   * @throws IllegalArgumentException if theItem is null
   */
  /**
   * Adds an item to the player's inventory.
   *
   * @param theItem item to add (must not be null)
   * @return true if the item was actually added/stacked,
   *         false if it was rejected (full inventory / full stack).
   */
  public boolean addItem(final Item theItem) {
      if (theItem == null) {
          throw new IllegalArgumentException("Cannot add null item to inventory.");
      }

      // ---------- PILLARS (do not count against MAX_ITEMS) ----------
      if (theItem instanceof Pillar pillar) {
          myInventory.add(theItem);

          char type = Character.toUpperCase(pillar.getPillarType());
          switch (type) {
              case 'A' -> myPillarAbstractionCollected = true;
              case 'E' -> myPillarEncapsulationCollected = true;
              case 'I' -> myPillarInheritanceCollected = true;
              case 'P' -> myPillarPolymorphismCollected = true;
              default -> { /* ignore unknown */ }
          }

          System.out.println("Picked up pillar: " + type);
          return true;
      }

      // ---------- POTIONS (stackable up to MAX_POTION_STACK) ----------
      if (theItem instanceof Potion) {
          String key = theItem.getClass().getSimpleName();
          int current = myPotionStacks.getOrDefault(key, 0);

          // Stack already full?
          if (current >= MAX_POTION_STACK) {
              System.out.println("You cannot carry more than "
                                 + MAX_POTION_STACK + " " + key + "s.");
              return false;
          }

          // First potion of this type uses an inventory slot
          if (current == 0 && getNonPillarItemCount() >= MAX_ITEMS) {
              System.out.println("Your inventory is full. You can't carry another " + key + ".");
              return false;
          }

          myPotionStacks.put(key, current + 1);

          // Only store one representative potion object in the list
          if (current == 0) {
              myInventory.add(theItem);
          }

          System.out.println("Picked up " + key + " ("
                             + (current + 1) + "/" + MAX_POTION_STACK + ")");
          return true;
      }

      // ---------- OTHER ITEMS (weapons, etc.) ----------
      if (getNonPillarItemCount() >= MAX_ITEMS) {
          System.out.println("Your inventory is full. You cannot carry more than "
                             + MAX_ITEMS + " items.");
          return false;
      }

      myInventory.add(theItem);
      System.out.println("Picked up item: "
                         + theItem.getClass().getSimpleName());
      return true;
  }


  /**
   * Uses (consumes or activates) an item by name.
   * Supports both stackable items (like potions) and non-stackable items.
   *
   * @param theItemName the simple class name of the item (e.g., "HealingPotion")
   * @throws IllegalArgumentException if the name is null/blank
   * @throws IllegalStateException if the item is not found or cannot be used
   */
  public void useItem(final String theItemName) {
      if (theItemName == null || theItemName.isBlank()) {
          throw new IllegalArgumentException("Item name cannot be null or empty.");
      }

      // 1) Find a matching item object in the list
      Item toUse = null;
      for (Item item : myInventory) {
          if (item.getClass().getSimpleName().equals(theItemName)) {
              toUse = item;
              break;
          }
      }

      if (toUse == null) {
          throw new IllegalStateException("Item not found in inventory: " + theItemName);
      }

      // 2) If it’s a potion, use stack logic
      if (toUse instanceof Potion) {
          String key = theItemName;  // e.g. "HealingPotion"
          int current = myPotionStacks.getOrDefault(key, 0);

          if (current <= 0) {
              throw new IllegalStateException("No " + key + " left to use!");
          }

          // --- Apply potion effect based on subtype ---
          if (toUse instanceof HealingPotion hp) {
              if (myOwner != null) {
                  int cur = myOwner.getHitPoints();
                  int max = myOwner.getMaxHitPoints();

                  int heal = hp.getHealAmount();
                  int newHp = cur + heal;
                  if (newHp > max) {
                      heal = max - cur;     // actual healed amount
                      newHp = max;
                  }

                  myOwner.setHitPoints(newHp);
                  log("Healed " + heal + " HP. Now at " + newHp + "/" + max);

                  // Ask the GUI to refresh HP bar, etc.
                  if (myView != null) {
                      myView.showHeroStats(myOwner);
                  }
              }
          } else {
              // Other potions (VisionPotion, etc.)
              toUse.use();
          }

          // --- Decrement stack, but only remove the item when stack hits 0 ---
          current -= 1;
          if (current > 0) {
              myPotionStacks.put(key, current);
              // KEEP the representative potion object in myInventory
          } else {
              myPotionStacks.remove(key);
              myInventory.remove(toUse);   // remove only when last one is used
          }

          log("Used one " + key + ". Remaining: " + current);
          return;
      }

      // 3) Non-potion items: just use and remove
      toUse.use();
      myInventory.remove(toUse);
  }
  
  /**
   * Drops (discards) one instance of an item by its simple class name.
   * For potions, only one is removed from the stack. For other items
   * the whole item is removed. Pillars are not dropped.
   *
   * @param theItemName simple class name, e.g. "HealingPotion"
   */
  public void dropItem(final String theItemName) {
      if (theItemName == null || theItemName.isBlank()) {
          throw new IllegalArgumentException("Item name cannot be null or empty.");
      }

      // Find a matching item in the inventory
      Item toDrop = null;
      for (Item item : myInventory) {
          if (item.getClass().getSimpleName().equals(theItemName)) {
              toDrop = item;
              break;
          }
      }

      if (toDrop == null) {
          throw new IllegalStateException("Item not found in inventory: " + theItemName);
      }

      // Don't allow dropping pillars (too important)
      if (toDrop instanceof Pillar) {
          log("You decide not to drop such an important artifact.");
          return;
      }

      // --- Potions: adjust stack count and only remove the object when stack hits 0 ---
      if (toDrop instanceof Potion) {
          String key = theItemName;  // e.g., "HealingPotion"
          int current = myPotionStacks.getOrDefault(key, 0);

          if (current <= 0) {
              throw new IllegalStateException("No " + key + " left to drop!");
          }

          current -= 1;

          if (current > 0) {
              // Still have some left: update count, keep representative object in list
              myPotionStacks.put(key, current);
          } else {
              // This was the last one: remove from both map and list
              myPotionStacks.remove(key);
              myInventory.remove(toDrop);
          }

          log("Dropped one " + key + ". Remaining: " + current);
          return;
      }

      // --- Non-potion, non-pillar items (weapons, etc.) ---
      myInventory.remove(toDrop);
      log("Dropped " + theItemName + ".");
  }


  /**
   * Removes a specified item from the player's inventory.
   *
   * @param theItem the item to remove
   */
  public void removeItem(final Item theItem) {
    myInventory.remove(theItem);
      
  }

  /**
   * Returns a defensive copy of the potion stacks map.
   *
   * @return a copy of the potion stack counts
   */
  public Map<String, Integer> getPotionStacks() {
    return new HashMap<>(myPotionStacks);
  }

  /**
   * Checks if the player's inventory contains the specified item.
   * 
   * @param theItem the item to search for
   * @return {@code true} if the item is in the inventory; {@code false} otherwise
   */
  public boolean hasItem(final Item theItem) {
    return myInventory.contains(theItem);
  }
  
  public void setOwner(final Hero theOwner) {
	    myOwner = theOwner;
	  }

  /**
   * Determines whether the player can exit the dungeon.
   * The player can only exit if all four pillars (A, E, I, and P) have been collected.
   * 
   * @return {@code true} if all pillars are collected; {@code false} otherwise
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

  /** Number of non-pillar items currently in the inventory. */
  private int getNonPillarItemCount() {
    int count = 0;
    for (Item item : myInventory) {
      if (!(item instanceof Pillar)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Clears all items and resets the inventory to its initial state.
   * This method removes every item from the inventory list and potion stacks,
   * and resets all Pillar collection flags to {@code false}.
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

    log("Inventory has been reset to default state.");
  }
}