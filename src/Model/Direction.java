package Model;

/**
 * This enum represents what directions exist from this room
 */
public enum Direction
{
	NORTH,
	EAST,
	SOUTH,
	WEST;
	
	public Direction opposite()
	{
		switch(this)
		{
        case NORTH: return SOUTH;
        case SOUTH: return NORTH;
        case EAST:  return WEST;
        case WEST:  return EAST;
		}
		return null;
	}
}
