package controller;

import java.util.Random;

import model.Dungeon;
import model.HealingPotion;
import model.Hero;
import model.Item;
import model.ItemType;
import model.Pillar;
import model.Rarity;
import model.Room;
import model.Shopkeeper;
import model.VisionPotion;
import model.Weapon;
import view.GameView;

public class RoomController 
{
	private static int PIT_DMG = 4;
	
	private static int QUIT_SHOP = 4;
	
	private static int HEAL_POT = 4;
	
	private static int VISION_POT = 1;
	
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
			myView.showMessage("\n" + shop.buyItem(myHero, input) + "\n");
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
		myView.showPillar(pillars[myPillarCount]);
		myPillarCount++;
	}
	
	public void activateTreasure()
	{
		int depth = myDungeon.getCurrentRoom().getDepth();
		for(ItemType it: myDungeon.getCurrentRoom().getItems())
		{
			int roll = myRandom.nextInt(100);
			Rarity rarity = getRarity(roll);
			
			if(it == ItemType.HEALING_POTION)
			{
				addToInvetory(new HealingPotion(HEAL_POT, rarity));
			}
			else if(it == ItemType.GOLD)
			{
				int goldrng = (int) (depth * (8 + (roll * 0.1)));
				myHero.setGold(myHero.getGold() + goldrng);
			}
			else if(it == ItemType.VISION_POTION)
			{
				addToInvetory(new VisionPotion(VISION_POT, rarity));
			}
			else if(it == ItemType.WEAPON)
			{
				Weapon newWeapon;
				int weaponRoll = myRandom.nextInt(4);
				switch(weaponRoll)
				{
					case 1:
						newWeapon = Weapon.createSpear(rarity);
					case 2:
						newWeapon = Weapon.createFlail(rarity);
					case 3:
						newWeapon = Weapon.createFalchion(rarity);
					case 4:
						newWeapon = Weapon.createMorningStar(rarity);
					default:
						newWeapon = Weapon.createStick(rarity);
				}
				addToInvetory(newWeapon);
			}
		}
	}
	
	private void addToInvetory(final Item theItem)
	{ 
		try
		{
			myHero.getInventory().addItem(theItem);
		}
		catch(IllegalStateException e)
		{
			myDungeon.getCurrentRoom().setIsLooted(false);
			myView.showMessage("Could not collect a item");
		}
	}
	
	public void activateEncounter()
	{
		CombatController.battleMultiple(myHero,
							myDungeon.getCurrentRoom().getMonsters());
		
		// When the hero wins
		if(myHero.isAlive());
		{
			activateTreasure();
		}
	}
	
	public Rarity getRarity(final int theRoll)
	{ 	// Roll is 1-100
		Rarity rarity = Rarity.COMMON;
		if(theRoll > 50 && theRoll < 75)
		{
			rarity = Rarity.UNCOMMON;
		}
		else if(theRoll > 75 && theRoll < 88)
		{
			rarity = Rarity.RARE;
		}
		else if(theRoll > 88 && theRoll < 94)
		{
			rarity = Rarity.EPIC;
		}
		else if(theRoll > 94)
		{
			rarity = Rarity.LEGENDARY;
		}
		return rarity;
	}
}
