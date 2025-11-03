/**
 * Represents the weapons for both the heros and monsters that they both can pick
 * up and use. This can also have the specific weapon from the most powerful to
 * the least powerful.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/1/25
 */
public class Weapon implements Item {
	
  /** the name of the weapon */
  private String myName;
  
  /** the amount of damage the weapon can inflict */
  private int myDamage;
	
  /** @return the weapon Stick with damage 5. */
  public static Weapon createStick() {
	  return new Weapon("Stick", 5);
  }
  
  /** @return the weapon Spear with damage 10. */
  public static Weapon createSpear() {
	  return new Weapon("Spear", 10);
  }
  
  /** @return the weapon Falchion with damage 15 */
  public static Weapon createFalchion() {
	  return new Weapon("Falchion", 15);
  }
  
  /** @return the weapon Flail with damage 20 */
  public static Weapon createFlail() {
	  return new Weapon("Flail", 20);
  }  
  
  /** @return the weapon MorningStar with damage 25 */
  public static Weapon createMorningStar() {
	  return new Weapon("Morning Star", 25);
  }
  
  /**
   * Constructs the specific weapons name and the damage 
   * 
   * @param theName the name of the weapon.
   * @param theDamage the amount of damage this weapon can inflict.
   */
  public Weapon(String theName, int theDamage) {
		myName = theName;
		myDamage = theDamage;
  }
	
  /**
   * Returns the amount of damage this weapon deals.
   * 
   * @return the weapon's damage value
   */
  public int getDamage() {
 return myDamage;		
  }
	
  /**
   * Provides a short description of the weapon, including its name and damage.
   * 
   * @return a descriptive string for the weapon
   */
  public String getName() {
    return myName;
		
  }
  
  public String getDescription() {
	  return myName + "(Damage: " + myDamage + ")";
  }
  
  /**
   * Defines the behavior when the weapon is used in battle.
   * Prints a message indicating the weapon's name and damage dealt.
   */
  public void use() {
	    System.out.println("You swing the " + myName + " for " + myDamage + " damage!");
	}

}
