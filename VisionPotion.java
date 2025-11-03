/**
 * Represents a Vision Potion that reveals hidden parts of the dungeon
 * when consumed by the hero.
 * 
 * This class extends {@link Potion} and overrides the {@code consume()}
 * method to define the potion's effect.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/1/25
 */
public class VisionPotion extends Potion {

    /**
     * Constructs a VisionPotion with a specified duration.
     * 
     * @param theDuration the duration (in arbitrary units) for which 
     *                    the potion's vision effect lasts
     */
    public VisionPotion(int theDuration) {
        super(theDuration);
    }

    /**
     * Consumes this Vision Potion and prints a message describing its effect.
     */
    @Override
    public void consume() {
        System.out.println("You used a Vision Potion. You can now see hidden parts of the "
        		+ "dungeon!");
    }

    /**
     * Provides a short description of this Vision Potion item.
     * 
     * @return a descriptive string showing the potion’s purpose
     */
    @Override
    public String getDescription() {
        return "Vision Potion (reveals hidden parts of the dungeon)";
    }

    /**
     * Defines what happens when the potion is used.
     * By default, this calls the {@code consume()} method to activate the effect.
     */
    @Override
    public void use() {
        consume();
    }
}
