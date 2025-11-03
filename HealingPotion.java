/**
 * Represents a Healing Potion item that restores the hero's hit points when consumed.
 * 
 * This class extends the {@link Potion} superclass and overrides the 
 * {@code consume()} method to define the healing effect. The amount of health 
 * restored can vary depending on the implementation in the main game.
 * 
 * Part of the Dungeon Adventure project's Inventory system.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/31/25
 */
public class HealingPotion extends Potion {

    /**
     * Constructs a HealingPotion with a specified duration value.
     * 
     * @param theDuration the length of time (in arbitrary units) 
     * for which the potion's effect lasts or is active
     */
    public HealingPotion(int theDuration) {
        super(theDuration);
    }

    /**
     * Consumes this Healing Potion.
     * -Prints a message indicating that the player has used the potion and restored health.
     * In a full implementation, this method could be expanded to actually increase
     * the hero’s hit points.</p>
     * 
     * @override Indicates that this method overrides consume.
     */
    @Override
    public void consume() {
        System.out.println("You used a Healing Potion. Restored health!");
    }
    

    /**
     * Provides a short description of this Healing Potion item.
     * 
     * @return a descriptive string showing the potion’s purpose
     */
    public String getDescription() {
        return "Healing Potion (restores health)";
    }

    /**
     * Defines what happens when the potion is used.
     * By default, this calls the {@code consume()} method to activate the effect.
     */
    public void use() {
        consume();
    }
}
