package model;

/**
 * Represents a Vision Potion that reveals hidden parts of the dungeon
 * when consumed by the hero. Duration scales with rarity.
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.0.1
 * 
 * <p>@date 11/16/25
 */
public class VisionPotion extends Potion {

  private int myBaseDuration;

  /**
   * Constructs a VisionPotion with a specified base duration and rarity.
   * 
   * <p>@param theDuration the base duration of the vision effect
   * 
   * <p>@param theRarity the rarity of the potion
   * 
   * <p>@throws IllegalArgumentException if parameters are invalid
   */
  public VisionPotion(final int theDuration, final Rarity theRarity) {
    super(theRarity);
    if (theDuration <= 0) {
      throw new IllegalArgumentException("Duration must be positive.");
    }
    if (theRarity == null) {
      throw new IllegalArgumentException("Rarity cannot be null.");
    }
    myBaseDuration = theDuration;
  }
    
  public int getDuration() {
    return myBaseDuration + getRarityBonus();
  }

  /**
   * Consumes this Vision Potion and prints a message describing its effect and duration.
   */
  @Override
  public void consume() {
    System.out.println("You used a Vision Potion. You can now see hidden parts of the"
        + " dungeon for " + getDuration() + " turns!");
  }

  /**
   * Provides a short description of this Vision Potion item.
   * 
   * <p>@return a descriptive string showing the potion’s rarity and duration
   */
  @Override
  public String getDescription() {
    return "Vision Potion (" + getRarity() + ", Duration: " + getDuration() + ")";
  }
  
  @Override
  public String getName() {
    return "Vision Potion";
  }
}