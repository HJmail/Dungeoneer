package View;

import java.util.ArrayList;
import java.util.Scanner;

import Model.Direction;
import Model.Dungeon;
import Model.Hero;
import Model.Item;

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

	@Override
	public void showCombatStart() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showCombatResult() 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void gameOver()
	{
		System.out.println("\nGAME OVER!");
	}
}
