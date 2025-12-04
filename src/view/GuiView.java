package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.Direction;
import model.Dungeon;
import model.GameConfig;
import model.Hero;
import model.Item;
import model.Monster;
import model.Priestess;
import model.Thief;
import model.Warrior;

public class GuiView implements GameView
{
    final static ImageIcon WARRIOR_ICON = loadScaledIcon("/Dungeoneer Characters/warrior_down.png", 128);
    final static ImageIcon THIEF_ICON = loadScaledIcon("/Dungeoneer Characters/thief_down.png", 128);
    final static ImageIcon PRIESTESS_ICON = loadScaledIcon("/Dungeoneer Characters/priestess_down.png", 128);
	
    JSlider myDiffSlider;
    
    JTextField myNameField;
    
    JTextField mySeedField;
    
    Hero myHero;
    
    long mySeed;
    
    ArrayList<JRadioButton> myClassButtons; // implement better coding practice for GUI
    
    Scanner myUserInput; // temp for testing
    
	public GuiView()
	{
		myUserInput = new Scanner(System.in);
	}
	
	@Override
	public void showHeroStats(final Hero theHero) {
	    // GUI version does not manually print stats;
	    // DungeoneerFrame handles updating the HUD.
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
	    JPanel mainPanel = createMainPanel(createHeroSelectionPanel(),
	    									createHeroTextPanel(),
	    									createDifficultySlider(),
	    									createSeedSelection());
	    
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
	
		return getGameConfig();
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
		System.out.println("You found the " + theChar + "Pillar!");
	}

	@Override
	public void gameOver() 
	{
		System.out.println("\nGAME OVER!");
	}

	private static ImageIcon loadScaledIcon(final String path, final int size) 
	{
	    ImageIcon icon = new ImageIcon(GuiView.class.getResource(path));
	    Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaled);
	}
	
	private JPanel createHeroSelectionPanel()
	{
		// icons
	    JLabel warriorLabel = new JLabel("Warrior", WARRIOR_ICON, SwingConstants.CENTER);
	    warriorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    warriorLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
	    
	    JLabel thiefLabel = new JLabel("Thief", THIEF_ICON, SwingConstants.CENTER);
	    thiefLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    thiefLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
	    
	    JLabel priestessLabel = new JLabel("Priestess", PRIESTESS_ICON, SwingConstants.CENTER);
	    priestessLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    priestessLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
	    
	    // radio buttons
	    JRadioButton warriorButton = new JRadioButton("Warrior");
	    JRadioButton thiefButton = new JRadioButton("Thief");
	    JRadioButton priestessButton = new JRadioButton("Priestess");
	    
	    warriorButton.setActionCommand("Warrior");
	    thiefButton.setActionCommand("Thief");
	    priestessButton.setActionCommand("Priestess");
	    
	    myClassButtons = new ArrayList<>();
	    myClassButtons.add(warriorButton);
	    myClassButtons.add(thiefButton);
	    myClassButtons.add(priestessButton);

	    ButtonGroup heroGroup = new ButtonGroup();
	    heroGroup.add(warriorButton);
	    heroGroup.add(thiefButton);
	    heroGroup.add(priestessButton);

	    warriorButton.setSelected(true); // base case

	    // Hero Panels
	    JPanel warriorPanel = new JPanel(new BorderLayout());
	    warriorPanel.add(warriorLabel, BorderLayout.CENTER);
	    warriorPanel.add(warriorButton, BorderLayout.SOUTH);

	    JPanel thiefPanel = new JPanel(new BorderLayout());
	    thiefPanel.add(thiefLabel, BorderLayout.CENTER);
	    thiefPanel.add(thiefButton, BorderLayout.SOUTH);

	    JPanel priestessPanel = new JPanel(new BorderLayout());
	    priestessPanel.add(priestessLabel, BorderLayout.CENTER);
	    priestessPanel.add(priestessButton, BorderLayout.SOUTH);

	    // Main panel
	    JPanel heroesRow = new JPanel(new GridLayout(1, 3, 10, 0));
	    heroesRow.add(warriorPanel);
	    heroesRow.add(thiefPanel);
	    heroesRow.add(priestessPanel);
	    
	    return heroesRow;
	}
	
	private JPanel createHeroTextPanel()
	{
	    JTextField nameField = new JTextField(12);
	    JPanel namePanel = new JPanel(new BorderLayout());
	    namePanel.add(new JLabel("Hero Name: "), BorderLayout.WEST);
	    namePanel.add(nameField, BorderLayout.CENTER);
	    
	    myNameField = nameField;
	    return namePanel;
	}
	
	private JPanel createDifficultySlider()
	{
		JSlider diffSlider = new JSlider(1, 9, 5);
	    diffSlider.setMajorTickSpacing(1);
	    diffSlider.setPaintTicks(true);
	    diffSlider.setPaintLabels(true);
	    diffSlider.setSnapToTicks(true);

	    JLabel diffLabel = new JLabel(difficultyDescription(diffSlider.getValue()));
	    diffLabel.setHorizontalAlignment(SwingConstants.CENTER);

	    diffSlider.addChangeListener(e ->
	        diffLabel.setText(difficultyDescription(diffSlider.getValue()))
	    );

	    JPanel diffPanel = new JPanel(new BorderLayout());
	    diffPanel.add(new JLabel("Difficulty (1–9):", SwingConstants.CENTER),
	                  BorderLayout.NORTH);
	    diffPanel.add(diffSlider, BorderLayout.CENTER);
	    diffPanel.add(diffLabel, BorderLayout.SOUTH);
	    
	    myDiffSlider = diffSlider;
	    return diffPanel;
	}
	
	private JPanel createSeedSelection()
	{
	    JTextField seedField = new JTextField(12);
	    JPanel seedPanel = new JPanel(new BorderLayout());
	    seedPanel.add(new JLabel("Dungeon Seed: "), BorderLayout.WEST);
	    seedPanel.add(seedField, BorderLayout.CENTER);
	    
	    mySeedField = seedField;
	    return seedPanel;
	}
	
	private JPanel createMainPanel(final JPanel theHeroSelection,
									final JPanel theHeroName,
									final JPanel theDifficultySlider,
									final JPanel theSeedSelection)
	{
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createVerticalStrut(5));
	
	    JLabel title = new JLabel("Choose your Hero and Difficulty", SwingConstants.CENTER);
	    title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	    mainPanel.add(title);
	    
	    mainPanel.add(Box.createVerticalStrut(10));
	    mainPanel.add(theHeroSelection);
	    mainPanel.add(Box.createVerticalStrut(10));
	    mainPanel.add(theHeroName);
	    mainPanel.add(Box.createVerticalStrut(10));
	    mainPanel.add(theDifficultySlider);
	    mainPanel.add(Box.createVerticalStrut(10));
	    mainPanel.add(theSeedSelection);
		
		return mainPanel;
	}
	
	private String difficultyDescription(final int diff) 
	{
	    if (diff <= 2) 
	    {
	      return diff + " - Beginner. New to Dungeoneer!";
	    } 
	    else if (diff <= 3) 
	    {
	      return diff + " - Easy. Not too hard and not too easy.";
	    } 
	    else if (diff <= 5) 
	    {
	      return diff + " - Regular. The original game.";
	    } 
	    else if (diff <= 8) 
	    {
	      return diff + " - Hardened. For the experienced to challenge themselves.";
	    } 
	    else 
	    {
	      return diff + " - Veteran. You won't survive...";
	    }
	  }
	
	@Override
	public String askCombatChoice(final Hero hero, final Monster monster) {
	    // Simple temporary implementation:
	    System.out.println(hero.getName() + " is fighting " + monster.getName()
	                       + " (GUI stub) – defaulting to NORMAL attack.");
	    return "NORMAL";
	}

	
	private GameConfig getGameConfig()
	{
		// hero name
		String heroName = myNameField.getText().trim();
		if(heroName.isEmpty())
		{
			heroName = "Dungeoneer";
		}
		
		String selectedClass = "Warrior";
		// hero creation -- need hero factory
		for(JRadioButton button: myClassButtons)
		{
			if(button.isSelected())
			{
				selectedClass= button.getActionCommand();
				break;
			}
		}
		//temp until factory
		switch(selectedClass)
		{
			case "Warrior": 
				myHero = new Warrior(heroName);
			case "Thief":
				myHero = new Thief(heroName);
			case "Priestess":
				myHero = new Priestess(heroName);
		}
		
	    String seedInput = mySeedField.getText().trim();
	    if (seedInput.isEmpty()) {
	      mySeed = new Random().nextLong();
	    } else {
	      try {
	        mySeed = Long.parseLong(seedInput);
	      } catch (Exception ex) {
	        mySeed = new Random().nextLong();
	      }
	    }
	    
	    // difficulty
		int difficulty = myDiffSlider.getValue();
		
		return new GameConfig(myHero, difficulty, mySeed);
	}
}