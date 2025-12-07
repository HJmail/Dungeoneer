package model;

import java.awt.Point;
import java.util.EnumSet;
import java.io.Serializable;

/**
 * This class represents the dungeon the player must traverse.
 * @author Skyler Z. Broussard
 * @version 12/6/2025
 */
public class Dungeon implements Serializable
{
	/**
	 * This is the serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is a 2d array that holds a grid of rooms that represents a Maze
	 */
	private Room[][] myMaze; 
	
	/**
	 * This field is the number of rows.
	 */
	private int myRows;
	
	/**
	 * This field is the number of cols.
	 */
	private int myCols;
	
	/**
	 *  This holds the Hero's room location.
	 */
	private Point myHeroRoomLocation;
	
	/**
	 * This is the starting point used in some logic.
	 */
	private Point myStartLocation;
	
	/**
	 *  This is the basic class constructor.
	 */
	public Dungeon(final int theDifficulty)
	{
		myRows = theDifficulty + 4;
		myCols = theDifficulty + 4;
		myMaze = new Room[myRows][myCols];
		myHeroRoomLocation = new Point();
		myStartLocation = new Point();
		
		generateDimensions();
	}
	
	/**
	 * This method generates a random dungeon
	 */
	private void generateDimensions()
	{
		for(int i = 0; i < myRows; i++) // outer is row
		{
			for(int j = 0; j < myCols; j++) // inner is col
			{
				Room newRoom = new Room();
				
				myMaze[i][j] = newRoom;
			}
		}
	}
	
	/**
	 * Checks if a given path is able to go into.
	 * @return Gets a EnumSet representing the avaliable directions.
	 */
	public EnumSet<Direction> getTraversable()
	{
		return myMaze[(int)myHeroRoomLocation.getX()][(int)myHeroRoomLocation.getY()].getDirections();
	}
	
	/**
	 * This processes the direction a hero will move.
	 * @param theDir the Direction the hero wants to move.
	 * @param theHero This is the Hero
	 * @return The new Room's RoomType for logic.
	 */
	public RoomType stepHero(Direction theDir, Hero theHero)
	{
	    Room room = getCurrentRoom();

	    Point oldPos = getHeroTileLocation();
	    Point nextPos = theDir.translate(oldPos);
	    
	    if(!room.canMoveTo(nextPos))
	    {
	    	return room.getRoomType();
	    }
	    
	    Tile tile = room.getTile(nextPos);
	    DungeonTile type = tile.getTileType();
	    
	    if(type.isDoor())
	    {
	    	Direction doorDir = type.getDoorDirection();
	    	return goThroughDoor(doorDir);
	    }
	    setHeroTileLocation(nextPos);
	    room.activateTile(nextPos, theHero);
	    return room.getRoomType();
	}
	
	/**
	 * This method does logic for Door traversal for room transfers.
	 * @param theDir The Direction that we just traveled
	 * @return the RoomType of the new Room for logic.
	 */
	private RoomType goThroughDoor(final Direction theDir)
	{
	    int roomRow = myHeroRoomLocation.x;
	    int roomCol = myHeroRoomLocation.y;
	    
	    roomRow += theDir.dy();  
	    roomCol += theDir.dx();
	    
	    myHeroRoomLocation = new Point(roomRow, roomCol);
	    Room nRoom = getCurrentRoom();
	    Direction entry = theDir.opposite();
	    Point entryPos = findDoor(nRoom, entry);
	    
	    nRoom.setHeroTileLocation(entryPos);
	    RoomType nrt = nRoom.getRoomType();
	    
	    return nrt;
	 }
	
	/**
	 * Finds Doors within the the room.
	 * @param theRoom the given room.
	 * @param theDir the given Direction
	 * @return Point that represents where hero will be.
	 */
	private Point findDoor(final Room theRoom, final Direction theDir)
	{
		int rows = theRoom.getTilesRows();
	    int cols = theRoom.getTilesCols();

	    int midRow = rows / 2;
	    int midCol = cols / 2;
		return switch (theDir) 
		{
		        case NORTH -> new Point(0, midCol);      // top wall (row 0, middle col)
		        case SOUTH -> new Point(rows - 1, midCol);      // bottom wall
		        case WEST  -> new Point(midRow, 0);           // left wall
		        case EAST  -> new Point(midRow, cols - 1);    // right wall
		};
	}
	
	/**
	 * This method just changes the hero location for rooms.
	 * @param theRows The new row the Hero will be.
	 * @param theCols The new col the Hero will be.
	 */
	private void setHeroLocation(final int theRows, final int theCols)
	{
		myHeroRoomLocation.setLocation(theRows, theCols);
	}
	
	/**
	 * This sets the Hero's Tile Location
	 * @param thePoint Point representing the tile.
	 */
	public void setHeroTileLocation(final Point thePoint)
	{
		getCurrentRoom().setHeroTileLocation(thePoint);
	}
	
	/**
	 * This gets the Hero tile location.
	 * @return A Point representing the Hero Tile Location.
	 */
	public Point getHeroTileLocation()
	{
		return getCurrentRoom().getHeroTileLocation();
	}
	
	/**
	 * This method moves the hero if possible 
	 * @param theDirection The direction to move.
	 * @return Boolean representing if the move was successful.
	 */
	public boolean move(final Direction theDirection)
	{
		boolean moved = true;
		int x = (int) myHeroRoomLocation.getX();
		int y = (int) myHeroRoomLocation.getY();
		
		if(getTraversable().contains(theDirection) && theDirection == Direction.NORTH)
		{
			moveHero(x - 1, y, Direction.NORTH);
		}
		else if(getTraversable().contains(theDirection) && theDirection == Direction.EAST)
		{
			moveHero(x, y + 1, Direction.EAST);
		}
		else if(getTraversable().contains(theDirection) && theDirection == Direction.SOUTH)
		{
			moveHero(x + 1, y, Direction.SOUTH);
		}
		else if(getTraversable().contains(theDirection) && theDirection == Direction.WEST)
		{
			moveHero(x, y - 1, Direction.WEST);
		}
		else
		{
			moved = false;
		}
		return moved;
	}
	
	/**
	 * The Logic for moving a hero to a new room and starting new room logic.
	 * @param theRow Row of new room.
	 * @param theCol Col of new room.
	 * @param theDirection the Direction so we can maintain good symbols for discretions.
	 */
	private void moveHero(final int theRow, final int theCol, final Direction theDirection)
	{ 
		// getting current row and col
		int x = (int) myHeroRoomLocation.getX();
		int y = (int) myHeroRoomLocation.getY();
		
		// exiting old room.
		Room oldRoom = myMaze[x][y];
		oldRoom.exit(theDirection);
		oldRoom.setVisability(true);
		oldRoom.setActivated(true);
		
		// joining new room
		setHeroLocation(theRow, theCol);
	}
	
	/**
	 * This sets the room depth for a given room.
	 * @param theRow the Row of the Room.
	 * @param theCol the Col of the Room.
	 * @param theDepth the Given depth for the room.
	 */
	public void setRoomDepth(final int theRow, final int theCol, final int theDepth)
	{
		myMaze[theRow][theCol].setDepth(theDepth);
	}
	
	/**
	 * This method sets the starting room location.
	 * @param theRow the Row.
	 * @param theCol the Col.
	 */
	public void setStartLocation(final int theRow, final int theCol)
	{
		myStartLocation.setLocation(theRow, theCol);
		setHeroLocation(theRow, theCol);
	}
	
	/**
	 * This gets the start Point.
	 * @return A point that represents the start
	 */
	public Point getStartPoint()
	{
		return new Point(myStartLocation);
	}
	
	/**
	 * This gets the number of rows.
	 * @return The number of rows.
	 */
	public int getRows()
	{
		return myRows;
	}
	
	/**
	 * This gets the number of columns.
	 * @return The number of columns.
	 */
	public int getCols()
	{
		return myCols;
	}
	
	/**
	 * Gets the current room that the hero is in.
	 * @return returns the hero's room.
	 */
	public Room getCurrentRoom()
	{
		int x = (int) myHeroRoomLocation.getX();
		int y = (int) myHeroRoomLocation.getY();
		return getRoom(x, y);
	}
	
	/**
	 * This gets the current room the player is in.
	 * @return The current room the player is in.
	 */
	public Room getRoom(final int theRow, final int theCol)
	{
		return myMaze[theRow][theCol];
	}
	
	/**
	 * This turns the Dungeon into the a string
	 * @returns	String representing the Dungeon.
	 */
	public String toString()
	{
		String dungeon = "";
		
		for(int i = 0; i < myRows; i++)
		{
			for(int j = 0; j < myCols; j++)
			{
				dungeon += getRoom(i, j).getRoomChar();
				dungeon += ' ';
			}
			dungeon += "\n";
		}
		return dungeon;
	}
}
