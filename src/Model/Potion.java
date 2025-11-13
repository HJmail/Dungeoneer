/**
 * Represents a general Potion item that can be consumed by the player to produce
 * temporary effects such as healing or enhanced vision. Potions have a base
 * duration and a rarity that modifies the final duration or effect strength.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 10/24/25
 */
public abstract class Potion implements Item {

    /** The rarity of this potion (affects duration/strength). */
    private Rarity myRarity;

    /**
     * Constructs a Potion with a specified base duration and rarity.
     * 
     * @param theDuration the base duration of the potion effect
     * @param theRarity   the rarity of the potion
     */
    public Potion(Rarity theRarity) {
        myRarity = theRarity;
    }

    /**
     * Returns the rarity bonus for potions (interpreted as extra duration/strength).
     * COMMON -> 0, UNCOMMON -> 1, RARE -> 2, EPIC -> 3, LEGENDARY -> 4
     * 
     * @return the integer duration/strength bonus from rarity
     */
    protected int getRarityBonus() {
        return switch (myRarity) {
            case COMMON -> 0;
            case UNCOMMON -> 1;
            case RARE -> 2;
            case EPIC -> 3;
            case LEGENDARY -> 4;
        };
    }

    /**
     * Returns the rarity of this potion.
     * 
     * @return the potion's rarity
     */
    public Rarity getRarity() {
        return myRarity;
    }

    /**
     * Subclasses must implement the specific consumption behavior.
     */
    public abstract void consume();

    /**
     * Generic description for a potion. Subclasses may override for more detail.
     * 
     * @return a short description of the potion
     */
    @Override
    public String getDescription() {
        return "Potion (" + myRarity + ")";
    }

    /**
     * Default use behavior: call consume() to apply the potion's effect.
     */
    @Override
    public void use() {
        consume();
    }
}
