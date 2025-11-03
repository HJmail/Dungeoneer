/**
 * Represents a general Potion item that can be consumed by the player to produce
 * temporary effects such as healing or enhanced vision. 
 * 
 * This abstract class defines shared potion behavior, including duration tracking
 * and the requirement for subclasses to implement specific consumption effects.
 *
 * All potion types (e.g., {@link HealingPotion}, {@link VisionPotion}) extend this
 * class to provide unique effects while maintaining consistent structure.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/1/25
 */
public abstract class Potion implements Item {
	
  /** The duration (in arbitrary units) for which the potion's effect lasts. */
  private int myDuration;
  
  /**
   * Constructs a Potion with a specified duration.
   * 
   * @param theDuration the time duration of the potion's effect
   */
  public Potion(int theDuration) {
	  myDuration = theDuration;
  }
	
  /**
   * Returns the duration of the potion's effect.
   * 
   * @return the potion's duration
   */
  public int getDuration() {
    return getDuration();
		
  }
  
  /**
   * Defines how the potion is consumed.
   * This abstract method must be implemented by subclasses to specify
   * what happens when the potion is used (e.g., healing, vision enhancement).
   */
  public abstract void consume();
	  
}
	
