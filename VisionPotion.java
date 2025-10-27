/**
 * This class represents the duration and consumption message of the VisionPotion.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/26/25
 */
public class VisionPotion extends Potion {
	
	private int DURATION;
	
	public VisionPotion(int theDuration) {
		super(theDuration);
	}
	
	public void consume() { 
		System.out.print("You used a Vision Potion. "
				+ "You can now see hidden parts of the dungeon");
		
	}

}
