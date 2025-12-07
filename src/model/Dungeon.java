package model;

import java.awt.Point;
import java.util.EnumSet;

import controller.RoomController;
import view.GameView;

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
	 *  This array holds the Hero's location.
	 */
	private Point myHeroLocation;
	
	/**
	 * This is the starting point used in some logic.
	 */
	private Point myStartLocation;
	
	private Hero myHero;
	
	/**
	 *  This is the basic class constructor.
	 */
	
	private GameView myView;

	public Dungeon(final Hero theHero, final int theDifficulty)
	{
		myHero = theHero;
		
		myRows = theDifficulty + 4;
		myCols = theDifficulty + 4;
		myMaze = new Room[myRows][myCols];
		myHeroLocation = new Point();
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
		return myMaze[(int)myHeroLocation.getX()][(int)myHeroLocation.getY()].getDirections();
	}
	
	/**
	 * This method just changes the hero location.
	 * @param theRows The new row the Hero will be.
	 * @param theCols The new col the Hero will be.
	 */
	private void setHeroLocation(final int theRows, final int theCols)
	{
		myHeroLocation.setLocation(theRows, theCols);
	}
	
	/**
	 * This method moves the hero if possible 
	 * @param theDirection The direction to move.
	 * @return Boolean representing if the move was successful.
	 */
	public boolean move(final Direction theDirection)
	{
		boolean moved = true;
		int x = (int) myHeroLocation.getX();
		int y = (int) myHeroLocation.getY();
		
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
	    // old location
	    int oldX = (int) myHeroLocation.getX();
	    int oldY = (int) myHeroLocation.getY();

	    // exit old room
	    myMaze[oldX][oldY].exit(theDirection);

	    // move hero
	    setHeroLocation(theRow, theCol);

	    // new room
	    Room newRoom = myMaze[theRow][theCol];

	    // enter the new room (pits, pickups)
	    newRoom.enter(myHero);

	    // activate encounter
	    String result =
	        RoomController.activateEncounter(myHero, newRoom, myView);

	    // hero died in battle
	    if (result.equals("HERO_LOSE")) {
	        myView.gameOver();
	        return;
	    }

	    // update GUI (HP bar, minimap, room state)
	    if (myView != null) {
	        myView.showHeroStats(myHero);
	        myView.showDungeon(this);
	    }
	}

	
	public void setRoomDepth(final int theRow, final int theCol, final int theDepth)
	{
		myMaze[theRow][theCol].setDepth(theDepth);
	}
	
	public void setStartLocation(final int theRow, final int theCol)
	{
		myStartLocation.setLocation(theRow, theCol);
	}
	
	public Point getStartPoint()
	{
		return new Point(myStartLocation);
	}
	
	public void revealSurroundingRooms() {
	    int row = myHeroRoomLocation.x;
	    int col = myHeroRoomLocation.y;

	    // Check all surrounding room positions
	    for (int r = row - 1; r <= row + 1; r++) {
	        for (int c = col - 1; c <= col + 1; c++) {

	            // Skip out-of-bounds
	            if (r < 0 || r >= myRows || c < 0 || c >= myCols) continue;

	            Room room = myMaze[r][c];
	            room.setVisability(true);  // Skyler already uses this flag
	        }
	    }
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
		int x = (int) myHeroLocation.getX();
		int y = (int) myHeroLocation.getY();
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
	
	public void setView(final GameView theView) {
	    myView = theView;
	}
	
	public Point getHeroLocation() {
	    return new Point(myHeroLocation);
	}

	public String debugPrintDungeon() {
	    StringBuilder sb = new StringBuilder();

	    for (int r = 0; r < myRows; r++) {
	        for (int c = 0; c < myCols; c++) {
	            Room room = myMaze[r][c];
	            sb.append(room.getRoomChar()).append(' ');
	        }
	        sb.append('\n');
	    }

	    return sb.toString();
	}


}