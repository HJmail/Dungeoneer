package Model;

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
	Room[][] myMaze; 
	
	/**
	 * This is the hero that gets passed through.
	 */
	Hero myHero;
	
	/**
	 * This field is the number of rows.
	 */
	int myRows;
	
	/**
	 * This field is the number of cols.
	 */
	int myCols;
	
	/**
	 * This is the given seed randomly generated for used for creating the map.
	 */
	int mySeed;
	
	/**
	 *  This is the basic class constructor.
	 */
	public Dungeon(final Hero theHero, final int theRows, final int theCols)
	{
		myHero = theHero;
		myRows = theRows;
		myCols = theCols;
		
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
						
			}
		}
	}
	
	/**
	 * Checks if a given path is able to go into.
	 */
	private boolean isTraversable()
	{
		
	}
	
	/**
	 * This places the entrance and exit rooms
	 */
	private void placeEnteraceAndExit()
	{
		
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
	
	/**
	 * This prints the dungeon to the console.
	 */
	public String toString()
	{
		
	}
	
}
