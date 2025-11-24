package model;

/**
 * Represents one of the four Pillars of OO that the player must collect
 * to win the Dungeon Adventure game.
 * 
 * <p>Each pillar is identified by a character type:
 * 'A' for Abstraction, 'E' for Encapsulation.
 * 'I' for Inheritance, and 'P' for Polymorphism.
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.0.1
 * 
 * <p>@date 11/16/25
 */
public class Pillar implements Item {

  /** The type of pillar represented by a character ('A', 'E', 'I', 'P'). */
  private char myPillarType;

  /**
   * Constructs a Pillar with a specified type.
   * 
   * <p>@param thePillarType the character representing the pillar's type
   * 
   * <p>@throws IllegalArgumentException if thePillarType is not A, E, I, or P
   */
  public Pillar(final char thePillarType) {
    char upper = Character.toUpperCase(thePillarType);
    if (upper != 'A' && upper != 'E' && upper != 'I' && upper != 'P') {
      throw new IllegalArgumentException("Invalid pillar type: must be A, E, I, or P.");
    }
    myPillarType = thePillarType;
  }

  /**
   * Returns the pillar type character.
   * 
   * <p>@return the character representing the pillar type
   */
  public char getPillarType() {
    return myPillarType;
  }

  /**
   * Provides a short description of this Pillar item.
   * 
   * <p>@return a string describing the pillar and its type
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
   * <p>@return the name of the OO concept represented by this pillar
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