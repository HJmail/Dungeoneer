package Model;

/**
 * Represents one of the four Pillars of OO that the player must collect
 * to win the Dungeon Adventure game.
 * 
 * Each pillar is identified by a character type:
 * 'A' for Abstraction, 'E' for Encapsulation.
 * 'I' for Inheritance, and 'P' for Polymorphism.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/1/25
 */
public class Pillar implements Item {

    /** The type of pillar represented by a character ('A', 'E', 'I', 'P'). */
    private char myPillarType;

    /**
     * Constructs a Pillar with a specified type.
     * 
     * @param thePillarType the character representing the pillar's type
     */
    public Pillar(char thePillarType) {
        myPillarType = thePillarType;
    }

    /**
     * Returns the pillar type character.
     * 
     * @return the character representing the pillar type
     */
    public char getPillarType() {
        return myPillarType;
    }

    /**
     * Provides a short description of this Pillar item.
     * 
     * @return a string describing the pillar and its type
     */
    @Override
    public String getDescription() {
        return "Pillar of " + getPillarMeaning();
    }

    /**
     * Defines the behavior when the pillar is used.
     * Pillars cannot be 'used' directly, so this method simply prints a message.
     */
    @Override
    public void use() {
        System.out.println("You examine the Pillar of " + getPillarMeaning() + ".");
    }

    /**
     * Returns the full name of the pillar based on its character type.
     * 
     * @return the name of the OO concept represented by this pillar
     */
    private String getPillarMeaning() {
        return switch (Character.toUpperCase(myPillarType)) {
            case 'A' -> "Abstraction";
            case 'E' -> "Encapsulation";
            case 'I' -> "Inheritance";
            case 'P' -> "Polymorphism";
            default -> "Unknown";
        };
    }
}
