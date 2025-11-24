package model;

/**
 * Represents an additional item Gold class for the player to collect and
 * use for the shop. 
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.1.2
 * 
 * <p>@date 11/16/25
 */
public class Gold implements Item {

  /** Represents the golds amount the player has as an integer. */
  private int myAmount; 

  /**
   * Constructs a new Gold object with a specified initial amount.
   * 
   * <p>@param theAmount the initial amount of gold the player starts with
   */
  public Gold(final int theAmount) {
    if (theAmount < 0) {
      throw new IllegalArgumentException("Gold amount cannot be negative.");
    }
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
   * <p>@param moreGold the additional gold to be added
   */
  public void addGold(int moreGold) {
    myAmount += moreGold;
  }
  
  /**
   * Provides a short description of this gold item.
   * 
   * <p>@return a descriptive string showing the current gold amount
   */
  public String getDescription() {
    return "Gold (" + myAmount + " coins)";
  }

  /**
   * Defines the behavior when gold is used.
   * Since gold itself cannot be 'used' directly, this simply prints
   * a message about the total gold held.
   */
  public void use() {
    System.out.println("You have " + myAmount + " gold coins.");
  }
}
