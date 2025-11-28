package Contoller;

import java.util.Random;

import Model.Dungeon;
import Model.Hero;
import Model.ItemType;
import Model.Pillar;
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
	
	private Random myRandom;
	
	private int myPillarCount;
	
	public RoomController(final Hero theHero,
						final Dungeon theDungeon,
						final GameView theView,
						final Random theRandom)
	{
		myHero = theHero;
		myDungeon = theDungeon;
		myView = theView;
		myRandom  = theRandom;
		myPillarCount = 0;
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
		char[] pillars = {'A', 'E', 'I', 'P'};
		myHero.getInventory().addItem(new Pillar(pillars[myPillarCount]));
		myPillarCount++;
	}
	
	public void activateTreasure()
	{
		int depth = myDungeon.getCurrentRoom().getDepth();
		for(ItemType it: myDungeon.getCurrentRoom().getItems())
		{
			if(it == ItemType.HEALING_POTION)
			{
				
			}
			else if(it == ItemType.GOLD)
			{
				
			}
			else if(it == ItemType.VISION_POTION)
			{
				
			}
			else if(it == ItemType.WEAPON)
			{
				
			}
		}
		
	}
	
	public void activateEncounter()
	{
		// When the hero wins
		activateTreasure();
	}
}
