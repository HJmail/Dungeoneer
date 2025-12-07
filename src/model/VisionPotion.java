package model;

/**
 * Represents a Vision Potion that reveals hidden parts of the dungeon
 * for a short time when consumed by the hero.
 *
 *<p>Currently this class just stores a duration; the actual reveal logic
 * can be handled elsewhere (e.g., in the view/controller) when the potion is used.
 *
 * @author Cristian Acevedo-Villasana
 * @version 0.0.2
 * @date 12/04/25
 */
public class VisionPotion extends Potion {

  /** Number of turns the vision effect should last. */
  private final int myDuration;

  /**
   * Default constructor – 3-turn duration.
   s*/
  public VisionPotion() {
    this(3);
  }

  /**
   * Creates a Vision Potion with a custom duration.
   *
   * @param theDuration how many turns the effect lasts
   */
  public VisionPotion(final int theDuration) {
    myDuration = theDuration;
  }

  /**
   * Returns the duration of this potion's effect, in turns.
   *
   * @return duration in turns
   */
  public int getDuration() {
    return myDuration;
  }

  @Override
  protected void consume() {
    // Hook into dungeon/board reveal effects as needed.
    System.out.println("You drink a Vision Potion. The dungeon becomes clearer...");
  }

  @Override
  public String getName() {
    return "Vision Potion";
  }

  @Override
  public String getDescription() {
    return "Vision Potion (Duration: " + myDuration + " turns)";
  }
}