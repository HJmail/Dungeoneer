 package Model;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import Model.EventType;

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
	 * This is the hero that gets passed through.
	 */
	private Hero myHero;
	
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
	private int[] myHeroLocation;
	
	private int[] myStartLocation;
	
	
	/**
	 *  This is the basic class constructor.
	 */
	public Dungeon(final Hero theHero, final int theDifficulty)
	{
		myHero = theHero;
		myRows = theDifficulty + 4;
		myCols = theDifficulty + 4;
		myMaze = new Room[myRows][myCols];
		myHeroLocation = new int[2];
		myStartLocation = new int[2];
		
		generateDungeon();
	}
	
	/**
	 * This class constructor will hopefully load data from save files (future)
	 */
	//public Dungeon()
	//{
	//	
	//}
	
	/**
	 * This method generates a random dungeon
	 */
	private void generateDungeon()
	{
		for(int i = 0; i < myRows; i++) // outer is row
		{
			for(int j = 0; j < myCols; j++) // inner is col
			{
				Room newRoom = new Room();
				
				myMaze[i][j] = newRoom;
			}
		}
		// Giving the Room their Directions
		//setAllRoomDirections();
		
		// All of the special room creations happen here
		//placeEntranceAndExit();
	}
	
	/**
	 * Checks if a given path is able to go into.
	 */
	private EnumSet<Direction> getTraversable()
	{
		return myMaze[myHeroLocation[0]][myHeroLocation[1]].getDirections();
	}
	
	/**
	 * This sets all the created rooms directions.
	 */
	private void setAllRoomDirections()
	{
		// IMPORTANT THIS DOES NOT CHANGE BASED ON GIVEN ROOMS OR WALLS
		// SIMPLY REMOVES DIRECTIONS BASED ON MAP EDGES
		for(int i = 0; i < myMaze.length; i++) // rows
		{
			for(int j = 0; j < myMaze[0].length; j++) // cols
			{
				EnumSet<Direction> directions = EnumSet.of(Direction.NORTH, Direction.EAST,
															Direction.SOUTH, Direction.WEST);
				if(i == 0) // check for NORTH edges
				{
					directions.remove(Direction.NORTH);
				}
				if(i == myMaze.length - 1) // check for SOUTH edges
				{
					directions.remove(Direction.SOUTH);
				}
				if(j == 0) // check for WEST edge
				{
					directions.remove(Direction.WEST);
				}
				if(j == myMaze[0].length - 1) // checks for EAST edge
				{
					directions.remove(Direction.EAST);
				}
				myMaze[i][j].setDirections(directions);
			}
		}
	}
	
	/**
	 * This places the entrance and exit rooms
	 */
	private void placeEntranceAndExit()
	{
		// Need to make this dynamic and randomly generated.

		myMaze[0][0].setRoomChar('e');
		setHeroLocation(0, 0);
		
		myMaze[myRows - 1][myCols - 1].setRoomChar('E');
	}
	
	/**
	 *  This places the pillars randomly throughout the map 
	 */
	private void placePillars()
	{
		
	}
	
	/**
	 *  THis places the monsters randomly throughout the map
	 */
	private void placeMonsters()
	{
		
	}
	
	/**
	 * This method just changes the hero location.
	 * @param theRows The new row the Hero will be.
	 * @param theCols The new col the Hero will be.
	 */
	private void setHeroLocation(final int theRows, final int theCols)
	{
		myHeroLocation[0] = theRows;
		myHeroLocation[1] = theCols;
	}
	
	
	/**
	 * This is a helper method that makes sure a given input string is within the array of string
	 * @param theInput
	 * @param theArray
	 * @return
	 */
	private boolean checkStringArray(final String theInput, final String[] theArray)
	{
		boolean isThere = false;
		for(String s: theArray)
		{
			if(s.equals(theInput.toUpperCase()))
			{
				isThere = true;
				break;
			}
		}
		return isThere;
	}
	
	
	private boolean move(final String theDirection)
	{
		boolean moved = true;
		
		if(theDirection.equals("N") && getTraversable().contains(Direction.NORTH))
		{
			moveHero(myHeroLocation[0] - 1, myHeroLocation[1]);
		}
		else if(theDirection.equals("E") && getTraversable().contains(Direction.EAST))
		{
			moveHero(myHeroLocation[0], myHeroLocation[1] + 1);
		}
		else if(theDirection.equals("S") && getTraversable().contains(Direction.SOUTH))
		{
			moveHero(myHeroLocation[0] + 1, myHeroLocation[1]);
		}
		else if(theDirection.equals("W") && getTraversable().contains(Direction.WEST))
		{
			moveHero(myHeroLocation[0], myHeroLocation[1] - 1);
		}
		else
		{
			moved = false;
		}
		return moved;
	}
	
	private void moveHero(final int theRow, final int theCol)
	{
		myMaze[myHeroLocation[0]][myHeroLocation[1]].exit();
		setHeroLocation(theRow, theCol);
		myMaze[theRow][theCol].enter(myHero);
	}
	
	
	/**
	 * Sets the given room to  the 
	 * @param theRow
	 * @param theCol
	 * @param theEvent
	 */
	public void setRoomType(final int theRow, final int theCol, final EventType theEvent)
	{
		moveHero(theRow, theCol);
		myMaze[theRow][theCol].setEvent(theEvent);
	}
	
	public int checkMove(final String theInput)
	{
		int returnInt = 0; // 0 is success, 1 direction is bad, 2 is bad input
		String[] goodInput = {"N", "E", "S", "W"};
		
		if(checkStringArray(theInput, goodInput) && !move(theInput))
		{
			returnInt = 1; // direction doesn't work
		}
		else
		{
			returnInt = 2; // bad input
		}
		return returnInt;
	}
	
	public void setStartLocation(final int theRow, final int theCol)
	{
		myStartLocation[0] = theRow;
		myStartLocation[1] = theCol;
	}
	
	public int[] getStartLocation()
	{
		return myStartLocation;
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
		return getRoom(myHeroLocation[0],myHeroLocation[1]);
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
	 * This gets a string to represent the dungeon.
	 */
	public String toString()
	{
		String returnString = "";
		for(int i = 0; i < myRows; i++)
		{
			for(int j = 0; j < myCols; j++)
			{
				returnString += myMaze[i][j].getRoomChar();
			}
			returnString += "\n";
		}
		return returnString;
	}
	
}
