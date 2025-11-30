package model;

import java.util.ArrayList;
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
	private RoomType myRoomType;
	
	/**
	 * This EnumSet represents the items that are lootable from the room.
	 */
	private EnumSet<ItemType> myItems;
	
	/**
	 * This List holds all DungeonCharacters within the room. 
	 */
	private List<Monster> myMonstersInRoom;
	
	/**
	 * This field holds a reference to hero if they are here.
	 */
	//private Hero myHero;
	
	/**
	 *  Represents maze depth.
	 */
	private int myDepth;
	
	private Shopkeeper myShop;
	
	private boolean myIsActivated;
	
	private boolean myIsVisable;
	
	private boolean myIsLooted;
	
	public Room()
	{
		myDirections = EnumSet.noneOf(Direction.class);
		myRoomType = RoomType.NONE;
		myItems = EnumSet.noneOf(ItemType.class);
	}
	
	/**
	 * This is the Room constructor when we have all the data already.
	 * @param theDirections This is a EnumSet that represents existing doors relative to this room.
	 * @param theFeatures This is a EnumSet that represents the existing Features within this room.
	 * @param theItems This is the EnumSet that represents the existing Items within this room that is lootable.
	 * @param theCharRepresentation This is a char that represents the room on the map.
	 */
	public Room(final EnumSet<Direction> theDirections,
				final RoomType theEvent,
				final EnumSet<ItemType> theItems)
	{
		myDirections = EnumSet.copyOf(theDirections);
		myRoomType = theEvent;
		myItems = EnumSet.copyOf(theItems);
	}
	
	public void exit(final Direction theDirection)
	{
		if(myRoomType != RoomType.START 
				&& myRoomType != RoomType.EXIT) 
		{
			if(theDirection == Direction.NORTH || theDirection.opposite() == Direction.NORTH)
			{	
				setRoomType(RoomType.TRAVELED_NS);
			}
			else
			{
				setRoomType(RoomType.TRAVELED_EW);
			}
		}
	}
	
	/**
	 * This gets the room's char that is used on the map.
	 * @return This is the char that is used on the map.
	 */
	public char getRoomChar()
	{
		return myRoomType.getChar();
	}
	
	public EnumSet<Direction> getDirections()
	{
		return myDirections;
	}
	
	public RoomType getRoomType()
	{
		return myRoomType;
	}
	
	public int getDepth()
	{
		return myDepth;
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
	public void setRoomType(final RoomType theEvent)
	{
		myRoomType = theEvent;
	}
	
	/**
	 * This sets the Directions of the room.
	 * @param theDirections The available directions from the room.
	 */
	public void setDirections(final EnumSet<Direction> theDirections)
	{
		myDirections = EnumSet.copyOf(theDirections); 
	}
	
	public void setDepth(final int theDepth)
	{
		myDepth = theDepth;
	}

	
	public void addMonster(final Monster theCharacter)
	{
		myMonstersInRoom.add(theCharacter);
	}
	
	public void removeMonster(final Monster theCharacter)
	{
		myMonstersInRoom.remove(theCharacter);
	}
	
	public Shopkeeper getShopkeeper()
	{
		return myShop;
	}
	
	public void setShopkeeper(final Shopkeeper theShop)
	{
		myShop = theShop;
	}
	
	public void setActivated(final boolean theActivated)
	{
		myIsActivated = theActivated;
	}
	
	public void setMonsters(final List<Monster> theMonsters)
	{
		myMonstersInRoom = new ArrayList<Monster>(theMonsters);
	}
	
	public List<Monster> getMonsters()
	{
		return new ArrayList<Monster>(myMonstersInRoom);
	}
	
	public boolean hasCombat()
	{
		return myMonstersInRoom.size() == 0;
	}
	
	public boolean isActivated()
	{
		return myIsActivated;
	}
	
	public EnumSet<ItemType> getItems()
	{	
		return EnumSet.copyOf(myItems);
	}
	
	public void setVisability(final boolean theView)
	{
		myIsVisable = theView;
	}
	
	public boolean getVisability()
	{
		return myIsVisable;
	}
	
	public boolean isLooted()
	{
		return myIsLooted;
	}
	
	public void setIsLooted(final boolean theLooted)
	{
		myIsLooted = theLooted;
	}
}

