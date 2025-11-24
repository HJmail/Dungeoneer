package Contoller;

import java.util.Random;
import java.util.Scanner;

import Model.Dungeon;
import Model.DungeonGenerator;
import Model.RoomType;
import Model.Shopkeeper;
import Model.Hero;
import Model.Priestess;
import Model.Room;
import Model.Thief;
import Model.Warrior;
import View.ConsoleView;

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
	private static Hero myHero;
	
	/**
	 * This field is a scanner for user input.
	 */
	private static Scanner myUserInput;
	
	/**
	 * This boolean represents if a game is running.
	 */
	private static boolean myGameStatus;
	
	/**
	 * This field is the dungeon of the current game.
	 */
	private static Dungeon myDungeon;
	
	/**
	 * This field represents the difficulty
	 */
	private static int myDifficulty;
	
	/**
	 * This is the Console view.
	 */
	private static ConsoleView myConsoleView;
	
	/**
	 * Used for temporary random like seed generation
	 */
	private static Random myRandom;
	
	/**
	 *  Actual seed for the run.
	 */
	private static long mySeed;
	
	//private static CombatController myCombat;
	
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
		myGameStatus = true;
		
		// setting up type of View
		myConsoleView = new ConsoleView(); // will switch to GUI when created...
		
		// Combat Controller
		//myCombat = new CombatController();
		CombatController.setView(myConsoleView);
		
		// User Input Scanner and results... (only used when we do console.) will change.
		myUserInput = new Scanner(System.in);
		
		// User inputs
		myDifficulty = promptDifficulty();
		myHero = promptHero();
		mySeed = promptSeed();
		
		myDungeon  = DungeonGenerator.generate(mySeed, myDifficulty, myHero);
	}
	
	/**
	 * This method plays the game when called, until myGameStatus becomes false.
	 */
	private static void play()
	{
		while(myGameStatus) // currently off
		{
			promptMove(); // user input for move
			
			Room room = myDungeon.getCurrentRoom(); 
			
			if(!room.isActivated()) // check if new room is activated
			{
				// all Room Logic
				if(room.hasCombat())
				{
					CombatController.battleMultiple(myHero, room.getMonsters());
				}
				else if(room.getRoomType() == RoomType.PIT) // if pit then we will take damage
				{
					myHero.setHitPoints(myHero.getHitPoints() - 4); // -4 hit points
				}
				else if(room.getRoomType() == RoomType.SHOP) // shop keeper set up and prompt
				{
					room.setShopkeeper(new Shopkeeper());
					promptShop();
				}
				room.setActivated(true);
			}
		}
		myConsoleView.showMessage("Game Halted.");
	}
	
	private static void promptShop(final Room theRoom)
	{
		Shopkeeper shop = theRoom.getShopkeeper();
		myConsoleView.showMessage(shop.displayItems());
	}
	
	/**
	 * This method asks the user what difficulty they want.
	 * @return Integer representing the difficulty.
	 */
	private static int promptDifficulty()
	{
		myConsoleView.showMessage("What Difficulty do you want?  Please choose 1-9: ");
		return myUserInput.nextInt();
	}
	
	/**
	 * This method asks the user what class they want to play.
	 * @return The newly made hero that the user will play.
	 */
	private static Hero promptHero()
	{
		myConsoleView.showMessage("What Class do you want to play? (P)riestess, (T)hief, or (W)arrior: ");
		String response = myUserInput.next();
		String heroName = promptHeroName();
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
		myConsoleView.showMessage(heroName + " has been created!");
		return userHero;
	}

	private static String promptHeroName()
	{
		myConsoleView.showMessage("What is you Hero's Name: ");
		return myUserInput.next();
	}
	
	private static long promptSeed()
	{
		myConsoleView.showMessage("What Seed do you want?");
		return myUserInput.nextInt();
	}
	
	private static void promptMove()
	{
		boolean goodResponse = false;
		
		while(!goodResponse) // keep prompting until good input.
		{
			myConsoleView.showDungeon(myDungeon);
			
			myConsoleView.showMessage(myDungeon.getCurrentRoom().getDirections().toString());
			
			myConsoleView.showMessage("What direction do you want to move? (N)orth, (E)ast, (S)outh, and (W)est: ");
			
			String chosenDirection = myUserInput.next(); 
			goodResponse = processMovement(chosenDirection);
		}
	}
	
	/**
	 * This method has all of the logic for moving rooms.
	 * @param theInput This is the user input.
	 */
	private static boolean processMovement(final String theInput)
	{
		boolean wasSuccessful = false;
		int move = myDungeon.checkMove(theInput.toUpperCase());
		if(move == 0)
		{
			wasSuccessful = true;
			if(myDungeon.getCurrentRoom().getRoomType() == RoomType.EXIT &&
					myHero.getInventory().canExit())
			{
				myConsoleView.showMessage("You have reached the exit.");
				myGameStatus = false; // ending while loop
			}
		}
		else if(move == 1)
		{
			myConsoleView.showMessage("That direction does not work! Please select another.");
			
		}
		else
		{
			myConsoleView.showMessage("That input was erroneous, please try again.");
		}
		return wasSuccessful;
	}
}
