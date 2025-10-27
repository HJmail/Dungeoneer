package Model;

import java.util.List;

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
	 * This field is the entrance to the dungeon
	 */
	Room myEntrance;
	
	/**
	 * This field is the exit to the dungeon
	 */
	Room myExit;
	
	/**
	 * This field is a list of my pillars
	 */
	List<Pillar> myPillars;
	
	/**
	 * This field is a list of monsters
	 */
	List<Monster> myMonsters;
	
	/**
	 * This field is the number of rows
	 */
	int myNumRows;
	
	/**
	 * This field is the number of cols
	 */
	int myNumCols;
	
	/**
	 *  This is the basic class constructor
	 */
	public Dungeon(final Hero theHero)
	{
		
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
