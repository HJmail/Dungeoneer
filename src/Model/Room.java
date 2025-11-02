package Model;

import java.util.EnumSet;
import java.util.List;


/**
 * This class represents one room within the dungeon.
 */
public class Room 
{	
	
	/**
	 * This EnumSet represents the directions that exist
	 */
	private EnumSet<Direction> myDirections;
	
	/**
	 * This EnumSet represents the type of room it is
	 */
	private EnumSet<Feature> myFeatures;
	
	/**
	 * This EnumSet represents the items that are lootable from the room.
	 */
	private EnumSet<Item> myItems;
	
	/**
	 * This character field represents what is shown on the map.
	 */
	private char myCharRepresentation;
	
	/**
	 * This List holds all DungeonCharacters within the room. 
	 */
	List<DungeonCharacter> myCharactersInRoom;
	
	public Room()
	{
		
	}
	
	/**
	 * This is the Room constructor when we have all the data already.
	 * @param theDirections This is a EnumSet that represents existing doors relative to this room.
	 * @param theFeatures This is a EnumSet that represents the existing Features within this room.
	 * @param theItems This is the EnumSet that represents the existing Items within this room that is lootable.
	 * @param theCharRepresentation This is a char that represents the room on the map.
	 */
	public Room(final EnumSet<Direction> theDirections,
				final EnumSet<Feature> theFeatures,
				final EnumSet<Item> theItems,
				final char theCharRepresentation)
	{
		myDirections = EnumSet.copyOf(theDirections);
		myFeatures = EnumSet.copyOf(theFeatures);
		myItems = EnumSet.copyOf(theItems);
		myCharRepresentation = theCharRepresentation;
	}
	
	/**
	 * This enters the Hero into the room.
	 * @param theHero The Hero that entered the room. 
	 */
	public void enter(final Hero theHero)
	{
		myCharactersInRoom.add(theHero);
		activateRoom();
	}
	
	/**
	 * This gets the room's char that is used on the map.
	 * @return This is the char that is used on the map.
	 */
	public char getRoomChar()
	{
		return myCharRepresentation;
	}
	
	/**
	 * This starts the Room logic starting 
	 */
	private void activateRoom()
	{
		System.out.println(myDirections);
	}
	
	
	
	

	
	/**
	 *  This enum represents the Events or Features present within the room.
	 */
	enum Feature
	{
		ENTERANCE, EXIT, PIT, PILLAR, SHOP, ENCOUNTER
	}
	
	/**
	 * This enum represents what items are in the room.
	 */
	enum Item
	{
		HEALING_POTION, VISION_POTION
	}
}
