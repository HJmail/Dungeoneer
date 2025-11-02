package Contoller;

import java.util.Scanner;

import Model.Dungeon;
import Model.Hero;

/**
 *  This class is the main logic holding class for the Dungeoneer Game
 *  @author Skyler Z Broussard
 *  @version 0.0.1  10/20/2025
 */
public class DungeonAdventure 
{	
	/**
	 * This field is the main character 
	 */
	static Hero myHero;
	
	/**
	 * This field is a scanner for user input.
	 */
	static Scanner myUserInput;
	
	/**
	 * This boolean represents if a game is running.
	 */
	static boolean myGameStatus;
	
	/**
	 * This field is the dungeon of the current game.
	 */
	static Dungeon myDungeon;
	
	
	/**
	 * This is the method with the main workflow.
	 * @param theArgs
	 */
	public static void main(final String theArgs[])
	{
		startGame();
		
		play();
		
	}
	
	/**
	 * This method starts creating the required fields for all other functionalities.
	 */
	private static void startGame() 
	{
		myUserInput = new Scanner(System.in);
		myHero = new Hero();
		myDungeon  = new Dungeon(myHero);
		myGameStatus = false;
	}
	
	/**
	 * This method plays the game when called, until myGameStatus becomes false.
	 */
	private static void play()
	{
		while(myGameStatus)
		{
			
		}
	}
	
	/**
	 * This method has all of the logic for moving rooms.
	 * @param theInput
	 */
	private static void processMovement(final String theInput)
	{
		
	}
	
	public static Hero getHero()
	{
		return myHero;
	}
}
