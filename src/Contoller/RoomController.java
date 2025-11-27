package Contoller;

import Model.Dungeon;
import Model.Hero;
import Model.Room;
import Model.RoomType;
import Model.Shopkeeper;
import View.GameView;

public class RoomController 
{
	private static int PIT_DMG = 4;
	
	private static int QUIT_SHOP = 4;
	
	private Hero myHero;
	
	private Dungeon myDungeon;
	
	private GameView myView;
	
	public RoomController(final Hero theHero,
						final Dungeon theDungeon,
						final GameView theView)
	{
		myHero = theHero;
		myDungeon = theDungeon;
		myView = theView;
	}
	
	
	public void activateShop()
	{
		// shop logic
		boolean isDone = false;
		while(!isDone) // not done
		{
			Room room = myDungeon.getCurrentRoom();
			Shopkeeper shop = room.getShopkeeper();
			myView.showShopItems(shop.getItems()); // need to fix later
			int input = myView.askShop();
			shop.buyItem(myHero, input);
			isDone = (input == QUIT_SHOP);
		}
	}
	
	public boolean activatePit()
	{
		// pit logic
		myHero.setHitPoints(myHero.getHitPoints() - PIT_DMG);
		myView.showPit(PIT_DMG);
		return myHero.isAlive();
		
	}
	
	public void activatePillar()
	{
	
		
	}
	
	public void activateTreasure()
	{
		
	}
	
	public void activateEncounter()
	{
		
	}
}
