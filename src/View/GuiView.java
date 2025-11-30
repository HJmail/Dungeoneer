package View;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

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

import Model.Direction;
import Model.Dungeon;
import Model.GameConfig;
import Model.Hero;
import Model.Item;
import Model.Priestess;
import Model.Thief;
import Model.Warrior;

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
    
    ArrayList<JRadioButton> myClassButtons;
    
	@Override
	public void showMessage(String message) 
	{
		
	}

	@Override
	public void showDungeon(Dungeon theDungeon) 
	{
		// TODO Auto-generated method stub
		
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
	public void showShopItems(ArrayList<Item> theItem) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int askShop() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showPit(int thePitDmg) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Direction askDirection() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showPillar(char theChar) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameOver() 
	{
		// TODO Auto-generated method stub
		
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
