package Model;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

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
	 * This is the given seed randomly generated for used for creating the map.
	 */
	private int mySeed;
	
	/**
	 *  This array holds the Hero's location.
	 */
	private int[] myHeroLocation;
	
	/**
	 *  This is the basic class constructor.
	 */
	public Dungeon(final Hero theHero, final int theDifficulty)
	{
		myHero = theHero;
		myRows = theDifficulty + 4;
		myCols = theDifficulty + 4;
		myMaze = new Room [myRows][myCols];
		myHeroLocation = new int[2];
		
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
		// All of the special room creations happen here
		placeEntranceAndExit();
		
	}
	
	/**
	 * Checks if a given path is able to go into.
	 */
	public EnumSet<Direction> getTraversable()
	{
		return myMaze[myHeroLocation[0]][myHeroLocation[1]].getDirections();
	}
	
	/**
	 * This places the entrance and exit rooms
	 */
	private void placeEntranceAndExit()
	{
		// Need to make this dynamic and randomly generated.
		
		EnumSet<Direction> entryDirection = EnumSet.of(Direction.EAST, Direction.SOUTH) ;
		EnumSet<Direction> exitDircretion = EnumSet.of(Direction.NORTH, Direction.WEST);
		
		//entry
		myMaze[0][0].setDirections(entryDirection);
		myMaze[0][0].setRoomChar('e');
		setHeroLocation(0, 0);
		
		// exit
		myMaze[myRows - 1][myCols - 1].setDirections(exitDircretion);
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
	 * This moves the hero and handles errors if needed.
	 * @param theDirection This is the direction the player wants to go.
	 */
	public void moveHero(final String theDirection)
	{
		
	}
	
	private void setHeroLocation(final int theRows, final int theCols)
	{
		myHeroLocation[0] = theRows;
		myHeroLocation[1] = theCols;
	}
	
	/**
	 * This prints the dungeon to the console.
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
