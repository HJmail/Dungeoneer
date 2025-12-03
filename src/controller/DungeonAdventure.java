package controller;

import java.util.Random;

import model.Direction;
import model.Dungeon;
import model.DungeonGenerator;
import model.GameConfig;
import model.Room;
import model.RoomType;
import view.GameView;
import view.GuiView;

/**
 *  This class is the main logic holding class for the Dungeoneer Game
 *  @author Skyler Z Broussard
 *  @version 0.0.1  10/20/2025
 */
public class DungeonAdventure 
{	
	/**
	 * This boolean represents if a game is running.
	 */
	private static boolean myGameStatus;
	
	/**
	 * This field is the dungeon of the current game.
	 */
	private static Dungeon myDungeon;
	
	/**
	 * This is the Console view.
	 */
	private static GameView myView;
	
	private static GameConfig myGameConfig;
	
	private static RoomController myRoomController;
	
	private static Random myRandom;
	
	
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
		
		// Setting up View ... can be Console based or GUI based.
		//myView = new ConsoleView();
		myView = new GuiView();
		
		// User inputs
		myGameConfig = myView.askGameConfig();
		
		myRandom = new Random(myGameConfig.getSeed());
		myDungeon  = DungeonGenerator.generate(myRandom,
												myGameConfig.getDifficulty(),
												myGameConfig.getHero());
		// other controllers 
		CombatController.setView(myView);
		myRoomController = new RoomController(myGameConfig.getHero(),
											  myDungeon,
											  myView,
											  myRandom);	
	}
	
	/**
	 * This method plays the game when called, until myGameStatus becomes false.
	 */
	private static void play()
	{
		while(myGameStatus)
		{
			promptInvetory();
			promptMove(); // user input for move
			checkHitPoints();
		}
		myView.showMessage("Game Halted.");
	}
	
	private static void promptInvetory()
	{
		myView.askInventory(myGameConfig.getHero().getInventory());
	}
	
	private static void promptMove()
	{
		boolean goodResponse = false;
		
		while(!goodResponse) // keep prompting until good input.
		{
			myView.showDungeon(myDungeon);
			
			myView.showMessage(myDungeon.getTraversable().toString()); // THIS IS FOR TESTING ONLY
			
			Direction chosenDirection = myView.askDirection();
			goodResponse = myDungeon.move(chosenDirection);
		}
		// Room is updated need to activate it.
		activateRoom();
	}
	
	private static void checkHitPoints()
	{
		if(!myGameConfig.getHero().isAlive())
		{
			myGameStatus = false;
			myView.gameOver();
		}
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
		else if(cRoom.getItems().size() > 0 && !cRoom.isLooted() && activated)
		{
			myRoomController.activateTreasure(); // loots again.
		}
		// room is nothing.
	}

}