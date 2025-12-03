package model;

/**
 *  This is the enum for possible events in rooms.
 */
public enum RoomType 
{
	// type of events
	NONE('n'),
	PIT('o'),
	ENCOUNTER('x'),
	PILLAR('l'),
	EXIT('E'),
	START('e'),
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