package Model;

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
  
  public void addItem(Item theItem) {
	  
  }
  
  public void removeItem(Item theItem) {
	  
  }
  
  public boolean hasItem(Item theItem) {
	  return false;
  }
	
  public boolean canExit() {
	 return false;
	 
  }
	
}