package controller;

import java.awt.Point;
import java.util.Random;

import model.Direction;
import model.Dungeon;
import model.DungeonGenerator;
import model.GameConfig;
import model.Room;
import model.RoomGenerator;
import model.RoomType;
import view.ConsoleView;
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
	 * This is the Console/Gui view.
	 */
	private static GameView myView;
	
	/**
	 * This is a gameConfig File that stores main game data.
	 */
	private static GameConfig myGameConfig;
	
	/**
	 * This is the Room Controller room logic
	 */
	private static RoomController myRoomController;
	
	/**
	 * This is the seeded random.
	 */
	private static Random myRandom;
	
	/**
	 * This is the room generator.
	 */
	private static RoomGenerator myRoomGenerator;
	
	/**
	 * This is the main logic for the dungeon.
	 */
	public DungeonAdventure()
	{
		setupGame();
		play();
	}
	
	/**
	 * This method starts creating the required fields for all other functionalities.
	 */
	private void setupGame() 
	{
		myGameStatus = true;
		
		// Setting up View ... can be Console based or GUI based.
		myView = new ConsoleView();
		//myView = new GuiView(this);
		
		myRoomGenerator =  new RoomGenerator();
		
		// User inputs
		myGameConfig = myView.askGameConfig();
		
		myRandom = new Random(myGameConfig.getSeed());
		myDungeon  = DungeonGenerator.generate(myRandom,
												myGameConfig.getDifficulty(),
												myGameConfig.getHero());
		
		myRoomGenerator.generate(myDungeon, myRandom);
		
		Room startRoom = myDungeon.getCurrentRoom();
		myDungeon.setHeroTileLocation(new Point(startRoom.getTilesRows()/2,
										startRoom.getTilesCols()/2));
		
		// other controllers 
		CombatController.setView(myView);
		myRoomController = new RoomController(myGameConfig.getHero(),
											  myDungeon,
											  myView,
											  myRandom);	
		
		myView.initRoomPanel(myDungeon.getCurrentRoom(), myGameConfig);
	}
	
	/**
	 * This method plays the game when called, until myGameStatus becomes false.
	 */
	private void play()
	{
		while(myGameStatus)
		{
			promptMove();
			promptInvetory();
			checkHitPoints();
		}
		myView.showMessage("Game Halted.");
	}
	
	/**
	 * This is the hero movement within a room.
	 * @param theDirection the given Direction.
	 */
	public void moveHero(final Direction theDirection)
	{
		activateRoom(myDungeon.stepHero(theDirection, myGameConfig.getHero()));
		myView.showRoom(myDungeon.getCurrentRoom());
	}
	
	private void activateRoom(final RoomType theRoomType)
	{
		if(theRoomType == RoomType.ENCOUNTER)
		{
			CombatController.battleMultiple(myGameConfig.getHero(), myDungeon.getCurrentRoom().getMonsters());
		}
		
	}
	
	private void promptInvetory()
	{
		myView.askInventory(myGameConfig.getHero().getInventory());
	}
	
	private void promptMove()
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
		//activateRoom();
	}
	
	private void checkHitPoints()
	{
		if(!myGameConfig.getHero().isAlive())
		{
			myGameStatus = false;
			myView.gameOver();
		}
	}
	
	private void activateRoom()
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
	
	public GameConfig getGameConfig()
	{
		return myGameConfig;
	}

}