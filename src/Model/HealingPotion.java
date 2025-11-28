package model;

/**
 * Represents a Healing Potion that restores health points to the hero when consumed.
 * The healing magnitude is represented here by a base heal value plus rarity bonus (optional).
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <P>@version 0.0.1
 * 
 * <p>@date 11/16/25
 */
public class HealingPotion extends Potion {

  /** Base heal amount (HP) for this potion; you can tune as needed. */
  private int myBaseHeal;


  /**
   * Constructs a HealingPotion with a specified base heal and rarity.
   * 
   * <p>@param theBaseHeal the base amount of HP the potion restores
   * 
   * <p>@param theRarity   the rarity of the potion
   * 
   * <p>@throws IllegalArgumentException if theBaseHeal <= 0 or theRarity is null
   */
  public HealingPotion(final int theBaseHeal, final Rarity theRarity) {
    super(theRarity);
    if (theBaseHeal <= 0) {
      throw new IllegalArgumentException("Base heal amount must be positive.");
    }
    if (theRarity == null) {
      throw new IllegalArgumentException("Rarity cannot be null.");
    }
    myBaseHeal = theBaseHeal;
  }

  /**
   * Returns the effective heal amount provided by this potion.
   * For simple design we add the potion rarity bonus to the base heal.
   * 
   * <p>@return the calculated heal amount
   */
  public int getHealAmount() {
    return myBaseHeal + getRarityBonus();
  }

  /**
   * Consumes this Healing Potion.
   * Prints a message indicating healing and the amount healed.
   */
  @Override
    public void consume() {
    System.out.println("You used a Healing Potion. Restored " + getHealAmount() + " HP!");
  }

  /**
   * Provides a short description of this Healing Potion item.
   * 
   * <p>@return a descriptive string showing the potion’s rarity and heal amount
   */
  @Override
  public String getDescription() {
    return "Healing Potion (" + getRarity() + ", Heals: " + getHealAmount() + " HP)";
  }
  
  @Override
  public String getName() {
    return "Healing Potion";
  }
}