package Model;

/**
 * Represents an additional item Gold class for the player to collect and
 * use for the shop. 
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/25/25
 */
public class Gold implements Item {
	
  /** Represents the golds amount the player has as an integer */	
  private int myAmount; 
	
  /**
   * Constructs a new Gold object with a specified initial amount.
   * 
   * @param theAmount the initial amount of gold the player starts with
   */
  public Gold(int theAmount) {
    myAmount = theAmount;
		
  }

  /*
   * getAmount is a getter for getting the current amount of gold the player(user) has.
   * 
   * @return the amount of gold owned by the player.
   */
  public int getAmount() {
    return myAmount;
		
  }
  
  /**
   * myAmount equals the amount of gold that is added to the player's total.
   * 
   * @param moreGold the additional gold to be added
   */
  public void addGold(int moreGold) {
    myAmount += moreGold;
  }
  
  /**
   * Provides a short description of this gold item.
   * 
   * @return a descriptive string showing the current gold amount
   */
  public String getDescription() {
      return "Gold (" + myAmount + " coins)";
  }

  /**
   * Defines the behavior when gold is used.
   * <p>Since gold itself cannot be 'used' directly, this simply prints
   * a message about the total gold held.</p>
   */
  public void use() {
      System.out.println("You have " + myAmount + " gold coins.");
  }
}
