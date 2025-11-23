package Model;

/**
 *  This is the enum for possible events in rooms.
 */
public enum RoomType 
{
	// type of events
	NONE('*'),
	PIT('o'),
	ENCOUNTER('x'),
	PILLAR('l'),
	EXIT('E'),
	START('e'),
	TRAVELED_NS('|'),
	TRAVELED_EW('-'),
	SHOP('s'),
	TREASURE('t');
	
	private char myChar;
	
	RoomType(final char theCharacter)
	{
		myChar = theCharacter;
	}
	
	public char getChar()
	{
		return myChar;
	}
}

