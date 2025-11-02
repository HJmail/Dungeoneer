package Model;

/**
 * This enum represents what directions exist from this room
 */
public enum Direction
{
	// This is the displacement patterns for the Enums.
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);
	
	/**
	 * This is the X displacement
	 */
	public final int myDisplacementX;
	
	/**
	 * This is the Y displacement
	 */
	public final int myDisplacementY;
	
	/**
	 * This is the constructor for the Direction Enum
	 * @param theDisplacementX This is the displacement for X
	 * @param theDisplacementY This is the displacement for Y
	 */
	Direction(final int theDisplacementX, final int theDisplacementY)
	{
		myDisplacementX = theDisplacementX;
		myDisplacementY = theDisplacementY;	
	}
}
