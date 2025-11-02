package Model;

/**
 *  This is the enum for possible events in rooms.
 */
public enum EventType 
{
	// type of events
	NONE("Just a damp room with a cold draft."),
	PIT("AHHHHH, there seems to have been a pit!"),
	ENCOUNTER("Thats not friendly!"),
	SHOP("Want to trade?");
	
	/**
	 *  This is the description.
	 */
	private final String myDescription;
	
	/**
	 * This is the constructor for the enum.
	 * @param theDesc This is the description.
	 */
	EventType(final String theDesc)
	{
		myDescription = theDesc;
	}
	
	/**
	 * This is the getter method for the description.
	 * @return The description.
	 */
	public String getDescription()
	{
		return myDescription;
	}
}
