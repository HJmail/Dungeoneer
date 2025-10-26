package Model;

import java.util.List;

/**
 * This class represents one room within the dungeon.
 */
public class Room 
{	
	/**
	 *  boolean representing if this room is a pit.
	 */
	boolean myHasPit = false;
	
	/** 
	 * boolean representing if this room is a entrance.
	 */
	boolean myHasEntrance = false;
	
	/**
	 *  Boolean representing if this room is a exit.
	 */
	boolean myHasExit = false;
	
	// Might remove if we make shop keeper
	/**
	 * boolean representing if there is a healing potion 
	 */
	boolean myHealingPotion = false;
	
	// Might remove if we make shop keeper
	/**
	 * boolean representing if there is a vision potion 
	 */
	boolean myVisionPotion = false;
	
	/**
	 *  boolean representing if there is a north door.
	 */
	boolean myNorthDoor = false;
	
	/**
	 *  boolean representing if there is a east Door.
	 */
	boolean myEastDoor = false;
	
	/**
	 *  boolean representing if there is a south door.
	 */
	boolean mySouthDoor = false;
	
	/**
	 *  boolean representing if there is a west door.
	 */
	boolean myWestDoor = false;
	
	/**
	 * This is the pillar for the room if one.
	 */
	Pillar myPillar;
	
	/**
	 * This List holds all DungeonCharacters within the room. 
	 */
	List<DungeonCharacter> myCharactersInRoom;
	
	public Room()
	{
		
	}
	
	public Room(final boolean thePit,
				final boolean theEnterance,
				final boolean theExit,
				final boolean theHealingPotion,
				final boolean theVisionPotion,
				final boolean thePillar,
				final boolean theNorth,
				final boolean theEast,
				final boolean theSouth,
				final boolean theWest)
	{
		
	}
}
