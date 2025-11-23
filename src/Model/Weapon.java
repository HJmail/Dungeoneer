package Model;

/**
 * Represents the weapons for both the heroes and monsters that they can pick
 * up and use. Weapons have a base damage and a rarity which modifies final damage.
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.0.1
 * 
 * <p>@date 11/1/25
 */
public class Weapon implements Item {

  /** The name of the weapon. */
  private String myName;

  /** The base damage (before rarity bonus). */
  private int myBaseDamage;

  /** The rarity of this weapon (affects damage). */
  private Rarity myRarity;

  /**
   * Constructs a weapon with a name, base damage, and rarity.
   * 
   * <p>@param theName the name of the weapon
   * 
   * <p>@param theBaseDamage the base damage of the weapon
   * 
   * <p>@param theRarity the rarity of the weapon
   */
  public Weapon(final String theName, final int theBaseDamage, final Rarity theRarity) {
    if (theName == null || theName.isBlank()) {
      throw new IllegalArgumentException("Weapon name cannot be null or empty.");
    }
    if (theBaseDamage <= 0) {
      throw new IllegalArgumentException("Weapon damage must be positive.");
    }
    if (theRarity == null) {
      throw new IllegalArgumentException("Rarity cannot be null.");
    }
    myName = theName;
    myBaseDamage = theBaseDamage;
    myRarity = theRarity;
  }

  /**
   * Returns the rarity bonus for weapons.
   * COMMON -> 0, UNCOMMON -> 2, RARE -> 4, EPIC -> 6, LEGENDARY -> 8
   * 
   * <p>@return the integer damage bonus from rarity
   */
  private int getRarityBonus() {
    return switch (myRarity) {
      case COMMON -> 0;
      case UNCOMMON -> 2;
      case RARE -> 4;
      case EPIC -> 6;
      case LEGENDARY -> 8;
    };
  }

  /**
   * Returns the final damage of the weapon (base + rarity bonus).
   * 
   * <p>@return the weapon's damage value
   */
  public int getDamage() {
    return myBaseDamage + getRarityBonus();
  }

  /**
   * Returns the name of this weapon.
   * 
   * <p>@return the weapon's name
   */
  public String getName() {
    return myName;
  }

  /**
   * Returns the rarity of this weapon.
   * 
   * <p>@return the weapon's rarity
   */
  public Rarity getRarity() {
    return myRarity;
  }

  /**
   * Provides a short description of the weapon, including its rarity and damage.
   * 
   * <p>@return a descriptive string for the weapon
   */
  @Override
  public String getDescription() {
    return myName + " (" + myRarity + ", Damage: " + getDamage() + ")";
  }

  /**
   * Defines the behavior when the weapon is used in battle.
   * Prints a message indicating the weapon's name and damage dealt.
   */
  @Override
  public void use() {
    System.out.println("You swing the " + myName + " for " + getDamage() + " damage!");
  }

  /** Static factory methods (allow caller to choose rarity). */
  
  public static Weapon createStick(Rarity rarity) {
    return new Weapon("Stick", 5, rarity);
  }

  /**
   * Creates a Spear weapon instance with a base damage of 10.
   * This factory method provides a convenient way to instantiate a
   * {@link Weapon} without manually specifying the name and damage values.
   *
   * @param rarity the rarity of the Spear (affects its final damage)
   * @return a new {@link Weapon} object representing a Spear
   */
  public static Weapon createSpear(final Rarity rarity) {
    return new Weapon("Spear", 10, rarity);
  }

  /**
   * Creates a Flail weapon instance with a base damage of 15.
   * The Flail is a mid-tier weapon that benefits from rarity-based bonuses.
   *
   * @param rarity the rarity of the Flail (affects its final damage)
   * @return a new {@link Weapon} object representing a Flail
   */
  public static Weapon createFlail(final Rarity rarity) {
    return new Weapon("Flail", 15, rarity);
  }

  /**
   * Creates a Falchion weapon instance with a base damage of 20.
   * The Falchion is a high-tier weapon, dealing significantly more damage
   * than standard weapons and scaling with rarity.
   *
   * @param rarity the rarity of the Falchion (affects its final damage)
   * @return a new {@link Weapon} object representing a Falchion
   */
  public static Weapon createFalchion(final Rarity rarity) {
    return new Weapon("Falchion", 20, rarity);
  }

  /**
   * Creates a Morning Star weapon instance with a base damage of 25.
   * The Morning Star is among the strongest melee weapons available,
   * gaining large bonuses from higher rarity levels.
   *
   * @param rarity the rarity of the Morning Star (affects its final damage)
   * @return a new {@link Weapon} object representing a Morning Star
   */
  public static Weapon createMorningStar(final Rarity rarity) {
    return new Weapon("Morning Star", 25, rarity);
  }
}

