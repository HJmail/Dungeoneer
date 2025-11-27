package Contoller;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Model.Direction;
import Model.Dungeon;
import Model.DungeonGenerator;
import Model.RoomType;
import Model.Shopkeeper;
import Model.Hero;
import Model.Item;
import Model.Priestess;
import Model.Room;
import Model.Thief;
import Model.Warrior;
import View.ConsoleView;
import View.GameView;

/**
 *  This class is the main logic holding class for the Dungeoneer Game
 *  @author Skyler Z Broussard
 *  @version 0.0.1  10/20/2025
 */
public class DungeonAdventure 
{	
	private static final int PIT_DMG = 4;
	
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
	private static GameView myView;
	
	/**
	 * Used for temporary random like seed generation
	 */
	private static Random myRandom;
	
	/**
	 *  Actual seed for the run.
	 */
	private static long mySeed;
	
	private static RoomController myRoomController;
	
	
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
		
		// User Input Scanner and results... (only used when we do console.) will change.
		myUserInput = new Scanner(System.in);
		
		// User inputs
		myDifficulty = promptDifficulty();
		myHero = promptHero();
		mySeed = promptSeed();
		
		myDungeon  = DungeonGenerator.generate(mySeed, myDifficulty, myHero);
		
		// other controllers
		myView = new ConsoleView();
		CombatController.setView(myView); // setting combat view
		myRoomController = new RoomController(myHero, myDungeon, myView);
		
	}
	
	/**
	 * This method plays the game when called, until myGameStatus becomes false.
	 */
	private static void play()
	{
		while(myGameStatus) // currently off
		{
			promptMove(); // user input for move
		}
		myView.showMessage("Game Halted.");
	}
	
	/**
	 * This method asks the user what difficulty they want.
	 * @return Integer representing the difficulty.
	 */
	private static int promptDifficulty()
	{
		myView.showMessage("What Difficulty do you want?  Please choose 1-9: ");
		return myUserInput.nextInt();
	}
	
	/**
	 * This method asks the user what class they want to play.
	 * @return The newly made hero that the user willS play.
	 */
	private static Hero promptHero()
	{
		myView.showMessage("What Class do you want to play? (P)riestess, (T)hief, or (W)arrior: ");
		String response = myUserInput.next();
		String heroName = promptHeroName();
		Hero userHero = null;
		if(response.equals("W"))
		{
			userHero = new Warrior(heroName);
		}
		else if(response.equals("T"))
		{
			userHero = new Thief(heroName);
		}
		else if(response.equals("P"))
		{
			userHero = new Priestess(heroName);
		}
		return userHero;
	}

	private static String promptHeroName()
	{
		myView.showMessage("What is you Hero's Name: ");
		return myUserInput.next();
	}
	
	private static long promptSeed()
	{
		myView.showMessage("What Seed do you want?");
		return myUserInput.nextInt();
	}
	
	private static void promptMove()
	{
		boolean goodResponse = false;
		
		while(!goodResponse) // keep prompting until good input.
		{
			myView.showDungeon(myDungeon);
			myView.showMessage(myDungeon.getTraversable().toString());
			Direction chosenDirection = myView.askDirection();
			goodResponse = myDungeon.move(chosenDirection);
		}
		activateRoom();
	}
	
	private static void activateRoom()
	{
		Room cRoom = myDungeon.getCurrentRoom();
		RoomType rt = cRoom.getRoomType();
		boolean activated = cRoom.isActivated();
		
		if(rt == RoomType.SHOP) // activated not needed (always available)
		{
			myRoomController.activateShop();
		}
		else if(rt == RoomType.PIT)
		{
			myRoomController.activatePit();
		}
		else if(rt == RoomType.PILLAR && !activated)
		{
			myRoomController.activatePillar();
		}
		else if(rt == RoomType.TREASURE && !activated)
		{
			myRoomController.activateTreasure();
		}
		else if(rt == RoomType.ENCOUNTER && !activated)
		{
			myRoomController.activateEncounter();
		}
		// room is nothing.
	}

}
