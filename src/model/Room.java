package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * This class represents one room within the dungeon.
 * @author Skyler Z. Broussard
 * @version 12/6/2025
 */
public class Room implements Serializable 
{	
	/**
	 * This is the serial version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the given room dimension.
	 */
	private static final int ROOM_DIMENSION = 13;
	
	/**
	 * This is the pit damage.
	 */
	private static final int PIT_DMG = 5;
	
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
	 * This is the Tile 2d array that represents the room.
	 */
	private Tile[][] myTiles; 
	
	/**
	 * This is a 2d Array that represents if a tile is filled.
	 */
	private boolean[][] myFullTiles;
	
	/**
	 *  Represents maze depth.
	 */
	private int myDepth;
	
	/**
	 * This is the Shop keeper in room if there is one.
	 */
	private Shopkeeper myShop;
	
	/**
	 *  Determines if room is already activated... DEPRICATED
	 */
	private boolean myIsActivated;
	
	/**
	 * This checks if the room is visible.
	 */
	private boolean myIsVisable;
	
	/**
	 * This represents the Hero tile Location
	 */
	private Point myHeroTileLocation;
	
	/** 
	 * This is the constructor method for room.
	 */
	public Room()
	{
		myDirections = EnumSet.noneOf(Direction.class);
		myRoomType = RoomType.NONE;
		myItems = EnumSet.noneOf(ItemType.class);
		myTiles = new Tile[ROOM_DIMENSION][ROOM_DIMENSION];
		myFullTiles = new boolean[ROOM_DIMENSION][ROOM_DIMENSION];
		createBaseTiles();
		myHeroTileLocation = new Point(0,0);
		myMonstersInRoom = new ArrayList<Monster>();
	}
	
	/**
	 * This activates the given tile.
	 * @param thePoint This is the Point of the tile.
	 * @param theHero This is the Hero.
	 * @return boolean representing if the hero can exit.
	 */
	public boolean activateTile(final Point thePoint, final Hero theHero)
	{
		Tile tile = getTile(thePoint);
		DungeonTile type = tile.getTileType();
		boolean returnbool = false;
		
		
		if(tile.hasItem())
		{
			theHero.getInventory().addItem(tile.getItem());
		}
		else if(type == DungeonTile.PIT)
		{
			theHero.setHitPoints(theHero.getHitPoints() - PIT_DMG);
		}
		else if(type == DungeonTile.EXIT)
		{
			returnbool = theHero.getInventory().getCollectedPillarsCount() == 4;
		}
		
		return returnbool;
		
	}
	
	/**
	 * This method check if hero can move to new spot in room.
	 * @param thePoint The Point of the room.
	 * @return boolean if hero can move.
	 */
	public boolean canMoveTo(final Point thePoint)
	{
		int row = (int) thePoint.getX();
		int col = (int) thePoint.getY();
		
	    if (row < 0 || row >= getTilesRows() || col < 0 || col >= getTilesCols()) 
	    {
	    	return false;
	    }
	    Tile target = getTile(thePoint);
	    
	    return target.isWalkable();
	}
	
	/**
	 * This method creates the base tiles for the room.
	 */
	private void createBaseTiles()
	{
		for(int row = 0; row < myTiles.length; row++)
		{
			for(int col = 0; col < myTiles[0].length; col++)
			{
				myTiles[row][col] = new Tile(DungeonTile.FLOOR); // base
			}
		}
	}
	
	/**
	 * Gets the given tile.
	 * @param thePoint point for tile.
	 * @return the Tile.
	 */
	public Tile getTile(final Point thePoint)
	{
		return myTiles[(int) thePoint.getX()][(int) thePoint.getY()];
	}
	
	/**
	 * This is the Tiles' rows size
	 * @return size of rows.
	 */
	public int getTilesRows()
	{
		return myTiles.length;
	}
	
	/**
	 * This it the Tiles' cols size
	 * @return size of cols.
	 */
	public int getTilesCols()
	{
		return myTiles[0].length;
	}
	
	/**
	 * This the gets the 2d array that holds if a room is full.
	 * @return
	 */
	public boolean[][] getFullTiles()
	{
		return myFullTiles.clone();
	}
	
	/**
	 * This sets the middle tile of a room.
	 * @param theType The DungeonTile we need to set.
	 */
	public void setMiddleTile(final DungeonTile theType)
	{
		setTile(new Point(ROOM_DIMENSION/2, ROOM_DIMENSION/2), theType);
	}
	
	public void setTile(final Point theTile, final DungeonTile theType)
	{
		int row = (int) theTile.getX();
		int col = (int) theTile.getY();
		
		if(!myFullTiles[row][col]) // checks if open... 
		{
			if(theType != DungeonTile.FLOOR)
			{
				myFullTiles[row][col] = true; // closes the tile
			}
			myTiles[row][col].setTile(theType);
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
	
	/**
	 * This gets the EnumSet that of available directions
	 * @return
	 */
	public EnumSet<Direction> getDirections()
	{
		return myDirections;
	}
	
	/**
	 * This gets the room's type
	 * @return Enum of room type.
	 */
	public RoomType getRoomType()
	{
		return myRoomType;
	}
	
	/**
	 * This is the depth of the room.
	 * @return depth of room.
	 */
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
	
	/**
	 * This sets the depth of the room
	 * @param theDepth the new depth of the room.
	 */
	public void setDepth(final int theDepth)
	{
		myDepth = theDepth;
	}

	/** 
	 * This adds monsters within the room.
	 * @param theCharacter the Monster.
	 */
	public void addMonster(final Monster theCharacter)
	{
		myMonstersInRoom.add(theCharacter);
	}
	
	/**
	 * This removes the monster.
	 * @param theCharacter The Monster to remove.
	 */
	public void removeMonster(final Monster theCharacter)
	{
		myMonstersInRoom.remove(theCharacter);
	}
	
	/**
	 * This gets the shopkeeper if there is one.
	 * @return the Shopkeeper.
	 */
	public Shopkeeper getShopkeeper()
	{
		return myShop;
	}
	
	/** 
	 * This sets the shop keeper
	 * @param theShop the shopkeeper.
	 */
	public void setShopkeeper(final Shopkeeper theShop)
	{
		myShop = theShop;
	}
	
	/**
	 * This sets if a room was activated.
	 * @param theActivated boolean representing activation.
	 */
	public void setActivated(final boolean theActivated)
	{
		myIsActivated = theActivated;
	}
	
	/**
	 * This sets the monsters within a room to a new arraylist.
	 * @param theMonsters the new monsters arraylist.
	 */
	public void setMonsters(final List<Monster> theMonsters)
	{
		myMonstersInRoom = new ArrayList<Monster>(theMonsters);
	}
	
	/**
	 * This gets the monster arraylist.
	 * @return The array list of monsters not a copy..
	 */
	public List<Monster> getMonsters()
	{
		return new ArrayList<Monster>(myMonstersInRoom);
	}
	
	/**
	 * Checks if a room has combat.
	 * @return
	 */
	public boolean hasCombat()
	{
		return myMonstersInRoom.size() == 0;
	}
	
	/**
	 * This checks if the room is activated.
	 * @return
	 */
	public boolean isActivated()
	{
		return myIsActivated;
	}
	
	/**
	 * This gets the items types within a room..
	 * @return EnumSet of Item Types in room.
	 */
	public EnumSet<ItemType> getItems()
	{	
		return EnumSet.copyOf(myItems);
	}
	
	/**
	 * Sets the Visability of the room.
	 * @param theView boolean of the view. 
	 */
	public void setVisability(final boolean theView)
	{
		myIsVisable = theView;
	}
	
	/** 
	 * Checks if the Room is visable.
	 * @return boolean representing the room's visablility
	 */
	public boolean isVisable()
	{
		return myIsVisable;
	}
	
	/**
	 * This sets the tile location of the hero within room.
	 * @param theNewPoint the new point of the hero.
	 */
	public void setHeroTileLocation(final Point theNewPoint)
	{
		myHeroTileLocation = theNewPoint;
	}
	
	/**
	 * This gets the hero's tile location.
	 * @return the point representing the hero's tile location.
	 */
	public Point getHeroTileLocation()
	{
		return myHeroTileLocation;
	}
	
	/**
	 * This creates a string of the room.
	 * @return String representation of the room.
	 */
	public String toString()
	{
		String returnString = "";
		
		for(int row = 0; row < getTilesRows(); row++)
		{
			for(int col = 0; col < getTilesCols(); col++) // we will check for heros at UI
			{
				returnString += myTiles[row][col].getTileType().getChar();
			}
			returnString += "\n";
		}
		return returnString;
	}
}

