package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * This class represents one room within the dungeon.
 */
public class Room 
{	
	private static final int ROOM_DIMENSION = 13;
	
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
	
	private Tile[][] myTiles; 
	
	private boolean[][] myFullTiles;
	
	/**
	 *  Represents maze depth.
	 */
	private int myDepth;
	
	private Shopkeeper myShop;
	
	private boolean myIsActivated;
	
	private boolean myIsVisable;
	
	private boolean myIsLooted;
	
	private Point myHeroRoomLocation;
	
	public Room()
	{
		myDirections = EnumSet.noneOf(Direction.class);
		myRoomType = RoomType.NONE;
		myItems = EnumSet.noneOf(ItemType.class);
		myTiles = new Tile[ROOM_DIMENSION][ROOM_DIMENSION];
		myFullTiles = new boolean[ROOM_DIMENSION][ROOM_DIMENSION];
		myHeroRoomLocation = new Point(0,0);
	}
	
	public Tile getTile(final Point thePoint)
	{
		return myTiles[(int) thePoint.getX()][(int) thePoint.getY()];
	}
	
	public int getTilesRows()
	{
		return myTiles.length;
	}
	
	public int getTilesCols()
	{
		return myTiles[0].length;
	}
	
	public boolean[][] getFullTiles()
	{
		return myFullTiles.clone();
	}
	
	public void setMiddleTile(final TileType theType)
	{
		setTile(new Point(ROOM_DIMENSION/2 + 1, ROOM_DIMENSION/2 + 1), theType);
	}
	
	public void setTile(final Point theTile, final TileType theType)
	{
		int row = (int) theTile.getX();
		int col = (int) theTile.getY();
		
		if(myFullTiles[row][col]) // checks if open... 
		{
			myFullTiles[row][col] = false; // closes the tile
			myTiles[row][col] = new Tile(theType);
		}
	}
	
	public void exit(final Direction theDirection)
	{
		setActivated(true);
		setVisability(true);
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
	
	public boolean isVisable()
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

