
/**
 * HealPotion represents a Healing Potion that restores health to the player.
 * Extends the abstract Potion class.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/25/25
 */
public class HealingPotion extends Potion {
	
  private int DURATION;
  
  public HealingPotion(int theDuration) {
	  super(theDuration);
  }
  
  @Override
  public void consume() {
	  System.out.println("You used a Healing Potion. Restored health!");
	  
  }

}
