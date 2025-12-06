package model;

import java.awt.Point;
import java.util.EnumSet;

/**
 * This class represents the dungeon the player must traverse.
 */
public class Dungeon 
{
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
	 */
	public EnumSet<Direction> getTraversable()
	{
		return myMaze[(int)myHeroRoomLocation.getX()][(int)myHeroRoomLocation.getY()].getDirections();
	}
	
	public void stepHero(Direction theDir, Hero theHero)
	{
	    Room room = getCurrentRoom();

	    Point oldPos = getHeroTileLocation();
	    Point nextPos = theDir.translate(oldPos);
	    
	    if(!room.canMoveTo(nextPos))
	    {
	    	return;
	    }
	    
	    Tile tile = room.getTile(nextPos);
	    TileType type = tile.getTileType();
	    
	    if(type.isDoor())
	    {
	    	System.out.println("Reached Door");
	    	Direction doorDir = type.getDoorDirection();
	    	goThroughDoor(doorDir);
	    	return;
	    }
	    
	    System.out.println("Moved to:" + nextPos + " TYPE=" + type + " isDoor=" + type.isDoor());
	    setHeroTileLocation(nextPos);
	    room.activateTile(nextPos, theHero);
	}
	
	private void goThroughDoor(final Direction theDir)
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
	}
	
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
	
	public void setHeroTileLocation(final Point thePoint)
	{
		getCurrentRoom().setHeroTileLocation(thePoint);
	}
	
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
	
	public void setRoomDepth(final int theRow, final int theCol, final int theDepth)
	{
		myMaze[theRow][theCol].setDepth(theDepth);
	}
	
	public void setStartLocation(final int theRow, final int theCol)
	{
		myStartLocation.setLocation(theRow, theCol);
		setHeroLocation(theRow, theCol);
	}
	
	public Point getStartPoint()
	{
		return new Point(myStartLocation);
	}
	
	/**
	 * This gets the number of rows.
	 * @return The number of rows
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
		
		System.out.println("\n" + dungeon);
		return dungeon;
	}
}
