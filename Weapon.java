/**
 * Represents the weapons for both the heros and monsters that they both can pick
 * up and use. This can also have the specific weapon from the most powerful to
 * the less powerful.
 * 
 * @author Cristian Acevedo-Villasana
 * @verison 0.0.1
 * @date 10/26/25
 */
public class Weapon implements Item {
	
  private String myName;
  private int myDamage;
	
  public Weapon(String theName, int theDamage) {
		myName = theName;
		myDamage = theDamage;
  }
	
		
  public int getDamage() {
    return myDamage;
		
  }
	
  public String myName() {
    return myName;
		
  }
  
  public void use() {
	    System.out.println("You swing the " + myName + " for " + myDamage + " damage!");
	}

}
