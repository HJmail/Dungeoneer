package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Direction;
import model.Dungeon;
import model.GameConfig;
import model.Hero;
import model.Inventory;
import model.Item;
import model.Potion;
import model.Room;

public class GuiView implements GameView
{	
    Scanner myUserInput; // temp for testing
    
    GameConfigPanel myGameConfigPanel;
    
	public GuiView()
	{
		myUserInput = new Scanner(System.in);
		myGameConfigPanel = new GameConfigPanel();
	}
    
	@Override
	public void showMessage(final String theMessage) 
	{
		System.out.println(theMessage);
	}

	@Override
	public void showDungeon(final Dungeon theDungeon) 
	{
		// NEED TO IMPLEMENT GUI HERE
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
				else if(!theDungeon.getRoom(i, j).isVisable())
				{
					dungeon += '*';
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

	@Override
	public GameConfig askGameConfig() 
	{   
	    JPanel mainPanel = myGameConfigPanel.getPanel();
	    
	    // prompt dialog
	    int result = JOptionPane.showConfirmDialog(
	            null,
	            mainPanel,
	            "Dungeoneer Setup",
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE
	    );
	    
	    // exit if not OK
	    if (result != JOptionPane.OK_OPTION) 
	    {
	        System.exit(0);
	    }
		return myGameConfigPanel.getGameConfig();
	}

	@Override
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
		System.out.println("Need to implement shop GUI");
		return 0;
	}

	@Override
	public void showPit(int thePitDmg) 
	{
		System.out.println("You have fallen into a pit and taken " +
				thePitDmg + "Points of Damage." );
	}

	@Override
	public Direction askDirection()  // need to implement GUI version
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

	@Override
	public void showPillar(char theChar) 
	{
		System.out.println("You found the " + theChar + " Pillar!");
	}

	@Override
	public void gameOver() 
	{
		System.out.println("\nGAME OVER!");
	}


	@Override
	public void askCombat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askInventory(final Inventory theInventory) 
	{
		boolean goodResponse = false;
		List<Item> inv = theInventory.getInventory();
		
		while(!goodResponse && inv.size() > 0)
		{
			boolean input = promptYesNo("Would you like to interact with Inventory? y/n:");
			if(input)
			{
				for(int i = 0; i < inv.size(); i++)
				{
					Item item = inv.get(i);
					System.out.println(i+1 + ". " + item.getName());
				}
				System.out.println((inv.size() + 1) + ". cancel");
				
				int itemSelection = 0;
				while(itemSelection > inv.size() + 1 || itemSelection < 1) // not in range
				{
					itemSelection = myUserInput.nextInt() - 1;
					if(itemSelection == inv.size())
					{
						break;
					}
					else if(inv.get(itemSelection) instanceof Potion) // not ideal but works...
					{ //only potions can get used
						boolean consume = promptYesNo("Would you like to consume this potion? y/n:");
						if(consume) System.out.println("Consume"); // consume potion logic...
					}
					else // weapon 
					{
						boolean drop = promptYesNo("Would you like to drop this Weapon? y/n:");
						if(drop) System.out.println("Drop"); // drop logic.
					}
				}
			}
			else 
			{
				goodResponse = true;
				break;
			}
		}
		if(inv.size() <= 0) System.out.println("Your inventory is empty.");
	}


	private boolean promptYesNo(final String theString)
	{
		while(true)
		{
			System.out.println(theString);
			String input = myUserInput.next().trim().toLowerCase();
			if(input.equals("y"))
			{
				return true;
			}
			else if(input.equals("n"))
			{
				return false;
			}
		}
	}

	@Override
	public void showHeroStats(Hero myOwner) {
		// TODO Auto-generated method stub
		
	}
	
	public void showRoom(final Room theRoom)
	{
		String stringOfRoom = theRoom.toString();
	}
}