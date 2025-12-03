package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.Direction;
import model.Dungeon;
import model.GameConfig;
import model.Hero;
import model.Item;
import model.Priestess;
import model.Thief;
import model.Warrior;

public class ConsoleView implements GameView
{
	
	Scanner myUserInput;
	
	public ConsoleView()
	{
		myUserInput = new Scanner(System.in);
	}

	@Override
	public void showMessage(final String theMessage) 
	{
		System.out.println(theMessage);
	}

	@Override
	public void showDungeon(final Dungeon theDungeon) 
	{
		String dungeon = "";
		int myRows = theDungeon.getRows();
		int myCols = theDungeon.getCols();
		
		for(int i = 0; i < myRows; i++)
		{
			for(int j = 0; j < myCols; j++)
			{
				if(theDungeon.getCurrentRoom() == theDungeon.getRoom(i, j))
				{
					dungeon += 'C';
				}
				else
				{
					dungeon += theDungeon.getRoom(i, j).getRoomChar();
				}
				dungeon += ' ';
			}
			dungeon += "\n";
		}
		
		System.out.println("\n" + dungeon);
	}
	
	public void showShopItems(final ArrayList<Item> theItem)
	{
		// This is hard coded right now but should be changed later.
		String shopString = "Welcome to my shop!\n" +
							"1. Healing Potion - " + 25 + " gold\n" +
							"2. Vision Potion - " + 40 + " gold\n" +
							"3. Weapon Upgrade - " + 100 + " gold\n" +
							"4. Stop Shopping\n";
		
		System.out.println(shopString);
	}
	
	public GameConfig askGameConfig()
	{		
		return new GameConfig(askHero(), askDifficulty(), askSeed());
	}
	
	private int askDifficulty()
	{
		boolean responseGood = false;
		int diff = 0;
		while(!responseGood)
		{
			System.out.println("What Difficulty do you want?  Please choose 1-9: ");
			diff = myUserInput.nextInt();
			responseGood = (diff > 0 && diff < 9);
		}
		return diff;
	}
	
	private Hero askHero()
	{
		System.out.println("What is you Hero's Name: ");
		String heroName = myUserInput.next();
		Hero userHero = null;
		while(userHero == null)
		{
			System.out.println("What Class do you want to play? (P)riestess, (T)hief, or (W)arrior: ");
			String response = myUserInput.next().toUpperCase();
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
		}
		return userHero;
	}
	
	private long askSeed()
	{
		while(true)
		{
			System.out.println("What Seed do you want? (Input Nothing for Random Seed)");
			String input = myUserInput.next().trim();
			
			if(input.isEmpty())
			{
				long randomSeed = System.currentTimeMillis();
				System.out.println("Using Random Seed: " + randomSeed);
				return randomSeed;
			}
			
			try 
			{
				return Long.parseLong(input);
			}
			catch(NumberFormatException e) 
			{
				System.out.println("Invalid Seed. Must be a whole number.");
			}
		}
	}

	@Override
	public int askShop() 
	{
		System.out.print("What do you choose: ");
		int choice = myUserInput.nextInt();
		myUserInput.nextLine();
		
		return choice;
	}

	@Override
	public Direction askDirection() 
	{
		Direction chosenDirection = null;
		while(chosenDirection == null)
		{
			System.out.println("What direction do you want to move? (N)orth, (E)ast, (S)outh, and (W)est: ");
			String input  = myUserInput.next().toUpperCase();
			switch(input)
			{
				case "N": 
					chosenDirection = Direction.NORTH;
					break;
				case "E":
					chosenDirection = Direction.EAST;
					break;
				case "S":
					chosenDirection = Direction.SOUTH;
					break;
				case "W":
					chosenDirection = Direction.WEST;
					break;
				default:
					System.out.println("That is not a direction");
			}
		}
		return chosenDirection;
	}
	
	public void showPit(final int thePitDmg)
	{
		System.out.println("You have fallen into a pit and taken " +
						thePitDmg + "Points of Damage." );
	}
	
    public void showPillar(final char theChar)
    {
    	System.out.println("You found the " + theChar + "Pillar!");
    }
	
	public void gameOver()
	{
		System.out.println("\nGAME OVER!");
	}
	
	@Override
	public void showHeroStats(final Hero theHero) {
	    System.out.println("=== HERO STATS ===");
	    System.out.println("Name: " + theHero.getName());
	    System.out.println("HP: " + theHero.getHitPoints());
	    System.out.println("Gold: " + theHero.getGold());
	    System.out.println("===================");
	}
}