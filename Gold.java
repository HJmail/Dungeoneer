
public class Gold implements Item {
	
  private int myAmount; 
	
  public Gold(int theAmount) {
    myAmount = theAmount;
		
  }
	
  public int getAmount() {
    return myAmount;
		
  }
  
  public void addGold(int moreGold) {
    myAmount += moreGold;
  }

}
