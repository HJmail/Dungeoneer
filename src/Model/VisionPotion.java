package Model;

/**
 * Represents a Vision Potion that reveals hidden parts of the dungeon
 * when consumed by the hero. Duration scales with rarity.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/26/25
 */
public class VisionPotion extends Potion {
	
	private int myBaseDuration;

    /**
     * Constructs a VisionPotion with a specified base duration and rarity.
     * 
     * @param theDuration the base duration of the vision effect
     * @param theRarity   the rarity of the potion
     */
    public VisionPotion(int theDuration, Rarity theRarity) {
        super(theRarity);
        myBaseDuration = theDuration;
    }
    
    public int getDuration() {
        return myBaseDuration + getRarityBonus();
    }

    /**
     * Consumes this Vision Potion and prints a message describing its effect and duration.
     */
    @Override
    public void consume() {
        System.out.println("You used a Vision Potion. You can now see hidden parts of the dungeon for " + getDuration() + " turns!");
    }

    /**
     * Provides a short description of this Vision Potion item.
     * 
     * @return a descriptive string showing the potion’s rarity and duration
     */
    @Override
    public String getDescription() {
        return "Vision Potion (" + getRarity() + ", Duration: " + getDuration() + ")";
    }
}
