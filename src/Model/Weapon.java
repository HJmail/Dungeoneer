/**
 * Represents the weapons for both the heroes and monsters that they can pick
 * up and use. Weapons have a base damage and a rarity which modifies final damage.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/1/25
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
     * @param theName    the name of the weapon
     * @param theDamage  the base damage of the weapon
     * @param theRarity  the rarity of the weapon
     */
    public Weapon(String theName, int theBaseDamage, Rarity theRarity) {
        myName = theName;
        myBaseDamage = theBaseDamage;
        myRarity = theRarity;
    }

    /**
     * Returns the rarity bonus for weapons.
     * COMMON -> 0, UNCOMMON -> 2, RARE -> 4, EPIC -> 6, LEGENDARY -> 8
     * 
     * @return the integer damage bonus from rarity
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
     * @return the weapon's damage value
     */
    public int getDamage() {
        return myBaseDamage + getRarityBonus();
    }

    /**
     * Returns the name of this weapon.
     * 
     * @return the weapon's name
     */
    public String getName() {
        return myName;
    }

    /**
     * Returns the rarity of this weapon.
     * 
     * @return the weapon's rarity
     */
    public Rarity getRarity() {
        return myRarity;
    }

    /**
     * Provides a short description of the weapon, including its rarity and damage.
     * 
     * @return a descriptive string for the weapon
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

    /** Static factory methods (allow caller to choose rarity) */
    
    public static Weapon createStick(Rarity rarity) {
        return new Weapon("Stick", 5, rarity);
    }

    public static Weapon createSpear(Rarity rarity) {
        return new Weapon("Spear", 12, rarity);
    }

    public static Weapon createFlail(Rarity rarity) {
        return new Weapon("Flail", 18, rarity);
    }

    public static Weapon createFalchion(Rarity rarity) {
        return new Weapon("Falchion", 22, rarity);
    }

    public static Weapon createMorningStar(Rarity rarity) {
        return new Weapon("Morning Star", 28, rarity);
    }
}

