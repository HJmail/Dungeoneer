package model;

import java.util.Random;

/**
 * Direction enum representing cardinal directions
 * and their behavior in the Dungeoneer project.
 *
 * @version 1.3
 */
public enum Direction {

    /**
     * North (up).
     */
    NORTH('N'),

    /**
     * West (left).
     */
    WEST('W'),

    /**
     * South (down).
     */
    SOUTH('S'),

    /**
     * East (right).
     */
    EAST('E');

  /** Random generator for selecting random directions. */
  private static final Random RANDOM = new Random();

  /** The character representation of this direction. */
  private final char myLetter;

  // Constructor
  Direction(final char theLetter) {
    myLetter = theLetter;
  }

  // Static Lookup Methods
  /**
   * Returns the Direction represented by the given letter.
   *
   * @param theLetter The character to convert.
   * @return the matching Direction, or null if none match.
   */
  public static Direction valueOf(final char theLetter) {
    for (final Direction direction : Direction.values()) {
      if (direction.letter() == theLetter) {
        return direction;
      }
    }
    return null;
  }

  /**
   * Returns a random Direction.
   */
  public static Direction randomDirection() {
    return values()[RANDOM.nextInt(values().length)];
  }

  // Instance Methods
  /** @return the character representing this direction. */
  public char letter() {
    return myLetter;
  }

  /**
   * Returns the Direction 90 degrees counter-clockwise.
   */
  public Direction left() {
    return switch (this) {
      case NORTH -> WEST;
      case WEST  -> SOUTH;
      case SOUTH -> EAST;
      case EAST  -> NORTH;
    };
  }

  /**
   * Returns the Direction 90 degrees clockwise.
   */
  public Direction right() {
    return switch (this) {
      case NORTH -> EAST;
      case EAST  -> SOUTH;
      case SOUTH -> WEST;
      case WEST  -> NORTH;
    };
  }

  /**
   * Returns the opposite direction (180 degrees turn).
   *
   * NOTE: This was originally named reverse(), and your
   * partner’s code expects opposite(), so opposite() calls reverse().
   */
  public Direction reverse() {
    return left().left();
  }

  /**
   * Alias for reverse(). Required for your partner’s code.
   *
   * @return the Direction opposite this one.
   */
  public Direction opposite() {
    return reverse();
    }

    /**
     * Change in x-coordinate when moving in this direction.
     */
    public int dx() {
        return switch (this) {
            case WEST -> -1;
            case EAST -> 1;
            default   -> 0;
        };
    }

    /**
     * Change in y-coordinate when moving in this direction.
     */
    public int dy() {
        return switch (this) {
            case SOUTH -> 1;
            case NORTH -> -1;
            default    -> 0;
    };
  }
}