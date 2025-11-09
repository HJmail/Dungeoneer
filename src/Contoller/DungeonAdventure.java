package Contoller;

import java.util.Scanner;

import Model.Direction;
import Model.Dungeon;
import Model.Hero;
import Model.Priestess;
import Model.Thief;
import Model.Warrior;

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
	 * This field represents the difficulty
	 */
	static int myDifficulty;
	
	/**
	 * This is the method with the main workflow.
	 * @param theArgs 
	 */
	public static void main(final String theArgs[])
	{
		setupGame();
		
		play();
		
	}
	
	/**
	 * This method starts creating the required fields for all other functionalities.
	 */
	private static void setupGame() 
	{
		myGameStatus = false; // change when wanting to run game.
		myUserInput = new Scanner(System.in);
		myDifficulty = promptDifficulty();
		myHero = promptHero();
		myDungeon  = new Dungeon(myHero, myDifficulty);
		System.out.println(myDungeon);
	}
	
	/**
	 * This method plays the game when called, until myGameStatus becomes false.
	 */
	private static void play()
	{
		while(myGameStatus)
		{
			promptMove();
		}
		System.out.println("Game has halted...");
	}
	
	/**
	 * This method asks the user what difficulty they want.
	 * @return Integer representing the difficulty.
	 */
	private static int promptDifficulty()
	{
		System.out.print("What Difficulty do you want?  Please choose 1-9: ");
		return myUserInput.nextInt();
	}
	
	/**
	 * This method asks the user what class they want to play.
	 * @return The newly made hero that the user will play.
	 */
	private static Hero promptHero()
	{
		System.out.print("What Class do you want to play? (P)riestess, (T)hief, or (W)arrior: ");
		String response = myUserInput.next();
		String heroName = "Test";
		Hero userHero = null;
		
		if(response == "W")
		{
			userHero = new Warrior(heroName);
		}
		else if(response == "T")
		{
			userHero = new Thief(heroName);
		}
		else if(response == "P")
		{
			userHero = new Priestess(heroName);
		}
		System.out.println(heroName + " has been created!");
		return userHero;
	}
	
	private static void promptMove()
	{
		System.out.print("What direction do you want to move? (N)orth, (E)ast, (S)outh, and (W)est: ");
		boolean goodResponse = false;
		
		while(!goodResponse) // keep prompting until good input.
		{
			String chosenDirection = myUserInput.next(); 
			goodResponse = processMovement(chosenDirection);
		}
	}
	
	/**
	 * This method has all of the logic for moving rooms.
	 * @param theInput 
	 */
	private static boolean processMovement(final String theInput)
	{
		boolean wasSuccessful = true;
		if(theInput == "N")
		{
			if(myDungeon.getTraversable().contains(Direction.NORTH))
			{
				
			}
		}
		else if(theInput == "E")
		{
			if(myDungeon.getTraversable().contains(Direction.EAST))
			{
				
			}
		}
		else if(theInput == "S")
		{
			if(myDungeon.getTraversable().contains(Direction.SOUTH))
			{
				
			}
		}
		else if(theInput == "W")
		{
			if(myDungeon.getTraversable().contains(Direction.WEST))
			{
				
			}
		}
		else
		{
			System.out.println("That input was erroneous, please try again.");
			wasSuccessful = false;
		}
		return wasSuccessful;
	}
}
