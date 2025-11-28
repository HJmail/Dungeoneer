package controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
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
import model.Dungeon;
import model.DungeonGenerator;
import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;
import view.ConsoleView;
import view.DungeoneerFrame;

/**
 *  This class is the main logic holding class for the Dungeoneer Game.
 *  
 *  <p>@author Skyler Z Broussard
 *  
 *  <p>@version 0.0.1  10/20/2025
 */
public class DungeonAdventure {

  /** This field is the main character. */
  private static Hero myHero;

  /** This boolean represents if a game is running.*/
  private static boolean myGameStatus;
 
  /** This field is the dungeon of the current game.*/
  private static Dungeon myDungeon;
  
  /** This field represents the difficulty. */
  private static int myDifficulty;
  
  /** This is the Console view. */
  private static ConsoleView myConsoleView;
  
  private static long mySeed;

  
  private static DungeoneerFrame myGui;
  
  /**
   * This is the method with the main workflow.
   * 
   * <p>@param theArgs 
   */
  
  public static void main(final String[] theArgs) {

    setupGame();

  }

  private static void setupGame() {

    myConsoleView = new ConsoleView();
    myGameStatus = true;

    // Single setup dialog – this sets myDifficulty inside promptHero()
    myHero = promptHero();
    myDungeon = DungeonGenerator.generate(mySeed, myDifficulty, myHero);

    // Create and show the GUI window
    myGui = new DungeoneerFrame(myDungeon, myHero);
    myGui.setVisible(true);

    // Optional: initial messages in the GUI log
    myGui.showMessage("Welcome to Dungeoneer!");
    myGui.showDungeon(myDungeon);
    
  }

  /**
   * Shows a single setup dialog where the player:
   *  - chooses a hero class (with sprite)
   *  - chooses difficulty (1-9 slider)
   *  - enters a hero name
   *  
   * Sets myDifficulty and returns the created Hero.
   */
  private static Hero promptHero() {

    // Hero sprites and labels
    ImageIcon warriorIcon   = loadScaledIcon("Dungeoneer_Characters/warrior_down.png", 128);
    ImageIcon thiefIcon     = loadScaledIcon("Dungeoneer_Characters/thief_down.png", 128);
    final ImageIcon priestessIcon = loadScaledIcon("Dungeoneer_Characters/priestess_down.png", 128);

    JLabel warriorLabel = new JLabel("Warrior", warriorIcon, SwingConstants.CENTER);
    warriorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    warriorLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

    JLabel thiefLabel = new JLabel("Thief", thiefIcon, SwingConstants.CENTER);
    thiefLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    thiefLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

    JLabel priestessLabel = new JLabel("Priestess", priestessIcon, SwingConstants.CENTER);
    priestessLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    priestessLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

    // Radio buttons
    JRadioButton warriorButton   = new JRadioButton("Warrior");
    JRadioButton thiefButton     = new JRadioButton("Thief");
    JRadioButton priestessButton = new JRadioButton("Priestess");

    ButtonGroup heroGroup = new ButtonGroup();
    heroGroup.add(warriorButton);
    heroGroup.add(thiefButton);
    heroGroup.add(priestessButton);

    warriorButton.setSelected(true);

    JPanel warriorPanel = new JPanel(new BorderLayout());
    warriorPanel.add(warriorLabel, BorderLayout.CENTER);
    warriorPanel.add(warriorButton, BorderLayout.SOUTH);

    JPanel thiefPanel = new JPanel(new BorderLayout());
    thiefPanel.add(thiefLabel, BorderLayout.CENTER);
    thiefPanel.add(thiefButton, BorderLayout.SOUTH);

    JPanel priestessPanel = new JPanel(new BorderLayout());
    priestessPanel.add(priestessLabel, BorderLayout.CENTER);
    priestessPanel.add(priestessButton, BorderLayout.SOUTH);

    JPanel heroesRow = new JPanel(new GridLayout(1, 3, 10, 0));
    heroesRow.add(warriorPanel);
    heroesRow.add(thiefPanel);
    heroesRow.add(priestessPanel);

    // Difficulty slider
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

    // Hero Name
    JTextField nameField = new JTextField(12);
    JPanel namePanel = new JPanel(new BorderLayout());
    namePanel.add(new JLabel("Hero Name: "), BorderLayout.WEST);
    namePanel.add(nameField, BorderLayout.CENTER);

    // Dungeon Seed (NEW!)
    JTextField seedField = new JTextField(12);
    JPanel seedPanel = new JPanel(new BorderLayout());
    seedPanel.add(new JLabel("Dungeon Seed: "), BorderLayout.WEST);
    seedPanel.add(seedField, BorderLayout.CENTER);

    // Main stacked panel
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.add(Box.createVerticalStrut(5));

    JLabel title = new JLabel("Choose your Hero and Difficulty", SwingConstants.CENTER);
    title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    content.add(title);

    content.add(Box.createVerticalStrut(10));
    content.add(heroesRow);
    content.add(Box.createVerticalStrut(10));
    content.add(diffPanel);
    content.add(Box.createVerticalStrut(10));
    content.add(namePanel);
    content.add(Box.createVerticalStrut(10));
    content.add(seedPanel);     // <-- NEW FIELD INSERTED

    // Show dialog
    int result = JOptionPane.showConfirmDialog(
            null,
            content,
            "Dungeoneer Setup",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result != JOptionPane.OK_OPTION) {
      System.exit(0);
    }

    // Assign difficulty
    myDifficulty = diffSlider.getValue();

    // Hero name
    String heroName = nameField.getText().trim();
    if (heroName.isEmpty()) {
      heroName = "Hero";
    }
    
    // Seed logic (moved into this dialog)
    String seedInput = seedField.getText().trim();
    if (seedInput.isEmpty()) {
      mySeed = new Random().nextLong();
    } else {
      try {
        mySeed = Long.parseLong(seedInput);
      } catch (Exception ex) {
        mySeed = new Random().nextLong();
      }
    }

    // Create hero
    if (priestessButton.isSelected()) {
      return new Priestess(heroName);
    }
    
    if (thiefButton.isSelected()) {
      return new Thief(heroName);

    }
    return new Warrior(heroName);

  }

  /**
   * Loads an image and scales it to the given size (square).
   */
  private static ImageIcon loadScaledIcon(final String path, final int size) {
    ImageIcon icon = new ImageIcon(path);
    Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
  }

  /**
   * Returns a text description for a given difficulty value.
   */
  private static String difficultyDescription(final int diff) {
    if (diff <= 2) {
      return diff + " - Beginner. New to Dungeoneer!";
    } else if (diff <= 3) {
      return diff + " - Easy. Not too hard and not too easy.";
    } else if (diff <= 5) {
      return diff + " - Regular. The original game.";
    } else if (diff <= 8) {
      return diff + " - Hardened. For the experienced to challenge themselves.";
    } else {
      return diff + " - Veteran. You won't survive...";
    }
  }
}