package model;

/**
 * Enum representing types of items findable.
 */
public enum ItemType 
{
	
	// These are the Different types of findable items.
	HEALING_POTION("This potion seems to glimmer a shiny crimson red."),
	VISION_POTION("This potion seems to shine white and silver."),
	PILLAR("This seems like a pillar, I wonder what it could be used for."),
	WEAPON("This can kill!"),
	GOLD("Shiny!");
	
	/**
	 * This is a description for the item.
	 */
	private final String  myDescription;
	
	/**
	 * This is the constructor for the enum.
	 * @param theDesc The description of the item.
	 */
	ItemType(final String theDesc)
	{
		myDescription = theDesc;
	}
	
	
	
	/**
	 * Getter method that returns the description.
	 * @return The description for the item.
	 */
	public String getDescription()
	{
		return myDescription;
	}
}
