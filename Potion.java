
public abstract class Potion implements Item {
	
  private int myDuration;
  
  public Potion(int theDuration) {
	  myDuration = theDuration;
  }

	
  public int getDuration() {
    return getDuration();
		
  }
  
  public abstract void consume();
	  
}
	
