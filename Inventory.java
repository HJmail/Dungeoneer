import java.util.ArrayList;
import java.util.List;

/**
 * This class functions for the inventory to basically add the
 * inventory for the dungeonCharacters of Heros and Monsters. 
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/24/25
 */

public class Inventory {
	
  private boolean myPillarACollected;
  private boolean myPillarECollected;
  private boolean myPillarICollected;
  private boolean myPillarPCollected;
  private List<Item> myInventory;
	
  public Inventory() {
    myInventory = new ArrayList<>();
    
  }
    
  public List<Item> getInventory() {
	return null; 
	  
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

  
  public void removeItem(Item theItem) {
    myInventory.remove(theItem);
	  
  }
  
  public boolean hasItem(Item theItem) {
	  return myInventory.contains(theItem);
  }
	
  /**
   * canExit is a method where the player has to have all pillars to be able to Exit
   * the Dungeon to Win.
   * 
   * @return the "&" operator if all are true will be true and if one is false will be false
   */
  public boolean canExit() {
	 return myPillarACollected && myPillarECollected
			 && myPillarICollected && myPillarPCollected;
	 
  }
	
}