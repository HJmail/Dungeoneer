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
	private EnumSet<EventType> myEvents;
	
	/**
	 * This EnumSet represents the items that are lootable from the room.
	 */
	private EnumSet<ItemType> myItems;
	
	/**
	 * This character field represents what is shown on the map.
	 */
	private char myCharRepresentation;
	
	/**
	 * This List holds all DungeonCharacters within the room. 
	 */
	List<DungeonCharacter> myCharactersInRoom;
	
	/**
	 * This field holds a reference to hero if they are here.
	 */
	private Hero myHero;
	
	public Room()
	{
		myCharRepresentation = '*';
	}
	
	/**
	 * This is the Room constructor when we have all the data already.
	 * @param theDirections This is a EnumSet that represents existing doors relative to this room.
	 * @param theFeatures This is a EnumSet that represents the existing Features within this room.
	 * @param theItems This is the EnumSet that represents the existing Items within this room that is lootable.
	 * @param theCharRepresentation This is a char that represents the room on the map.
	 */
	public Room(final EnumSet<Direction> theDirections,
				final EnumSet<EventType> theEvents,
				final EnumSet<ItemType> theItems,
				final char theCharRepresentation)
	{
		myDirections = EnumSet.copyOf(theDirections);
		myEvents = EnumSet.copyOf(theEvents);
		myItems = EnumSet.copyOf(theItems);
		myCharRepresentation = theCharRepresentation;
	}
	
	/**
	 * This enters the Hero into the room.
	 * @param theHero The Hero that entered the room. 
	 */
	public void enter(final Hero theHero)
	{
		myHero = theHero;
		myCharRepresentation = 'C';
		activateRoom();
	}
	
	public void exit()
	{
		myCharRepresentation = '-';
		myHero = null;
	}
	
	/**
	 * This gets the room's char that is used on the map.
	 * @return This is the char that is used on the map.
	 */
	public char getRoomChar()
	{
		return myCharRepresentation;
	}
	
	public EnumSet<Direction> getDirections()
	{
		return myDirections; // want it to be mutable 
	}
	
	public EnumSet<EventType> getEvents()
	{
		return myEvents; // want it to be mutable 
	}
	
	/**
	 * This starts the Room logic starting 
	 */
	private void activateRoom()
	{
		System.out.println(myDirections);
	}
	
	/**
	 * This sets the Enums of the room.
	 * @param theItems EnumSet that represents the room.
	 */
	public void setItems(final EnumSet<ItemType> theItems)
	{
		myItems = EnumSet.copyOf(theItems);
	}
	
	/**
	 * This sets the Events of the room.
	 * @param theEvents EnumSet that represents the events within the room.
	 */
	public void setEvents(final EnumSet<EventType> theEvents)
	{
		myEvents = EnumSet.copyOf(theEvents);
	}
	
	/**
	 * This sets the Directions of the room.
	 * @param theDirections The available directions from the room.
	 */
	public void setDirections(final EnumSet<Direction> theDirections)
	{
		myDirections = EnumSet.copyOf(theDirections);
		//System.out.println(myDirections);
	}
	
	/**
	 * This sets the Char of the room.
	 * @param theChar The char of the room.
	 */
	public void setRoomChar(final char theChar)
	{
		myCharRepresentation = theChar;
	}
}
